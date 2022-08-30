package com.sun.yygh.hosp.service.impl;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.yygh.common.exception.YyghException;
import com.sun.yygh.common.result.ResultCodeEnum;
import com.sun.yygh.hosp.mapper.ScheduleMapper;
import com.sun.yygh.hosp.repository.ScheduleRepository;
import com.sun.yygh.hosp.service.DepartmentService;
import com.sun.yygh.hosp.service.HospitalService;
import com.sun.yygh.hosp.service.ScheduleService;
import com.sun.yygh.model.hosp.BookingRule;
import com.sun.yygh.model.hosp.Department;
import com.sun.yygh.model.hosp.Hospital;
import com.sun.yygh.model.hosp.Schedule;
import com.sun.yygh.vo.hosp.BookingScheduleRuleVo;
import com.sun.yygh.vo.hosp.ScheduleOrderVo;
import com.sun.yygh.vo.hosp.ScheduleQueryVo;
import org.ehcache.core.util.CollectionUtil;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-24 12:08
 **/
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule> implements ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;

    @Override
    public void update(Schedule schedule) {
        schedule.setUpdateTime(new Date());
        scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getDetail(String hoscode, String depcode, String workDate) {
        List<Schedule> list = scheduleRepository.findScheduleByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        list.stream().forEach(item ->{
            this.packageSchedule(item);
        });
        return list;
    }

    private Schedule packageSchedule(Schedule schedule) {
        schedule.getParam().put("hosname", hospitalService.getHosName(schedule.getHoscode()));
        schedule.getParam().put("depname", departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
        return schedule;
    }

    @Override
    public Map<String, Object> getRule(Integer page, Integer limit, String hoscode, String depcode) {
        Criteria criteria = Criteria.where("hoscode").is(hoscode)
                .and("depcode").is(depcode);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
                        .first("workDate").as("workDate")
                .count().as("docCount")
                .sum("reservedNumber").as("reservedNumber")
                .sum("availableNumber").as("availableNumber"),
                Aggregation.sort(Sort.Direction.DESC, "workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        Aggregation total = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")
        );
        AggregationResults<BookingScheduleRuleVo> aggregate1 = mongoTemplate.aggregate(total, Schedule.class, BookingScheduleRuleVo.class);
        int size = aggregate1.getMappedResults().size();
        for (BookingScheduleRuleVo mappedResult : mappedResults) {
            Date workDate = mappedResult.getWorkDate();
            String dayOfWeek = this.getDayOfWeek(new DateTime(workDate));
            mappedResult.setDayOfWeek(dayOfWeek);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("bookingScheduleRuleList",mappedResults);
        result.put("total",size);
        String hosName = hospitalService.getHosName(hoscode);
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hosName);
        result.put("baseMap", baseMap);
        return result;
    }

    /**
     * 根据日期获取周几数据
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }



    @Override
    public void remove(String hoscode, String scheduleId) {
        Schedule scheduleByHoscodeAndHosScheduleId = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode, scheduleId);
        if (scheduleByHoscodeAndHosScheduleId != null) {
            scheduleRepository.deleteById(scheduleByHoscodeAndHosScheduleId.getId());
        }
    }

    @Override
    public void save(Map<String, Object> map) {

        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(map), Schedule.class);
        Schedule s = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(),schedule.getHosScheduleId());
        if (s != null) {
            s.setUpdateTime(new Date());
            s.setIsDeleted(0);
            s.setStatus(1);
            scheduleRepository.save(s);
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(0);
            schedule.setStatus(1);
            scheduleRepository.save(schedule);
        }



    }

    @Override
    public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        schedule.setIsDeleted(0);
        Example<Schedule> example = Example.of(schedule, matcher);
        Page<Schedule> all = scheduleRepository.findAll(example, pageable);
        return all;
    }

    @Override
    public Map<String, Object> getBookingScheduleRule(int page, int limit, String hoscode, String depcode) {

        Map<String, Object> result = new HashMap<>();

        //获取预约规则
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        if(null == hospital) {
            throw new YyghException(ResultCodeEnum.DATA_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();

        //获取可预约日期分页数据
        IPage iPage = this.getListDate(page, limit, bookingRule);
        //当前页可预约日期
        List<Date> dateList = iPage.getRecords();
        //获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(dateList);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregationResults = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> scheduleVoList = aggregationResults.getMappedResults();
        //获取科室剩余预约数

        //合并数据 将统计数据ScheduleVo根据“安排日期”合并到BookingRuleVo
        Map<Date, BookingScheduleRuleVo> scheduleVoMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(scheduleVoList)) {
            scheduleVoMap = scheduleVoList.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, BookingScheduleRuleVo -> BookingScheduleRuleVo));
        }
        //获取可预约排班规则
        List<BookingScheduleRuleVo> bookingScheduleRuleVoList = new ArrayList<>();
        for(int i=0, len=dateList.size(); i<len; i++) {
            Date date = dateList.get(i);

            BookingScheduleRuleVo bookingScheduleRuleVo = scheduleVoMap.get(date);
            if(null == bookingScheduleRuleVo) { // 说明当天没有排班医生
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //就诊医生人数
                bookingScheduleRuleVo.setDocCount(0);
                //科室剩余预约数  -1表示无号
                bookingScheduleRuleVo.setAvailableNumber(-1);
            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //计算当前预约日期为周几
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            if(i == len-1 && page == iPage.getPages()) {
                bookingScheduleRuleVo.setStatus(1);
            } else {
                bookingScheduleRuleVo.setStatus(0);
            }
            //当天预约如果过了停号时间， 不能预约
            if(i == 0 && page == 1) {
                DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if(stopTime.isBeforeNow()) {
                    //停止预约
                    bookingScheduleRuleVo.setStatus(-1);
                }
            }
            bookingScheduleRuleVoList.add(bookingScheduleRuleVo);
        }

        //可预约日期规则数据
        result.put("bookingScheduleList", bookingScheduleRuleVoList);
        result.put("total", iPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.getHosName(hoscode));
        //科室
        Department department =departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
//月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
//放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
//停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        return result;

    }

    private IPage<Date> getListDate(int page, int limit, BookingRule bookingRule) {
        DateTime releaseTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        int cycle = bookingRule.getCycle();
        if (releaseTime.isBeforeNow()) cycle += 1;
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            DateTime dateTime = new DateTime().plusDays(i);
            String s = dateTime.toString("yyyy-MM-dd");
            dateList.add(new DateTime(s).toDate());
        }
        List<Date> pageDateList = new ArrayList<>();
        int start = (page - 1) * limit;
        int end = (page - 1) * limit + limit;
        if (end > dateList.size()) end = dateList.size();
        for (int i = start; i < end; i++) {
            pageDateList.add(dateList.get(i));
        }
        IPage<Date> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, 7, dateList.size());
        iPage.setRecords(pageDateList);
        return iPage;
    }

    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    @Override
    public Schedule getById(String id) {
        Schedule schedule = scheduleRepository.findById(id).get();
        return this.packageSchedule(schedule);
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        if(null == schedule) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        Hospital hospital = hospitalService.getByHoscode(schedule.getHoscode());
        if(null == hospital) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        BookingRule bookingRule = hospital.getBookingRule();
        if(null == bookingRule) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        scheduleOrderVo.setHoscode(schedule.getHoscode());
        scheduleOrderVo.setHosname(hospitalService.getHosName(schedule.getHoscode()));
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepName(schedule.getHoscode(), schedule.getDepcode()));
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());

        int quitDay = Integer.parseInt(bookingRule.getQuitDay());
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(),
                bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());
        //预约开始时间
        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());

        //预约截止时间
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());

        //当天停止挂号时间
        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStartTime(startTime.toDate());
        return scheduleOrderVo;

    }
}

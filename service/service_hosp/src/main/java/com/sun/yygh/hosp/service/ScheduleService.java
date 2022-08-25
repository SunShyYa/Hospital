package com.sun.yygh.hosp.service;

import com.sun.yygh.model.hosp.Schedule;
import com.sun.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-24 12:08
 **/
public interface ScheduleService {
    Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void save(Map<String, Object> map);

    void remove(String hoscode, String scheduleId);

    Map<String, Object> getRule(Integer page, Integer limit, String hoscode, String depcode);

    List<Schedule> getDetail(String hoscode, String depcode, String workDate);
}

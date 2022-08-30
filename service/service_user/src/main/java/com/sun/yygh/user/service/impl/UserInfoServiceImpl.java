package com.sun.yygh.user.service.impl;

import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.yygh.common.exception.YyghException;
import com.sun.yygh.common.help.JwtHelper;
import com.sun.yygh.common.result.ResultCodeEnum;
import com.sun.yygh.enums.AuthStatusEnum;
import com.sun.yygh.model.user.Patient;
import com.sun.yygh.model.user.UserInfo;
import com.sun.yygh.model.user.UserLoginRecord;
import com.sun.yygh.user.mapper.UserInfoMapper;
import com.sun.yygh.user.service.PatientService;
import com.sun.yygh.user.service.UserInfoService;
import com.sun.yygh.vo.user.LoginVo;
import com.sun.yygh.vo.user.UserAuthVo;
import com.sun.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-28 11:52
 **/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PatientService patientService;

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        String name = userInfoQueryVo.getKeyword();
        Integer status = userInfoQueryVo.getStatus();
        Integer authStatus = userInfoQueryVo.getAuthStatus();
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(status)) {
            wrapper.eq("status",status);
        }
        if(!StringUtils.isEmpty(authStatus)) {
            wrapper.eq("auth_status",authStatus);
        }
        if(!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time",createTimeBegin);
        }
        if(!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time",createTimeEnd);
        }
        IPage<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        pages.getRecords().stream().forEach(item -> {
            this.packageUserInfo(item);
        });
        return pages;
    }

    private UserInfo packageUserInfo(UserInfo userInfo) {
        //处理认证状态编码
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        //处理用户状态 0  1
        String statusString = userInfo.getStatus().intValue()==0 ?"锁定" : "正常";
        userInfo.getParam().put("statusString",statusString);
        return userInfo;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if (status.intValue() == 0 || status.intValue() == 1) {
            UserInfo userInfo = this.getById(userId);
            userInfo.setStatus(status);
            this.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> show(Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserInfo userInfo = this.packageUserInfo(baseMapper.selectById(userId));
        map.put("userInfo", userInfo);
        List<Patient> allUserId = patientService.findAllUserId(userId);
        map.put("patientList", allUserId);
        return map;
    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        if(authStatus.intValue() == 2 || authStatus.intValue() == -1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }

    }

    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        UserInfo userInfo = baseMapper.selectById(userId);
        userInfo.setName(userAuthVo.getName());
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        //进行信息更新
        baseMapper.updateById(userInfo);

    }

    @Override
    public UserInfo getByOpenid(String openId) {
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openId);
        UserInfo userInfo = baseMapper.selectOne(wrapper);
        return userInfo;
    }

    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }

        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!redisCode.equals(code)) {
            throw new YyghException(ResultCodeEnum.CODE_ERROR);
        }

        //绑定手机号码
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = this.getByOpenid(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                this.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }

        //userInfo=null 说明手机直接登录
        if(null == userInfo) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            userInfo = baseMapper.selectOne(queryWrapper);
            if(null == userInfo) {
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                this.save(userInfo);
            }
        }


        if (userInfo.getStatus() == 0) {
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }



        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        String token = JwtHelper.createToken(userInfo.getId(), name);
        map.put("name", name);
        map.put("token",token);
        return map;
    }
}

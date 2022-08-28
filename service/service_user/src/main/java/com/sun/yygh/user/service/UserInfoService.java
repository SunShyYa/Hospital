package com.sun.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.yygh.model.user.UserInfo;
import com.sun.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-28 11:51
 **/
public interface UserInfoService extends IService<UserInfo> {
    Map<String, Object> loginUser(LoginVo loginVo);

    UserInfo getByOpenid(String openId);
}

package com.sun.yygh.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.yygh.model.user.UserInfo;
import com.sun.yygh.vo.user.LoginVo;
import com.sun.yygh.vo.user.UserAuthVo;
import com.sun.yygh.vo.user.UserInfoQueryVo;


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

    void userAuth(Long userId, UserAuthVo userAuthVo);

    IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo);

    void lock(Long userId, Integer status);

    Map<String, Object> show(Long userId);

    void approval(Long userId, Integer authStatus);
}

package com.sun.yygh.user.api;

import com.alibaba.nacos.client.utils.IPUtil;
import com.sun.yygh.common.result.Result;
import com.sun.yygh.common.utils.AuthContextHolder;
import com.sun.yygh.common.utils.IpUtil;
import com.sun.yygh.model.user.UserInfo;
import com.sun.yygh.user.service.UserInfoService;
import com.sun.yygh.vo.user.LoginVo;
import com.sun.yygh.vo.user.UserAuthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-28 11:49
 **/
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        loginVo.setIp(IpUtil.getIpAddr(request));
        Map<String, Object> map = userInfoService.loginUser(loginVo);
        return Result.ok(map);
    }

    @PostMapping("auth/userAuth")
    public Result useAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        userInfoService.userAuth(AuthContextHolder.getUserId(request), userAuthVo);
        return Result.ok();
    }

    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }
}

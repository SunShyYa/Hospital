package com.sun.yygh.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.yygh.common.result.Result;
import com.sun.yygh.model.user.UserInfo;
import com.sun.yygh.user.service.UserInfoService;
import com.sun.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 13:45
 **/
@RestController
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("{page}/{limit}")
    public Result list(@PathVariable Long page,
                       @PathVariable Long limit,
                       UserInfoQueryVo userInfoQueryVo) {
        Page<UserInfo> pageParam = new Page<>(page, limit);
        IPage<UserInfo> userInfoIPage = userInfoService.selectPage(pageParam, userInfoQueryVo);
        return Result.ok(userInfoIPage);
    }

    @GetMapping("lock/{userId}/{status}")
    public Result lock(@PathVariable Long userId,
                       @PathVariable Integer status) {
        userInfoService.lock(userId, status);
        return Result.ok();
    }

    @GetMapping("show/{userId}")
    public Result show(@PathVariable Long userId) {
        Map<String, Object> map = userInfoService.show(userId);
        return Result.ok(map);
    }

    @GetMapping("approval/{userId}/{authStatus}")
    public Result approval(@PathVariable Long userId,
                           @PathVariable Integer authStatus) {
        userInfoService.approval(userId, authStatus);
        return Result.ok();
    }
}

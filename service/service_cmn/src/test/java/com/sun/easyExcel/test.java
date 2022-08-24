package com.sun.easyExcel;

import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 13:28
 **/
public class test {
    public static void main(String[] args) {
        List<UserData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            UserData userData = new UserData();
            userData.setId(i);
            userData.setName("luck" + i);
            list.add(userData);
        }
        String fileName = "C:\\Users\\Lenovo\\Desktop\\新建文件夹\\01.xlsx";
        EasyExcel.write(fileName, UserData.class).sheet("用户信息").doWrite(list);
    }
}

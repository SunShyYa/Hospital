package com.sun.easyExcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 13:27
 **/
@Data
public class UserData {

    @ExcelProperty("编号")
    private  int id;

    @ExcelProperty("姓名")
    private String name;
}

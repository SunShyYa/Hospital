package com.sun.yygh.vo.cmn;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 12:35
 **/
@Data
public class DictEeVo {

    @ExcelProperty(value = "id",index = 0)
    private Long id;

    @ExcelProperty(value = "上级id", index = 1)
    private Long parentId;

    @ExcelProperty(value = "名称", index = 2)
    private String name;

    @ExcelProperty(value = "值", index = 3)
    private String value;

    @ExcelProperty(value = "编码", index = 4)
    private String dictCode;

}

package com.sun.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 12:43
 **/
@Data
@ApiModel(description = "Department")
public class DepartmentVo {

    @ApiModelProperty(value = "科室编号")
    private String depcode;

    @ApiModelProperty(value = "科室名称")
    private String depname;

    @ApiModelProperty(value = "下级节点")
    private List<DepartmentVo> children;
}

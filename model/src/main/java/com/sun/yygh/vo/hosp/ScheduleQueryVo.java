package com.sun.yygh.vo.hosp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 12:49
 **/
@Data
@ApiModel(description = "Schedule")
public class ScheduleQueryVo {

    @ApiModelProperty(value = "医院编号")
    private String hoscode;

    @ApiModelProperty(value = "科室编号")
    private String depcode;

    @ApiModelProperty(value = "医生编号")
    private String doccode;

    @ApiModelProperty(value = "安排日期")
    private Date workDate;

    @ApiModelProperty(value = "安排时间（0：上午 1：下午）")
    private Integer workTime;
}

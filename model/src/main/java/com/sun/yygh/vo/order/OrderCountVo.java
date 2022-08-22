package com.sun.yygh.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 12:53
 **/
@Data
@ApiModel(description = "OrderCountVo")
public class OrderCountVo {

    @ApiModelProperty(value = "安排日期")
    private String reserveDate;

    @ApiModelProperty(value = "预约单数")
    private Integer count;

}

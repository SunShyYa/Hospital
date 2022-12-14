package com.sun.yygh.model.hosp;

import com.alibaba.fastjson.JSONArray;
import com.sun.yygh.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 10:55
 **/
@Data
@ApiModel(description = "Hospital")
@Document("Hospital")
public class Hospital extends BaseMongoEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "医院编号")
    @Indexed(unique = true)
    private String hoscode;

    @ApiModelProperty(value = "医院名称")
    private String hosname;

    @ApiModelProperty(value = "医院类型")
    private String hostype;

    @ApiModelProperty(value = "省code")
    private String provinceCode;

    @ApiModelProperty(value = "市code")
    private String cityCode;

    @ApiModelProperty(value = "区code")
    private String districtCode;

    @ApiModelProperty(value = "详情地址")
    private String address;

    @ApiModelProperty(value = "医院logo")
    private String logoData;

    @ApiModelProperty(value = "医院简介")
    private String intro;

    @ApiModelProperty(value = "坐车路线")
    private String route;

    @ApiModelProperty(value = "状态 0：未上线 1：已上线")
    private Integer status;

    @ApiModelProperty(value = "预约规则")
    private BookingRule bookingRule;

    public void setBookingRule(String bookingRule) {
        this.bookingRule = JSONArray.parseObject(bookingRule, BookingRule.class);
    }

}

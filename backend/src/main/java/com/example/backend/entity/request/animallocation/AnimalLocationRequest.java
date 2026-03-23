package com.example.backend.entity.request.animallocation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "AnimalLocationRequest对象", description = "动物地点请求参数")
public class AnimalLocationRequest {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "地点名称")
    private String name;

    @ApiModelProperty(value = "描述")
    private String description;

    @ApiModelProperty(value = "纬度")
    private BigDecimal lat;

    @ApiModelProperty(value = "经度")
    private BigDecimal lon;

    @ApiModelProperty(value = "最佳动物季节")
    private String bestSeason;

    @ApiModelProperty(value = "常见动物")
    private String commonAnimals;

    @ApiModelProperty(value = "设施信息")
    private String facilities;

    @ApiModelProperty(value = "图片")
    private String imgUrl;
}
package com.example.backend.entity.request.travel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 
 * @Description 旅游景点请求参数类
 * @Version 1.0
 */
@Data
@ApiModel(value = "TravelRequest对象", description = "旅游景点请求参数")
public class TravelRequest {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("简介")
    private String intro;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("位置")
    private String location;

    @ApiModelProperty("价格")
    private String price;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("封面图片")
    private String imgUrl;

    @ApiModelProperty("评分")
    private BigDecimal rating;

    @ApiModelProperty("浏览数")
    private Integer viewCount;
}
package com.example.backend.entity.request.travelguide;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author 
 * @Description 旅游攻略请求参数类
 * @Version 1.0
 */
@Data
@ApiModel(value = "TravelGuideRequest对象", description = "旅游攻略请求参数")
public class TravelGuideRequest {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("景点ID")
    private Integer travelId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("封面图片")
    private String imgUrl;

    @ApiModelProperty("评分")
    private BigDecimal rating;
}
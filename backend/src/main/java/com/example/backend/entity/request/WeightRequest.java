package com.example.backend.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 
 * @Description 权重请求参数类
 * @Version 1.0
 */
@Data
@ApiModel(value = "WeightRequest对象", description = "权重请求参数")
public class WeightRequest {

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("景点ID")
    private Integer travelId;

    @ApiModelProperty("行为（collect、like、view）")
    private String behavior;

    @ApiModelProperty("分值（collect=3、like=2，view=1）")
    private Integer score;
}
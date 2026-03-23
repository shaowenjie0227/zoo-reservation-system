package com.example.backend.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 
 * @Description 生态保护新闻请求参数类
 * @Version 1.0
 */
@Data
@ApiModel(value = "ConservationNewsRequest对象", description = "生态保护新闻请求参数")
public class ConservationNewsRequest {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("简介")
    private String intro;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("类型（新闻、政策、法规）")
    private String type;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("图片")
    private String imgUrl;

    @ApiModelProperty("状态（0-禁用，1-正常）")
    private Integer status;
}
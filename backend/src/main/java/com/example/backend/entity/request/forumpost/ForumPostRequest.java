package com.example.backend.entity.request.forumpost;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author 
 * @Description 论坛帖子请求参数
 * @Version 1.0
 */
@Data
@ApiModel(value = "ForumPostRequest对象", description = "论坛帖子请求参数")
public class ForumPostRequest {

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "简介")
    private String intro;

    @ApiModelProperty(value = "内容")
    private String content;

    @ApiModelProperty(value = "关联动物ID")
    private Integer animalId;

    @ApiModelProperty(value = "动物地点ID")
    private Integer locationId;

    @ApiModelProperty("动物时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date observationTime;

    @ApiModelProperty(value = "图片")
    private String imgUrl;

    @ApiModelProperty(value = "浏览数")
    private Integer viewCount;

    @ApiModelProperty(value = "分类ID")
    private Integer categoryId;
}
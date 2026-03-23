package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 论坛帖子表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@TableName("forum_post")
@ApiModel(value = "ForumPost对象", description = "论坛帖子表")
public class ForumPost implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("简介")
    private String intro;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("关联动物ID")
    private Integer animalId;

    @ApiModelProperty("动物地点ID")
    private Integer locationId;

    @ApiModelProperty("动物时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date observationTime;

    @ApiModelProperty("图片")
    private String imgUrl;

    @ApiModelProperty("浏览数")
    private Integer viewCount;

    @ApiModelProperty("分类ID")
    private Integer categoryId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
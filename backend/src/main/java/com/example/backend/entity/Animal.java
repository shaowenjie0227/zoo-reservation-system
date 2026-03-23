package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 动物信息表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@ApiModel(value = "Animal对象", description = "动物信息表")
public class Animal implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("物种ID")
    private Integer typeId;

    @ApiModelProperty("动物名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("图片")
    private String coverUrl;

    @ApiModelProperty("简介")
    private String intro;

    @ApiModelProperty("最佳动物季节")
    private String season;

    @ApiModelProperty("浏览量")
    private Integer viewCount;

    @ApiModelProperty("标签数组")
    private String tags;

    // Gson实例，用于JSON转换
    @JsonIgnore
    @TableField(exist = false)
    private static final Gson gson = new Gson();

    // 获取标签数组
    public List<String> getTagsList() {
        if (tags == null || tags.trim().isEmpty()) {
            return new java.util.ArrayList<>();
        }
        try {
            return gson.fromJson(tags, new TypeToken<List<String>>(){}.getType());
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    // 设置标签数组
    public void setTagsList(List<String> tagsList) {
        if (tagsList == null || tagsList.isEmpty()) {
            this.tags = "[]";
        } else {
            this.tags = gson.toJson(tagsList);
        }
    }

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
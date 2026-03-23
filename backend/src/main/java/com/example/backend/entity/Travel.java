package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 旅游景点表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@ApiModel(value = "Travel对象", description = "旅游景点表")
public class Travel implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
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

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
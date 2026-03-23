package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 举报表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@ApiModel(value = "Report对象", description = "举报表")
public class Report implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户ID（可匿名）")
    private Integer userId;

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("地点")
    private String location;

    @ApiModelProperty("发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date occurrenceTime;

    @ApiModelProperty("图片证据")
    private String imgUrl;

    @ApiModelProperty("联系方式")
    private String contactInfo;

    @ApiModelProperty("状态（0-待处理，1-处理中，2-已处理）")
    private Integer status;

    @ApiModelProperty("处理结果")
    private String processResult;

    @ApiModelProperty("是否匿名")
    private Boolean isAnonymous;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
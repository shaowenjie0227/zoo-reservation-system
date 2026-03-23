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
 * 生态保护新闻表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@ApiModel(value = "ConservationNews对象", description = "生态保护新闻表")
public class ConservationNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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

    @ApiModelProperty("浏览数")
    private Integer viewCount;

    @ApiModelProperty("状态（0-禁用，1-正常）")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
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
 * 门票表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@ApiModel(value = "Ticket对象", description = "门票表")
public class Ticket implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关联景点ID")
    private Integer travelId;

    @ApiModelProperty("门票名称")
    private String name;

    @ApiModelProperty("门票描述")
    private String description;

    @ApiModelProperty("原价")
    private BigDecimal oriPrice;

    @ApiModelProperty("门票价格")
    private BigDecimal price;

    @ApiModelProperty("库存数量")
    private Integer inventory;

    @ApiModelProperty("有效期天数")
    private Integer validityDays;

    @ApiModelProperty("状态（0-下架，1-上架）")
    private Integer status;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
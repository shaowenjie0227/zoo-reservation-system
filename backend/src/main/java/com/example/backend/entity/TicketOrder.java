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
 * 门票订单表
 * </p>
 *
 * @author 
 * 
 */
@Getter
@Setter
@ApiModel(value = "TicketOrder对象", description = "门票订单表")
public class TicketOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户ID")
    private Integer userId;

    @ApiModelProperty("门票ID")
    private Integer ticketId;

    @ApiModelProperty("下单数量")
    private Integer payNum;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("订单总金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("状态(1-已付款,2-已完成,3-已取消)")
    private Integer status;

    @ApiModelProperty("联系人姓名")
    private String contactName;

    @ApiModelProperty("联系人手机号")
    private String contactPhone;

    @ApiModelProperty("身份证号")
    private String cardNo;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
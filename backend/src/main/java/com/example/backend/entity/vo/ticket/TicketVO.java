package com.example.backend.entity.vo.ticket;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.Ticket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 门票VO
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "TicketVO对象", description = "门票视图对象")
public class TicketVO extends Ticket {

    @TableField(exist = false)
    @ApiModelProperty(value = "景点名称")
    private String travelName;

    @TableField(exist = false)
    @ApiModelProperty(value = "景点位置")
    private String travelLocation;

    @TableField(exist = false)
    @ApiModelProperty(value = "景点图片")
    private String travelImgUrl;
}
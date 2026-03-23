package com.example.backend.entity.vo.ticketorder;

import com.baomidou.mybatisplus.annotation.TableField;
import com.example.backend.entity.TicketOrder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author 
 * @Description 门票订单VO类
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TicketOrderVO extends TicketOrder {
    @TableField(exist = false)
    private String userName;
}
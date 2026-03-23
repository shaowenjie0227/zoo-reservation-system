package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.ticketorder.TicketOrderVO;
import com.example.backend.utils.OrderExpirationTask;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.ITicketOrderService;
import com.example.backend.entity.TicketOrder;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 门票订单表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/ticketOrder")
public class TicketOrderController {

    @Resource
    private ITicketOrderService ticketOrderService;

    @Resource
    private OrderExpirationTask orderExpirationTask;

    @PostMapping("/add")
    @ApiOperation(value = "新增门票订单")
    public BaseResponse<Integer> add(@RequestBody TicketOrder request) {
        Integer res = ticketOrderService.add(request);
        return Result.success(res);
    }

    @GetMapping("/cancel")
    @ApiOperation(value = "取消门票订单")
    public BaseResponse<Boolean> cancel(@RequestParam Integer id) {
        Boolean res = ticketOrderService.cancel(id);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增门票订单")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<TicketOrder> request) {
        Boolean res = ticketOrderService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除门票订单")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = ticketOrderService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除门票订单")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = ticketOrderService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑门票订单")
    public BaseResponse<Boolean> edit(@RequestBody TicketOrder request) {
        Boolean res = ticketOrderService.edit(request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询门票订单")
    public BaseResponse<PageResult<List<TicketOrderVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer userId
    ) {
        PageResult<List<TicketOrderVO>> res = ticketOrderService.queryPage(pageNum, pageSize, orderNo, status, userId);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有门票订单")
    public BaseResponse<List<TicketOrder>> all() {
        List<TicketOrder> res = ticketOrderService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询门票订单")
    public BaseResponse<TicketOrder> getById(@RequestParam Integer id) {
        TicketOrder res = ticketOrderService.getByIdDetail(id);
        return Result.success(res);
    }

    @PostMapping("/processExpiredOrders")
    @ApiOperation(value = "手动处理过期订单")
    public BaseResponse<String> processExpiredOrders() {
        orderExpirationTask.manualProcessExpiredOrders();
        return Result.success("过期订单处理完成");
    }
}
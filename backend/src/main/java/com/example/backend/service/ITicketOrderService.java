package com.example.backend.service;

import com.example.backend.entity.TicketOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.ticketorder.TicketOrderVO;

import java.util.List;

/**
 * <p>
 * 门票订单表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface ITicketOrderService extends IService<TicketOrder> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(TicketOrder request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<TicketOrder> request);

        /**
         * 删除
         * @param id
         * @return
         */
        Boolean del(Integer id);
        
        /**
         * 批量删除
         * @param ids 批量ID列表
         * @return 是否成功
         */
        Boolean batchDel(List<Integer> ids);

        /**
         * 编辑
         * @param request 请求参数
         * @return 是否成功
         */
        Boolean edit(TicketOrder request);

        /**
         * 分页查询
         * @param pageNum 页码
         * @param pageSize 页大小
         * @param orderNo 订单号
         * @param status 状态
         * @param userId 用户ID
         * @return 分页结果
         */
        PageResult<List<TicketOrderVO>> queryPage(Integer pageNum, Integer pageSize, String orderNo, Integer status, Integer userId);

        /**
         * 查询所有
         * @return 所有数据
         */
        List<TicketOrder> getAll();

        /**
         * 根据ID查询详情
         * @param id ID
         * @return 详情数据
         */
        TicketOrder getByIdDetail(Integer id);


        /**
         * 取消订单
         * @param id 订单ID
         * @return 是否成功
         */
        Boolean cancel(Integer id);
}
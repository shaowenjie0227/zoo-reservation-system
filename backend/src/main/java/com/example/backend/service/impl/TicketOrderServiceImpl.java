package com.example.backend.service.impl;

import com.example.backend.entity.Ticket;
import com.example.backend.entity.TicketOrder;
import com.example.backend.entity.User;
import com.example.backend.entity.vo.ticketorder.TicketOrderVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.TicketOrderMapper;
import com.example.backend.service.ITicketOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.service.ITicketService;
import com.example.backend.service.IUserService;
import com.example.backend.utils.OrdersUtils;
import org.springframework.stereotype.Service;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.model.PageResult;
import com.example.backend.exception.BusinessException;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import com.example.backend.common.enums.CodeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 门票订单表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class TicketOrderServiceImpl extends ServiceImpl<TicketOrderMapper, TicketOrder> implements ITicketOrderService {
    @Resource
    private TicketOrderMapper ticketOrderMapper;
    @Resource
    private IUserService userService;
    @Resource
    private ITicketService ticketService;

    /**
     * 新增
     *
     * @param saveData
     * @return
     */
    @Override
    public Integer add(TicketOrder saveData) {
        // 设置用户ID
        UserLoginVO currentUserInfo = userService.getCurrentUserInfo();
        if (currentUserInfo != null) {
            saveData.setUserId(currentUserInfo.getId());
        }
        // 设置下单数量
        if (saveData.getPayNum() == null || saveData.getPayNum() <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        // 判断库存是否满意下单数量
        Ticket ticket = ticketService.getById(saveData.getTicketId());
        if (ticket == null || ticket.getInventory() < saveData.getPayNum()) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "库存不足");
        }

        // 设置订单编号
        saveData.setOrderNo(OrdersUtils.generateOrderNo());
        // 设置状态
        saveData.setStatus(1);
        int count = ticketOrderMapper.insert(saveData);
        if (count <= 0) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        // 库存减少
        if (ticket.getInventory() < saveData.getPayNum()) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        ticket.setInventory(ticket.getInventory() - saveData.getPayNum());
        ticketService.updateById(ticket);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<TicketOrder> request) {
        // 获取当前登录用户信息
        UserLoginVO currentUserInfo = userService.getCurrentUserInfo();
        if (currentUserInfo != null) {
            request.forEach(item -> {
                // 设置用户ID
                item.setUserId(currentUserInfo.getId());
            });
        }
        return saveBatch(request);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Override
    public Boolean del(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 批量ID列表
     * @return 是否成功
     */
    @Override
    public Boolean batchDel(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return removeByIds(ids);
    }

    /**
     * 编辑
     *
     * @param request
     * @return
     */
    @Override
    public Boolean edit(TicketOrder request) {
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<TicketOrderVO>> queryPage(Integer pageNum, Integer pageSize, String orderNo, Integer status, Integer userId) {
        QueryWrapper<TicketOrder> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(orderNo)) {
            queryWrapper.like("order_no", orderNo);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<TicketOrder> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<TicketOrderVO> ticketOrderVOS = convertVO(page.getRecords());
        return new PageResult<>(ticketOrderVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<TicketOrder> getAll() {
        return list();
    }


    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public TicketOrder getByIdDetail(Integer id) {
        return getById(id);
    }

    /**
     * 取消订单
     *
     * @param id 订单ID
     * @return 是否成功
     */
    @Override
    public Boolean cancel(Integer id) {
        // 校验订单是否存在
        TicketOrder ticketOrder = getById(id);
        if (ticketOrder == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR, "订单不存在");
        }
        // 取消订单
        ticketOrder.setStatus(3);
        boolean result = updateById(ticketOrder);
        if (!result) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR, "取消订单失败");
        }
        // 反还库存
        Ticket ticket = ticketService.getById(ticketOrder.getTicketId());
        if (ticket != null) {
            ticket.setInventory(ticket.getInventory() + ticketOrder.getPayNum());
            ticketService.updateById(ticket);
        }
        return result;
    }

    // 转换VO
    private List<TicketOrderVO> convertVO(List<TicketOrder> ticketOrders) {
        ArrayList<TicketOrderVO> list = new ArrayList<>();
        for (TicketOrder ticketOrder : ticketOrders) {
            TicketOrderVO ticketOrderVO = new TicketOrderVO();
            BeanUtils.copyProperties(ticketOrder, ticketOrderVO);
            User user = userService.getById(ticketOrder.getUserId());
            if (user != null) {
                ticketOrderVO.setUserName(user.getNickName());
            }
            list.add(ticketOrderVO);
        }
        return list;
    }
}
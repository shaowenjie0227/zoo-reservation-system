package com.example.backend.service.impl;

import com.example.backend.entity.Ticket;
import com.example.backend.entity.Travel;
import com.example.backend.entity.vo.ticket.TicketVO;
import com.example.backend.mapper.TicketMapper;
import com.example.backend.service.ITicketService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.service.ITravelService;
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
 * 门票表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class TicketServiceImpl extends ServiceImpl<TicketMapper, Ticket> implements ITicketService {
    @Resource
    private TicketMapper ticketMapper;
    @Resource
    private ITravelService travelService;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(Ticket request) {
        Ticket saveData = new Ticket();
        BeanUtils.copyProperties(request, saveData);
        ticketMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<Ticket> request) {
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
     * @param request 请求参数
     * @return
     */
    @Override
    public Boolean edit(Ticket request) {
        return updateById(request);
    }

    /**
     * 分页条件查询
     *
     * @return
     */
    @Override
    public PageResult<List<TicketVO>> queryPage(Integer pageNum, Integer pageSize, String name, Integer travelId, Integer status) {
        QueryWrapper<Ticket> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        if (travelId != null) {
            queryWrapper.eq("travel_id", travelId);
        }
        if (status != null) {
            queryWrapper.eq("status", status);
        }
        queryWrapper.orderByDesc("id");
        Page<Ticket> page = new Page<>(pageNum, pageSize);
        Page<Ticket> pageResult = ticketMapper.selectPage(page, queryWrapper);
        List<TicketVO> ticketVOList = convertVO(pageResult.getRecords());
        return new PageResult<>(ticketVOList, pageResult.getTotal(), pageNum, pageSize);
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<Ticket> getAll() {
        QueryWrapper<Ticket> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return ticketMapper.selectList(queryWrapper);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public Ticket getByIdDetail(Integer id) {
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        Ticket ticket = ticketMapper.selectById(id);
        if (ticket == null) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return ticket;
    }

    /**
     * 根据景点ID查询门票列表
     *
     * @param travelId 景点ID
     * @return 门票列表
     */
    @Override
    public List<TicketVO> getByTravelId(Integer travelId) {
        if (travelId == null || travelId <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        QueryWrapper<Ticket> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("travel_id", travelId);
        queryWrapper.eq("status", 1); // 只查询上架的门票
        queryWrapper.orderByDesc("id");
        List<Ticket> tickets = ticketMapper.selectList(queryWrapper);
        return convertVO(tickets);
    }

    /**
     * 转换为VO对象
     *
     * @param tickets
     * @return
     */
    private List<TicketVO> convertVO(List<Ticket> tickets) {
        List<TicketVO> ticketVOList = new ArrayList<>();
        for (Ticket ticket : tickets) {
            TicketVO ticketVO = new TicketVO();
            BeanUtils.copyProperties(ticket, ticketVO);
            
            // 查询关联的景点信息
            if (ticket.getTravelId() != null) {
                Travel travel = travelService.getByIdDetail(ticket.getTravelId());
                if (travel != null) {
                    ticketVO.setTravelName(travel.getName());
                    ticketVO.setTravelLocation(travel.getLocation());
                    ticketVO.setTravelImgUrl(travel.getImgUrl());
                }
            }
            
            ticketVOList.add(ticketVO);
        }
        return ticketVOList;
    }
}
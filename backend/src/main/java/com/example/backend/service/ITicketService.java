package com.example.backend.service;

import com.example.backend.entity.Ticket;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.ticket.TicketVO;

import java.util.List;

/**
 * <p>
 * 门票表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface ITicketService extends IService<Ticket> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(Ticket request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<Ticket> request);

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
         * @return
         */
        Boolean edit(Ticket request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<TicketVO>> queryPage(Integer pageNum, Integer pageSize, String name, Integer travelId, Integer status);

        /**
         * 查询全部
         * @return
         */
        List<Ticket> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        Ticket getByIdDetail(Integer id);

        /**
         * 根据景点ID查询门票列表
         * @param travelId 景点ID
         * @return 门票列表
         */
        List<TicketVO> getByTravelId(Integer travelId);
}
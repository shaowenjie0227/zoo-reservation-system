package com.example.backend.service;

import com.example.backend.entity.ConservationNews;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.conservationnews.ConservationNewsVO;

import java.util.List;

/**
 * <p>
 * 生态保护新闻表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface IConservationNewsService extends IService<ConservationNews> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(ConservationNews request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<ConservationNews> request);

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
        Boolean edit(ConservationNews request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<ConservationNewsVO>> queryPage(Integer pageNum, Integer pageSize, String title, String type, Integer status);

        /**
         * 查询全部
         * @return
         */
        List<ConservationNews> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        ConservationNews getByIdDetail(Integer id);

        /**
         * 增加浏览数
         * @param id
         * @return
         */
        Boolean increaseViewCount(Integer id);
}
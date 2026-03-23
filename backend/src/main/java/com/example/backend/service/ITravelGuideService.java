package com.example.backend.service;

import com.example.backend.entity.TravelGuide;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.travelguide.TravelGuideVO;

import java.util.List;

/**
 * <p>
 * 旅游攻略表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface ITravelGuideService extends IService<TravelGuide> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(TravelGuide request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<TravelGuide> request);

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
        Boolean edit(TravelGuide request);

        /**
     * 分页查询
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param title 标题
     * @param travelId 景点ID
     * @param categoryId 分类ID
     * @return 分页结果
     */
    PageResult<List<TravelGuideVO>> queryPage(Integer pageNum, Integer pageSize, String title, String userId, Integer travelId, Integer categoryId);

        /**
         * 查询所有
         * @return 所有数据
         */
        List<TravelGuide> getAll();

        /**
         * 根据ID查询详情
         * @param id ID
         * @return 详情
         */
        TravelGuide getByIdDetail(Integer id);
}
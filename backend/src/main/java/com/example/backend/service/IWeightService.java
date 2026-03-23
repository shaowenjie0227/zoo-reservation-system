package com.example.backend.service;

import com.example.backend.entity.Weight;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.weight.WeightVO;

import java.util.List;

/**
 * <p>
 * 权重表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface IWeightService extends IService<Weight> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(Weight request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<Weight> request);

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
        Boolean edit(Weight request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<WeightVO>> queryPage(Integer pageNum, Integer pageSize, String behavior, Integer userId, Integer travelId);

        /**
         * 查询全部
         * @return
         */
        List<Weight> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        Weight getByIdDetail(Integer id);

        /**
         * 根据 userId、targetId、behavior 删除权重记录
         * @param userId
         * @param targetId
         * @param collect
         */
        void deleteByUserIdAndTravelIdAndBehavior(Integer userId, Integer targetId, String collect);
}
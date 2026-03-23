package com.example.backend.service;

import com.example.backend.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.collect.CollectVO;

import java.util.List;

/**
 * <p>
 * 用户收藏表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface ICollectService extends IService<Collect> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(Collect request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<Collect> request);

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
         * @param id id
         * @param request 请求参数
         * @return
         */
        Boolean edit(Integer id, Collect request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<CollectVO>> queryPage(Integer pageNum, Integer pageSize, String type);

        /**
         * 查询全部
         * @return
         */
        List<Collect> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        Collect getByIdDetail(Integer id);

        /**
         * 检查用户是否已收藏
         * @param userId 用户ID
         * @param type 收藏类型
         * @param targetId 目标ID
         * @return 是否已收藏
         */
        Boolean isCollected(Integer userId, String type, Integer targetId);

        /**
         * 取消收藏
         * @param userId 用户ID
         * @param type 收藏类型
         * @param targetId 目标ID
         * @return 是否成功
         */
        Boolean cancelCollect(Integer userId, String type, Integer targetId);
}
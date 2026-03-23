package com.example.backend.service;

import com.example.backend.entity.Like;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.like.LikeVO;

import java.util.List;

/**
 * <p>
 * 点赞表 服务类
 * </p>
 *
 * @author 
 * @since 2025-01-27
 */
public interface ILikeService extends IService<Like> {
    /**
     * 新增点赞
     * @param request
     * @return
     */
    Integer add(Like request);

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    Boolean batchAdd(List<Like> request);

    /**
     * 删除点赞
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
     * 编辑点赞
     * @param id id
     * @param request 请求参数
     * @return
     */
    Boolean edit(Integer id, Like request);

    /**
     * 分页条件查询
     * @return
     */
    PageResult<List<LikeVO>> queryPage(Integer pageNum, Integer pageSize, String type, Integer userId, Integer targetId);

    /**
     * 查询全部
     * @return
     */
    List<Like> getAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    Like getByIdDetail(Integer id);

    /**
     * 检查用户是否已点赞
     * @param userId 用户ID
     * @param type 类型
     * @param targetId 目标ID
     * @return 是否已点赞
     */
    Boolean checkUserLike(Integer userId, String type, Integer targetId);

    /**
     * 取消点赞
     * @param userId 用户ID
     * @param type 类型
     * @param targetId 目标ID
     * @return 是否成功
     */
    Boolean cancelLike(Integer userId, String type, Integer targetId);
}
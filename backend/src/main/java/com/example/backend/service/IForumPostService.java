package com.example.backend.service;

import com.example.backend.entity.ForumPost;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.forumpost.ForumPostVO;

import java.util.List;

/**
 * <p>
 * 论坛帖子表 服务类
 * </p>
 *
 * @author
 * 
 */
public interface IForumPostService extends IService<ForumPost> {
    /**
     * 新增
     *
     * @param request
     * @return
     */
    Integer add(ForumPost request);

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    Boolean batchAdd(List<ForumPost> request);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Boolean del(Integer id);

    /**
     * 批量删除
     *
     * @param ids 批量ID列表
     * @return 是否成功
     */
    Boolean batchDel(List<Integer> ids);

    /**
     * 编辑
     *
     * @param id      id
     * @param request 请求参数
     * @return
     */
    Boolean edit(Integer id, ForumPost request);

    /**
     * 分页条件查询
     *
     * @return
     */
    PageResult<List<ForumPostVO>> queryPage(Integer pageNum, Integer pageSize, String title, String userId, Integer categoryId);

    /**
     * 查询全部
     *
     * @return
     */
    List<ForumPost> getAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    ForumPost getByIdDetail(Integer id);

    /**
     * 根据id查询VO
     *
     * @param id
     * @return
     */
    ForumPostVO getByIdDetailVO(Integer id);
}
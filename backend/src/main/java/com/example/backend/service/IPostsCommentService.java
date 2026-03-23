package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.PostsComment;
import com.example.backend.entity.vo.postscomment.PostsCommentVO;

import java.util.List;

/**
 * <p>
 * 帖子评论表 服务类
 * </p>
 *
 * @Author 
 */
public interface IPostsCommentService extends IService<PostsComment> {
    /**
     * 新增
     * @param request
     * @return
     */
    Integer add(PostsComment request);

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    Boolean batchAdd(List<PostsComment> request);

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
    Boolean edit(Integer id, PostsComment request);

    /**
     * 分页条件查询
     * @return
     */
    PageResult<List<PostsCommentVO>> queryPage(Integer pageNum, Integer pageSize, String content, String userId);

    /**
     * 查询全部
     * @return
     */
    List<PostsComment> getAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    PostsComment getByIdDetail(Integer id);
    
    /**
     * 根据帖子ID获取评论列表
     *
     * @param postsId 帖子ID
     * @return 评论列表
     */
    List<PostsCommentVO> getByPostsId(Integer postsId);
}
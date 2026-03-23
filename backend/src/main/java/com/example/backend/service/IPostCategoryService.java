package com.example.backend.service;

import com.example.backend.common.model.PageResult;
import com.example.backend.entity.PostCategory;
import com.example.backend.entity.vo.postcategory.PostCategoryVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 帖子分类表 服务类
 * </p>
 *
 * @author 
 */
public interface IPostCategoryService extends IService<PostCategory> {

    Integer add(PostCategory request);

    Boolean batchAdd(List<PostCategory> request);

    Boolean del(Integer id);

    Boolean batchDel(List<Integer> ids);

    Boolean edit(Integer id, PostCategory request);

    PageResult<List<PostCategoryVO>> queryPage(Integer pageNum, Integer pageSize, String name);

    List<PostCategory> getAll();

    PostCategory getByIdDetail(Integer id);
}

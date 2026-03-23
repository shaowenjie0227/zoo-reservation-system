package com.example.backend.service.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.common.enums.CodeEnum;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.PostCategory;
import com.example.backend.entity.vo.postcategory.PostCategoryVO;
import com.example.backend.exception.BusinessException;
import com.example.backend.mapper.PostCategoryMapper;
import com.example.backend.service.IPostCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 帖子分类表 服务实现类
 * </p>
 *
 * @author 
 */
@Service
public class PostCategoryServiceImpl extends ServiceImpl<PostCategoryMapper, PostCategory> implements IPostCategoryService {

    @Resource
    private PostCategoryMapper postCategoryMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(PostCategory request) {
        PostCategory saveData = new PostCategory();
        BeanUtils.copyProperties(request, saveData);
        postCategoryMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<PostCategory> request) {
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
        return removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean batchDel(List<Integer> ids) {
        return removeByIds(ids);
    }

    /**
     * 编辑
     *
     * @param id
     * @param request
     * @return
     */
    @Override
    public Boolean edit(Integer id, PostCategory request) {
        PostCategory findData = postCategoryMapper.selectById(id);
        if (findData == null) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        BeanUtils.copyProperties(request, findData);
        findData.setId(id);
        return updateById(findData);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageResult<List<PostCategoryVO>> queryPage(Integer pageNum, Integer pageSize, String name) {
        Page<PostCategory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<PostCategory> wrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByDesc("update_time");
        Page<PostCategory> userPage = postCategoryMapper.selectPage(page, wrapper);
        List<PostCategoryVO> voList = convertVO(userPage.getRecords());
        return new PageResult<>(voList, pageNum, userPage.getSize(), userPage.getTotal());
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public List<PostCategory> getAll() {
        return list();
    }

    /**
     * 根据ID查询详情
     *
     * @param id
     * @return
     */
    @Override
    public PostCategory getByIdDetail(Integer id) {
        return getById(id);
    }

    /**
     * 转VO
     *
     * @param postCategories
     * @return
     */
    private List<PostCategoryVO> convertVO(List<PostCategory> postCategories) {
        ArrayList<PostCategoryVO> list = new ArrayList<>();
        if (postCategories.isEmpty()) {
            return list;
        }
        for (PostCategory postCategory : postCategories) {
            PostCategoryVO postCategoryVO = new PostCategoryVO();
            BeanUtils.copyProperties(postCategory, postCategoryVO);
            list.add(postCategoryVO);
        }
        return list;
    }
}

package com.example.backend.service.impl;

import com.example.backend.entity.TravelGuideCategory;
import com.example.backend.entity.vo.travelguidecategory.TravelGuideCategoryVO;
import com.example.backend.mapper.TravelGuideCategoryMapper;
import com.example.backend.service.ITravelGuideCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import cn.hutool.core.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.model.PageResult;
import com.example.backend.exception.BusinessException;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

import com.example.backend.common.enums.CodeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 攻略分类表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class TravelGuideCategoryServiceImpl extends ServiceImpl<TravelGuideCategoryMapper, TravelGuideCategory> implements ITravelGuideCategoryService {
    @Resource
    private TravelGuideCategoryMapper travelGuideCategoryMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(TravelGuideCategory request) {
        TravelGuideCategory saveData = new TravelGuideCategory();
        BeanUtils.copyProperties(request, saveData);
        travelGuideCategoryMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<TravelGuideCategory> request) {
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
    public Boolean edit(Integer id, TravelGuideCategory request) {
        TravelGuideCategory findData = travelGuideCategoryMapper.selectById(id);
        if (findData == null) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        BeanUtils.copyProperties(request, findData);
        return updateById(findData);
    }

    /**
     * 分页条件查询
     *
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public PageResult<List<TravelGuideCategoryVO>> queryPage(Integer pageNum, Integer pageSize, String name) {
        Page<TravelGuideCategory> page = new Page<>(pageNum, pageSize);
        QueryWrapper<TravelGuideCategory> wrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            wrapper.like("name", name);
        }
        // 按更新时间倒序
        wrapper.orderByDesc("update_time");
        
        Page<TravelGuideCategory> userPage = travelGuideCategoryMapper.selectPage(page, wrapper);
        List<TravelGuideCategoryVO> voList = convertVO(userPage.getRecords());
        return new PageResult<>(voList, pageNum, userPage.getSize(), userPage.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<TravelGuideCategory> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public TravelGuideCategory getByIdDetail(Integer id) {
        return getById(id);
    }

    // 转换VO
    private List<TravelGuideCategoryVO> convertVO(List<TravelGuideCategory> travelGuideCategorys) {
        ArrayList<TravelGuideCategoryVO> list = new ArrayList<>();
        for (TravelGuideCategory travelGuideCategory : travelGuideCategorys) {
            TravelGuideCategoryVO travelGuideCategoryVO = new TravelGuideCategoryVO();
            BeanUtils.copyProperties(travelGuideCategory, travelGuideCategoryVO);
            list.add(travelGuideCategoryVO);
        }
        return list;
    }
}

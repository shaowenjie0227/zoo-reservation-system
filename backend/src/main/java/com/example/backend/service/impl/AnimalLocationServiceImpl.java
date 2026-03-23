package com.example.backend.service.impl;

import com.example.backend.entity.AnimalLocation;
import com.example.backend.entity.request.animallocation.AnimalLocationRequest;
import com.example.backend.entity.vo.animallocation.AnimalLocationVO;
import com.example.backend.mapper.AnimalLocationMapper;
import com.example.backend.service.IAnimalLocationService;
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
 * 动物地点表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class AnimalLocationServiceImpl extends ServiceImpl<AnimalLocationMapper, AnimalLocation> implements IAnimalLocationService {
    @Resource
    private AnimalLocationMapper animalLocationMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(AnimalLocation request) {
        AnimalLocation saveData = new AnimalLocation();
        BeanUtils.copyProperties(request, saveData);
        animalLocationMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<AnimalLocation> request) {
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
        if (id == null || id <= 0) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
        return removeById(id);
    }

    /**
     * 批量删除
     *
     * @param ids 批量ID列表
     * @return 是否成功
     */
    @Override
    public Boolean batchDel(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException(CodeEnum.PARAMS_ERROR);
        }
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
    public Boolean edit(Integer id, AnimalLocationRequest request) {
        AnimalLocation animalLocation = new AnimalLocation();
        BeanUtils.copyProperties(request, animalLocation);
        animalLocation.setId(id);
        return updateById(animalLocation);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<AnimalLocationVO>> queryPage(Integer pageNum, Integer pageSize, String name) {
        QueryWrapper<AnimalLocation> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<AnimalLocation> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<AnimalLocationVO> animalLocationVOS = convertVO(page.getRecords());
        return new PageResult<>(animalLocationVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<AnimalLocation> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public AnimalLocation getByIdDetail(Integer id) {
        return getById(id);
    }

    // 转换VO
    private List<AnimalLocationVO> convertVO(List<AnimalLocation> animalLocations) {
        ArrayList<AnimalLocationVO> list = new ArrayList<>();
        for (AnimalLocation animalLocation : animalLocations) {
            AnimalLocationVO animalLocationVO = new AnimalLocationVO();
            BeanUtils.copyProperties(animalLocation, animalLocationVO);
            list.add(animalLocationVO);
        }
        return list;
    }
}
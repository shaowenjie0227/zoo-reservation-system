package com.example.backend.service.impl;

import com.example.backend.entity.AnimalType;
import com.example.backend.entity.vo.animaltype.AnimalTypeVO;
import com.example.backend.mapper.AnimalTypeMapper;
import com.example.backend.service.IAnimalTypeService;
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
 * 动物物种表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class AnimalTypeServiceImpl extends ServiceImpl<AnimalTypeMapper, AnimalType> implements IAnimalTypeService {
    @Resource
    private AnimalTypeMapper animalTypeMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(AnimalType request) {
        AnimalType saveData = new AnimalType();
        BeanUtils.copyProperties(request, saveData);
        animalTypeMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<AnimalType> request) {
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
    public Boolean edit(Integer id, AnimalType request) {
        AnimalType findData = animalTypeMapper.selectById(id);
        if (findData == null) {
            throw new BusinessException(CodeEnum.SYSTEM_ERROR);
        }
        BeanUtils.copyProperties(request, findData);
        return updateById(findData);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<AnimalTypeVO>> queryPage(Integer pageNum, Integer pageSize, String name) {
        QueryWrapper<AnimalType> queryWrapper = new QueryWrapper<>();
        if (CharSequenceUtil.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<AnimalType> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<AnimalTypeVO> animalTypeVOS = convertVO(page.getRecords());
        return new PageResult<>(animalTypeVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<AnimalType> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public AnimalType getByIdDetail(Integer id) {
        return getById(id);
    }

    // 转换VO
    private List<AnimalTypeVO> convertVO(List<AnimalType> animalTypes) {
        ArrayList<AnimalTypeVO> list = new ArrayList<>();
        for (AnimalType animalType : animalTypes) {
            AnimalTypeVO animalTypeVO = new AnimalTypeVO();
            BeanUtils.copyProperties(animalType, animalTypeVO);
            list.add(animalTypeVO);
        }
        return list;
    }
}
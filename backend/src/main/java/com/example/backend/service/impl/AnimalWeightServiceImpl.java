package com.example.backend.service.impl;

import com.example.backend.entity.AnimalWeight;
import com.example.backend.entity.Animal;
import com.example.backend.entity.User;
import com.example.backend.entity.vo.animalweight.AnimalWeightVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.mapper.AnimalWeightMapper;
import com.example.backend.mapper.AnimalMapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.service.IAnimalWeightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.service.IUserService;
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
 * 动物权重表 服务实现类
 * </p>
 *
 * @author
 */
@Service
public class AnimalWeightServiceImpl extends ServiceImpl<AnimalWeightMapper, AnimalWeight> implements IAnimalWeightService {
    @Resource
    private AnimalWeightMapper animalWeightMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AnimalMapper animalMapper;

    /**
     * 新增
     *
     * @param request
     * @return
     */
    @Override
    public Integer add(AnimalWeight request) {
        AnimalWeight saveData = new AnimalWeight();
        BeanUtils.copyProperties(request, saveData);
        animalWeightMapper.insert(saveData);
        return saveData.getId();
    }

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    @Override
    public Boolean batchAdd(List<AnimalWeight> request) {
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
     * @param ids
     * @return
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
     * @param request
     * @return
     */
    @Override
    public Boolean edit(AnimalWeight request) {
        return updateById(request);
    }

    /**
     * 分页查询
     *
     * @return
     */
    @Override
    public PageResult<List<AnimalWeightVO>> queryPage(Integer pageNum, Integer pageSize, String behavior, Integer userId, Integer animalId) {
        QueryWrapper<AnimalWeight> queryWrapper = new QueryWrapper<>();
        if (userId != null) {
            queryWrapper.eq("user_id", userId);
        }
        if (animalId != null) {
            queryWrapper.eq("animal_id", animalId);
        }
        if (CharSequenceUtil.isNotBlank(behavior)) {
            queryWrapper.eq("behavior", behavior);
        }
        // ID 降序
        queryWrapper.orderByDesc("id");
        Page<AnimalWeight> page = page(new Page<>(pageNum, pageSize), queryWrapper);
        // 返回结果
        List<AnimalWeightVO> animalWeightVOS = convertVO(page.getRecords());
        return new PageResult<>(animalWeightVOS, page.getCurrent(), page.getSize(), page.getTotal());
    }

    /**
     * 查询全部
     *
     * @return
     */
    @Override
    public List<AnimalWeight> getAll() {
        return list();
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public AnimalWeight getByIdDetail(Integer id) {
        return getById(id);
    }

    @Override
    public void deleteByUserIdAndAnimalIdAndBehavior(Integer userId, Integer targetId, String collect) {
        QueryWrapper<AnimalWeight> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("animal_id", targetId)
                .eq("behavior", collect);
        remove(queryWrapper);
    }

    /**
     * 转换为VO对象
     *
     * @param animalWeights 实体列表
     * @return VO列表
     */
    private List<AnimalWeightVO> convertVO(List<AnimalWeight> animalWeights) {
        List<AnimalWeightVO> animalWeightVOList = new ArrayList<>();
        for (AnimalWeight animalWeight : animalWeights) {
            AnimalWeightVO animalWeightVO = new AnimalWeightVO();
            BeanUtils.copyProperties(animalWeight, animalWeightVO);

            // 查询用户信息
            if (animalWeight.getUserId() != null) {
                User user = userMapper.selectById(animalWeight.getUserId());
                if (user != null) {
                    animalWeightVO.setUserNickName(user.getNickName());
                }
            }

            // 查询动物信息
            if (animalWeight.getAnimalId() != null) {
                Animal animal = animalMapper.selectById(animalWeight.getAnimalId());
                if (animal != null) {
                    animalWeightVO.setAnimalName(animal.getName());
                    animalWeightVO.setAnimalDescription(animal.getDescription());
                }
            }

            animalWeightVOList.add(animalWeightVO);
        }
        return animalWeightVOList;
    }
}
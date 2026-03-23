package com.example.backend.service;

import com.example.backend.entity.AnimalType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.animaltype.AnimalTypeVO;

import java.util.List;

/**
 * <p>
 * 动物物种表 服务类
 * </p>
 *
 * @author 
 * @since 2025-01-27
 */
public interface IAnimalTypeService extends IService<AnimalType> {
    /**
     * 新增
     * @param request
     * @return
     */
    Integer add(AnimalType request);

    /**
     * 批量新增
     *
     * @param request
     * @return
     */
    Boolean batchAdd(List<AnimalType> request);

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
    Boolean edit(Integer id, AnimalType request);

    /**
     * 分页条件查询
     * @return
     */
    PageResult<List<AnimalTypeVO>> queryPage(Integer pageNum, Integer pageSize, String name);

    /**
     * 查询全部
     * @return
     */
    List<AnimalType> getAll();

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    AnimalType getByIdDetail(Integer id);
}
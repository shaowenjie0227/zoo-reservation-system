package com.example.backend.service;

import com.example.backend.entity.AnimalLocation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.request.animallocation.AnimalLocationRequest;
import com.example.backend.entity.vo.animallocation.AnimalLocationVO;

import java.util.List;

/**
 * <p>
 * 动物地点表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface IAnimalLocationService extends IService<AnimalLocation> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(AnimalLocation request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<AnimalLocation> request);

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
        Boolean edit(Integer id, AnimalLocationRequest request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<AnimalLocationVO>> queryPage(Integer pageNum, Integer pageSize, String name);

        /**
         * 查询全部
         * @return
         */
        List<AnimalLocation> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        AnimalLocation getByIdDetail(Integer id);
}
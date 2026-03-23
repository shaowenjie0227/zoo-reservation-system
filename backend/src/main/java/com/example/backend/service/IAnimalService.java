package com.example.backend.service;

import com.example.backend.entity.Animal;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.request.animal.AnimalRequest;
import com.example.backend.entity.vo.animal.AnimalVO;

import java.util.List;

/**
 * <p>
 * 动物信息表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface IAnimalService extends IService<Animal> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(Animal request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<Animal> request); 

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
        Boolean edit(Integer id, AnimalRequest request);

        /**
         * 分页条件查询
         * @return
         */
        PageResult<List<AnimalVO>> queryPage(Integer pageNum, Integer pageSize, String name, String typeId);

        /**
         * 查询全部
         * @return
         */
        List<Animal> getAll();

        /**
         * 根据id查询
         *
         * @param id
         * @return
         */
        AnimalVO getByIdDetail(Integer id);

        /**
         * 推荐动物
         * @param num 推荐数量
         * @return 推荐动物列表
         */
        List<AnimalVO> recommend(Integer num);

        List<AnimalVO> randomAnimals(Integer num);
}
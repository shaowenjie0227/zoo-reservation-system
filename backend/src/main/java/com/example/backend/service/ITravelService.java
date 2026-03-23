package com.example.backend.service;

import com.example.backend.entity.Travel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.common.model.PageResult;
import com.example.backend.entity.vo.travel.TravelVO;

import java.util.List;

/**
 * <p>
 * 旅游景点表 服务类
 * </p>
 *
 * @author 
 * 
 */
public interface ITravelService extends IService<Travel> {
        /**
        * 新增
        * @param request
        * @return
        */
        Integer add(Travel request);

        /**
         * 批量新增
         *
         * @param request
         * @return
         */
        Boolean batchAdd(List<Travel> request);

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
         * @param request 请求参数
         * @return 是否成功
         */
        Boolean edit(Travel request);

        /**
         * 分页查询
         * @param pageNum 页码
         * @param pageSize 页大小
         * @param name 名称
         * @param location 位置
         * @return 分页结果
         */
        PageResult<List<TravelVO>> queryPage(Integer pageNum, Integer pageSize, String name, String location);

        /**
         * 查询所有
         * @return 所有数据
         */
        List<Travel> getAll();

        /**
         * 根据ID查询详情
         * @param id 主键ID
         * @return 详情信息
         */
        Travel getByIdDetail(Integer id);

        /**
         * 推荐景点
         * @param num
         * @return
         */
        List<TravelVO> recommend(Integer num);

        /**
         * 随机指定数量的景点
         * @param num
         * @return
         */
        List<TravelVO> randomTravels(Integer num);
}
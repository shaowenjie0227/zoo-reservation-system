package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.animallocation.AnimalLocationVO;
import com.example.backend.entity.request.animallocation.AnimalLocationRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.IAnimalLocationService;
import com.example.backend.entity.AnimalLocation;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.BeanUtils;

/**
 * @Description 动物地点表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/animalLocation")
public class AnimalLocationController {

    @Resource
    private IAnimalLocationService animalLocationService;   

    @PostMapping("/add")
    @ApiOperation(value = "新增动物地点")
    public BaseResponse<Integer> add(@RequestBody AnimalLocationRequest request) {
        AnimalLocation animalLocation = new AnimalLocation();
        BeanUtils.copyProperties(request, animalLocation);
        Integer res = animalLocationService.add(animalLocation);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增动物地点")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<AnimalLocationRequest> request) {
        List<AnimalLocation> animalLocations = new java.util.ArrayList<>();
        for (AnimalLocationRequest animalLocationRequest : request) {
            AnimalLocation animalLocation = new AnimalLocation();
            BeanUtils.copyProperties(animalLocationRequest, animalLocation);
            animalLocations.add(animalLocation);
        }
        Boolean res = animalLocationService.batchAdd(animalLocations);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除动物地点")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = animalLocationService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除动物地点")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = animalLocationService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑动物地点")
    private BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody AnimalLocationRequest request) {
        Boolean res = animalLocationService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询动物地点")
    public BaseResponse<PageResult<List<AnimalLocationVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name
    ) {
        PageResult<List<AnimalLocationVO>> res = animalLocationService.queryPage(pageNum, pageSize, name);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有动物地点")
    public BaseResponse<List<AnimalLocation>> all() {
        List<AnimalLocation> res = animalLocationService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询动物地点")
    public BaseResponse<AnimalLocation> getById(@RequestParam Integer id) {
        AnimalLocation res = animalLocationService.getByIdDetail(id);
        return Result.success(res);
    }
}
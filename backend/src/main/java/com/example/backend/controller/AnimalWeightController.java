package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.AnimalWeight;
import com.example.backend.entity.request.AnimalWeightRequest;
import com.example.backend.entity.vo.animalweight.AnimalWeightVO;
import com.example.backend.service.IAnimalWeightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 动物权重表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/animalWeight")
@Api(tags = "动物权重管理")
public class AnimalWeightController {

    @Resource
    private IAnimalWeightService animalWeightService;

    @PostMapping("/add")
    @ApiOperation(value = "新增动物权重")
    public BaseResponse<Integer> add(@RequestBody AnimalWeightRequest request) {
        AnimalWeight animalWeight = new AnimalWeight();
        BeanUtils.copyProperties(request, animalWeight);
        Integer res = animalWeightService.add(animalWeight);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增动物权重")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<AnimalWeightRequest> request) {
        List<AnimalWeight> animalWeights = request.stream().map(item -> {
            AnimalWeight animalWeight = new AnimalWeight();
            BeanUtils.copyProperties(item, animalWeight);
            return animalWeight;
        }).collect(java.util.stream.Collectors.toList());
        Boolean res = animalWeightService.batchAdd(animalWeights);
        return Result.success(res);
    }

    @DeleteMapping("/del/{id}")
    @ApiOperation(value = "删除动物权重")
    public BaseResponse<Boolean> del(@PathVariable Integer id) {
        Boolean res = animalWeightService.del(id);
        return Result.success(res);
    }

    @DeleteMapping("/batchDel")
    @ApiOperation(value = "批量删除动物权重")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = animalWeightService.batchDel(ids);
        return Result.success(res);
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑动物权重")
    public BaseResponse<Boolean> edit(@RequestBody AnimalWeightRequest request) {
        AnimalWeight animalWeight = new AnimalWeight();
        BeanUtils.copyProperties(request, animalWeight);
        Boolean res = animalWeightService.edit(animalWeight);
        return Result.success(res);
    }

    @GetMapping("/queryPage")
    @ApiOperation(value = "分页查询动物权重")
    public BaseResponse<PageResult<List<AnimalWeightVO>>> queryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer animalId,
            @RequestParam(required = false) String behavior) {
        PageResult<List<AnimalWeightVO>> res = animalWeightService.queryPage(pageNum, pageSize, behavior, userId, animalId);
        return Result.success(res);
    }

    @GetMapping("/getAll")
    @ApiOperation(value = "查询所有动物权重")
    public BaseResponse<List<AnimalWeight>> getAll() {
        List<AnimalWeight> res = animalWeightService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询动物权重")
    public BaseResponse<AnimalWeight> getById(@PathVariable Integer id) {
        AnimalWeight res = animalWeightService.getByIdDetail(id);
        return Result.success(res);
    }
}
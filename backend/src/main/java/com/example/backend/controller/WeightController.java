package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.Weight;
import com.example.backend.entity.request.WeightRequest;
import com.example.backend.entity.vo.weight.WeightVO;
import com.example.backend.service.IWeightService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 权重表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/weight")
@Api(tags = "权重管理")
public class WeightController {

    @Resource
    private IWeightService weightService;

    @PostMapping("/add")
    @ApiOperation(value = "新增权重")
    public BaseResponse<Integer> add(@RequestBody WeightRequest request) {
        Weight weight = new Weight();
        BeanUtils.copyProperties(request, weight);
        Integer res = weightService.add(weight);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增权重")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<WeightRequest> request) {
        List<Weight> weights = request.stream().map(item -> {
            Weight weight = new Weight();
            BeanUtils.copyProperties(item, weight);
            return weight;
        }).collect(java.util.stream.Collectors.toList());
        Boolean res = weightService.batchAdd(weights);
        return Result.success(res);
    }

    @DeleteMapping("/del/{id}")
    @ApiOperation(value = "删除权重")
    public BaseResponse<Boolean> del(@PathVariable Integer id) {
        Boolean res = weightService.del(id);
        return Result.success(res);
    }

    @DeleteMapping("/batchDel")
    @ApiOperation(value = "批量删除权重")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = weightService.batchDel(ids);
        return Result.success(res);
    }

    @PutMapping("/edit")
    @ApiOperation(value = "编辑权重")
    public BaseResponse<Boolean> edit(@RequestBody WeightRequest request) {
        Weight weight = new Weight();
        BeanUtils.copyProperties(request, weight);
        Boolean res = weightService.edit(weight);
        return Result.success(res);
    }

    @GetMapping("/queryPage")
    @ApiOperation(value = "分页查询权重")
    public BaseResponse<PageResult<List<WeightVO>>> queryPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) Integer travelId,
            @RequestParam(required = false) String behavior) {
        PageResult<List<WeightVO>> res = weightService.queryPage(pageNum, pageSize, behavior, userId, travelId);
        return Result.success(res);
    }

    @GetMapping("/getAll")
    @ApiOperation(value = "查询所有权重")
    public BaseResponse<List<Weight>> getAll() {
        List<Weight> res = weightService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById/{id}")
    @ApiOperation(value = "根据ID查询权重")
    public BaseResponse<Weight> getById(@PathVariable Integer id) {
        Weight res = weightService.getByIdDetail(id);
        return Result.success(res);
    }
}
package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.TravelGuide;
import com.example.backend.entity.vo.travelguide.TravelGuideVO;
import com.example.backend.service.ITravelGuideService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 旅游攻略表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/travelGuide")
public class TravelGuideController {

    @Resource
    private ITravelGuideService travelGuideService;

    @PostMapping("/add")
    @ApiOperation(value = "新增旅游攻略")
    public BaseResponse<Integer> add(@RequestBody TravelGuide request) {
        Integer res = travelGuideService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增旅游攻略")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<TravelGuide> request) {
        Boolean res = travelGuideService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除旅游攻略")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = travelGuideService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除旅游攻略")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = travelGuideService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑旅游攻略")
    public BaseResponse<Boolean> edit(@RequestBody TravelGuide request) {
        Boolean res = travelGuideService.edit(request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询旅游攻略")
    public BaseResponse<PageResult<List<TravelGuideVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) Integer travelId,
            @RequestParam(required = false) Integer categoryId
    ) {
        PageResult<List<TravelGuideVO>> res = travelGuideService.queryPage(pageNum, pageSize, title, userId, travelId, categoryId);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有旅游攻略")
    public BaseResponse<List<TravelGuide>> all() {
        List<TravelGuide> res = travelGuideService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询旅游攻略")
    public BaseResponse<TravelGuide> getById(@RequestParam Integer id) {
        TravelGuide res = travelGuideService.getByIdDetail(id);
        return Result.success(res);
    }
}
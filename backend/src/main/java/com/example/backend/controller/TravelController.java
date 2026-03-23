package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.vo.travel.TravelVO;
import com.example.backend.entity.vo.user.UserLoginVO;
import com.example.backend.entity.Weight;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.example.backend.service.ITravelService;
import com.example.backend.service.IWeightService;
import com.example.backend.service.IUserService;
import com.example.backend.entity.Travel;

import org.springframework.web.bind.annotation.RestController;

/**
 * @Description 旅游景点表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/travel")
public class TravelController {

    @Resource
    private ITravelService travelService;

    @Resource
    private IWeightService weightService;

    @Resource
    private IUserService userService;

    @PostMapping("/add")
    @ApiOperation(value = "新增旅游景点")
    public BaseResponse<Integer> add(@RequestBody Travel request) {
        Integer res = travelService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    @ApiOperation(value = "批量新增旅游景点")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<Travel> request) {
        Boolean res = travelService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    @ApiOperation(value = "删除旅游景点")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = travelService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除旅游景点")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = travelService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    @ApiOperation(value = "编辑旅游景点")
    public BaseResponse<Boolean> edit(@RequestBody Travel request) {
        Boolean res = travelService.edit(request);
        return Result.success(res);
    }

    @GetMapping("/page")
    @ApiOperation(value = "分页查询旅游景点")
    public BaseResponse<PageResult<List<TravelVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location
    ) {
        PageResult<List<TravelVO>> res = travelService.queryPage(pageNum, pageSize, name, location);
        return Result.success(res);
    }

    @GetMapping("/list")
    @ApiOperation(value = "查询所有旅游景点")
    public BaseResponse<List<Travel>> all() {
        List<Travel> res = travelService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    @ApiOperation(value = "根据ID查询旅游景点")
    public BaseResponse<Travel> getById(@RequestParam Integer id) {
        Travel res = travelService.getByIdDetail(id);

        // 添加浏览权重记录（1分）
        try {
            UserLoginVO currentUser = userService.getCurrentUserInfo();
            if (currentUser != null) {
                // 判断用户是否存在该景点的浏览记录
                Weight existingWeight = weightService.getOne(new LambdaQueryWrapper<Weight>()
                            .eq(Weight::getUserId, currentUser.getId())
                        .eq(Weight::getTravelId, res.getId())
                        .eq(Weight::getBehavior, "view"));

                if (existingWeight == null) {
                    Weight weight = new Weight();
                    weight.setUserId(currentUser.getId());
                    weight.setTravelId(res.getId());
                    weight.setBehavior("view");
                    weight.setScore(1);
                    weightService.add(weight);
                }
            }
        } catch (Exception e) {
            // 权重记录失败不影响主要功能，记录日志即可
            System.err.println("添加浏览权重记录失败: " + e.getMessage());
        }

        return Result.success(res);
    }

    @GetMapping("/recommend")
    @ApiOperation(value = "推荐景点")
    public BaseResponse<List<TravelVO>> recommend(@RequestParam Integer num) {
        List<TravelVO> res = travelService.recommend(num);
        return Result.success(res);
    }
}
package com.example.backend.controller;

import com.example.backend.common.model.PageResult;
import com.example.backend.common.result.BaseResponse;
import com.example.backend.common.result.Result;
import com.example.backend.entity.PostsComment;
import com.example.backend.entity.vo.postscomment.PostsCommentVO;
import com.example.backend.service.IPostsCommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 帖子评论表控制层
 * @Version 1.0
 */
@RestController
@RequestMapping("/postsComment")
public class PostsCommentController {

    @Resource
    private IPostsCommentService postsCommentService;

    @PostMapping("/add")
    public BaseResponse<Integer> add(@RequestBody PostsComment request) {
        Integer res = postsCommentService.add(request);
        return Result.success(res);
    }

    @PostMapping("/batchAdd")
    public BaseResponse<Boolean> batchAdd(@RequestBody List<PostsComment> request) {
        Boolean res = postsCommentService.batchAdd(request);
        return Result.success(res);
    }

    @GetMapping("/del")
    public BaseResponse<Boolean> del(@RequestParam Integer id) {
        Boolean res = postsCommentService.del(id);
        return Result.success(res);
    }

    @PostMapping("/batchDel")
    @ApiOperation(value = "批量删除帖子评论")
    public BaseResponse<Boolean> batchDel(@RequestBody List<Integer> ids) {
        Boolean res = postsCommentService.batchDel(ids);
        return Result.success(res);
    }

    @PostMapping("/edit")
    private BaseResponse<Boolean> edit(@RequestParam Integer id, @RequestBody PostsComment request) {
        Boolean res = postsCommentService.edit(id, request);
        return Result.success(res);
    }

    @GetMapping("/page")
    public BaseResponse<PageResult<List<PostsCommentVO>>> page(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String userId
    ) {
        PageResult<List<PostsCommentVO>> res = postsCommentService.queryPage(pageNum, pageSize, content, userId);
        return Result.success(res);
    }

    @GetMapping("/list")
    public BaseResponse<List<PostsComment>> all() {
        List<PostsComment> res = postsCommentService.getAll();
        return Result.success(res);
    }

    @GetMapping("/getById")
    public BaseResponse<PostsComment> getById(@RequestParam Integer id) {
        PostsComment res = postsCommentService.getByIdDetail(id);
        return Result.success(res);
    }

    @GetMapping("/getByPostsId")
    @ApiOperation(value = "根据帖子ID获取评论列表")
    public BaseResponse<List<PostsCommentVO>> getByPostsId(@RequestParam Integer postsId) {
        List<PostsCommentVO> res = postsCommentService.getByPostsId(postsId);
        return Result.success(res);
    }
    
    @PostMapping("/reply")
    @ApiOperation(value = "回复评论")
    public BaseResponse<Integer> reply(@RequestBody PostsComment request) {
        // 确保设置了父评论ID
        if (request.getPid() == null || request.getPid() <= 0) {
            throw new RuntimeException("回复评论必须指定父评论ID");
        }
        Integer res = postsCommentService.add(request);
        return Result.success(res);
    }
}
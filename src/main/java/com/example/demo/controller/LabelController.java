package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.R;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Label;
import com.example.demo.service.LabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
@RestController
@RequestMapping("/label")
@Api(tags = "标签接口")
public class LabelController {
    @Resource
    private LabelService labelService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public Result<?> findPage(@RequestParam Integer pageNum, @RequestParam Integer pageSize,@RequestParam(defaultValue = "") String search1){
        Page<Label> page = new Page<>(pageNum, pageSize);
        // 根据label名称查询
        LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Label::getTitle,search1);
        labelService.page(page, wrapper);

        return Result.success(page);
    }

    @ApiOperation(value = "查询全部")
    @GetMapping("/findAll")
    public Result<?> findAll(){
        List<Label> list = labelService.list(null);

        return Result.success(list);
    }

    @ApiOperation(value = "根据图书Id查询所属标签")
    @GetMapping("/getLabels/{bookId}")
    public R getLabels(@ApiParam(value = "图书Id") @PathVariable Long bookId){
        List<Label> labels = labelService.getLabelsByBookId(bookId);

        return R.ok().data("labels",labels);

    }

    @ApiOperation(value = "新增标签")
    @PostMapping()
    public R save(@RequestBody Label label){
        LambdaQueryWrapper<Label> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Label::getTitle, label.getTitle());
        Label one = labelService.getOne(wrapper);
        if(one != null){
           return R.error().message("标签已存在");
        }
        labelService.save(label);
        return R.ok();
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Integer id){

        labelService.deleteLabel(id);
        return Result.success();
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteBatch")
    public  R deleteBatch(@RequestBody List<Integer> ids){
        labelService.removeByIds(ids);
        return R.ok();
    }

    @PostMapping("/update")
    public R update(@RequestBody  Label label){
        labelService.updateById(label);
        return R.ok();
    }



}


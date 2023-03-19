package com.example.demo.controller;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.R;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.Label;
import com.example.demo.entity.vo.BookLabelVo;
import com.example.demo.entity.vo.BookQuery;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.BookService;
import com.example.demo.service.LabelService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/book")
@Slf4j
@Api(tags = "书本接口")
public class BookController {
    @Resource
    private BookMapper BookMapper;
    @Resource
    private BookService bookService;
    @Resource
    private LabelService labelService;

    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public R uploadOssFile(MultipartFile file) {
        //获取上传文件  MultipartFile
        System.out.println(file);
        //返回上传到oss的路径
        String url = bookService.uploadFileAvatar(file);
        if (StringUtils.isEmpty(url)) {
            return R.error().message("上传失败！");
        }
        return R.ok().data("url",url);
    }

    @ApiOperation(value = "删除图片")
    @DeleteMapping("/delete")
    public R deleteOssFile(@RequestParam("fileUrl") String fileUrl) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        bookService.deleteFile(fileUrl);
        return R.ok();
    }


    @ApiOperation(value = "新增")
    @PostMapping
    public R save(@RequestBody Book book){
        LambdaQueryWrapper<Book> wr = new LambdaQueryWrapper<>();

        wr.eq(Book::getIsbn, book.getIsbn());
        if (bookService.count(wr) > 0){
            return R.error().message("该物品已存在，请勿重复添加！");
        }
        bookService.save(book);
            return R.ok().data("id", String.valueOf(book.getId()));
    }
    @PutMapping
    @ApiOperation(value = "更新")
    public  Result<?> update(@RequestBody Book Book){
        BookMapper.updateById(Book);
        return Result.success();
    }

    //    批量删除
    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteBatch")
    public  Result<?> deleteBatch(@RequestBody List<Integer> ids){
        BookMapper.deleteBatchIds(ids);
        return Result.success();
    }
    @ApiOperation(value = "根据Id删除")
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        boolean b = bookService.removeById(id);

        return b ? Result.success() : Result.error("20001","删除失败");
    }
    @ApiOperation(value = "分页多条件查询")
    @GetMapping("/findPage")
    public R findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize,
                      @RequestParam(defaultValue = "") String search1,
                      @RequestParam(defaultValue = "") String search2,
                      @RequestParam(defaultValue = "") String search3,
                      @RequestParam(defaultValue = "") String search4){

        return bookService.findPage(new Page<>(pageNum,pageSize),  search1,  search2,  search3,  search4);
    }


}

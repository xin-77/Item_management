package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
public class BookController {
    @Resource
    private BookMapper BookMapper;
    @Resource
    private BookService bookService;
    @Resource
    private LabelService labelService;

    @ApiOperation(value = "上传图片")
    @PostMapping("/upload")
    public Result<?> uploadOssFile(MultipartFile file) {
        //获取上传文件  MultipartFile
        //返回上传到oss的路径
        String url = bookService.uploadFileAvatar(file);
        return Result.success(url);
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
        return R.ok();
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
        BookMapper.deleteById(id);
        return Result.success();
    }
    @ApiOperation(value = "分页多条件查询")
    @GetMapping("/findPage")
    public R findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize,
                      @RequestParam(defaultValue = "") String search1,
                      @RequestParam(defaultValue = "") String search2,
                      @RequestParam(defaultValue = "") String search3,
                      @RequestParam(defaultValue = "") String search4){

        Page<Book> bookPage =  bookService.findPage(new Page<>(pageNum,pageSize),  search1,  search2,  search3,  search4);


//        if(StringUtils.isNotEmpty(search1)){
//            wrappers.like(Book::getIsbn,search1);
//        }
//        if(StringUtils.isNotEmpty(search2)){
//            wrappers.like(Book::getName,search2);
//        }
//        if(StringUtils.isNotEmpty(search3)){
//            wrappers.like(Book::getAuthor,search3);
//        }
//        Page<Book> bookPage = (Page<Book>) BookMapper.selectPage(new Page<>(pageNum,pageSize), wrappers);
        List<Book> records = bookPage.getRecords();
        log.info(records.toString());
        List<BookLabelVo> bookLabelVo = new ArrayList<>();
        for (Book record : records) {
            BookLabelVo vo = new BookLabelVo();
            List<Label> labels = labelService.getLabelsByBookId(Long.valueOf(record.getId()));
            BeanUtils.copyProperties(record, vo);
            vo.setLabels(labels);

            bookLabelVo.add(vo);
        }
        return R.ok().data("bookLabelVos", bookLabelVo).data("Size",  bookPage.getSize())
                .data("total",bookPage.getTotal()).data("current",bookPage.getCurrent());
    }


}

package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.R;
import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.example.demo.entity.LendRecord;
import com.example.demo.entity.User;
import com.example.demo.entity.vo.LendVo;
import com.example.demo.service.BookService;
import com.example.demo.service.LendRecordService;
import com.example.demo.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/LendRecord")
public class LendRecordController {
    @Resource
    private LendRecordService lendRecordService;
    @Resource
    private BookService bookService;
    @Resource
    private UserService userService;


    //删除一条记录
    @ApiOperation("根据lendId删除")
    @DeleteMapping("/{lendId}")
    public  Result<?> deleteRecord(@PathVariable Long lendId){

        lendRecordService.removeById(lendId);
        return Result.success();
    }
    @ApiOperation("根据lendId列表删除多个数据")
    @PostMapping("/deleteRecords")
    public Result<?> deleteRecords(@RequestBody List<Long> LendIds){
        lendRecordService.removeByIds(LendIds);
        return Result.success();
    }

    // 添加借阅信息
    @ApiOperation("添加借阅信息")
    @PostMapping
    @Transactional
    public R save(@RequestBody LendRecord lendRecord){

        Book book = bookService.getById(lendRecord.getBookId());
        if (book.getStock() == 0){
           return R.error().message("已无库存，无法借阅！");
        }
        // 借阅后图书减一
        book.setStock(book.getStock()-1);
        book.setBorrownum(book.getBorrownum()+1);
        bookService.updateById(book);
        lendRecordService.save(lendRecord);

        return R.ok();
    }
    @ApiOperation("分页查询")
    @GetMapping
    public R findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                      @RequestParam(defaultValue = "10") Integer pageSize,
                      @RequestParam(defaultValue = "") String search1,
                      @RequestParam(defaultValue = "") String search2,
                      @RequestParam(defaultValue = "") String search3){
        LambdaQueryWrapper<LendRecord> wrappers = new LambdaQueryWrapper<>();
        if(StrUtil.isNotEmpty(search1)){
            wrappers.like(LendRecord::getIsbn,search1);
        }
        if(StrUtil.isNotEmpty(search2)){
            wrappers.like(LendRecord::getBookname,search2);
        }
        if(StrUtil.isNotEmpty(search3)){
            wrappers.like(LendRecord::getReaderId,search3);
        }
        Page<LendRecord> lendRecordPage = lendRecordService.page(new Page<>(pageNum,pageSize), wrappers);

        List<LendRecord> records = lendRecordPage.getRecords();
        List<LendVo> lendVos = new ArrayList<>();
        LambdaQueryWrapper<User> userWrapper = new LambdaQueryWrapper<>();

        for (LendRecord record : records) {
            LendVo vo = new LendVo();
            User user = userService.getById(record.getReaderId());
            BeanUtils.copyProperties(record, vo);
            vo.setUserName(user.getUsername());

            lendVos.add(vo);
        }




        return  R.ok().data("lendVos", lendVos).data("Size",  lendRecordPage.getSize())
                .data("total",lendRecordPage.getTotal()).data("current",lendRecordPage.getCurrent());
    }

    @ApiOperation("还书")
    @PostMapping("/back")
    @Transactional
    public  R update(@RequestBody(required = false) LendRecord lendRecord){

        Book book = bookService.getById(lendRecord.getBookId());

        // 还书后图书加一
        book.setStock(book.getStock()+1);
        bookService.updateById(book);
        if (lendRecordService.getById(lendRecord.getId()).getStatus() == 1){
            return R.error().message("请勿重复归还");
        }
        lendRecordService.updateById(lendRecord);


        return R.ok();
    }

    @ApiOperation(value = "批量删除")
    @PostMapping("/deleteBatch")
    public  R deleteBatch(@RequestBody List<Integer> ids){
        lendRecordService.removeByIds(ids);
        return R.ok();
    }


}

package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookLabel;
import com.example.demo.entity.Label;
import com.example.demo.entity.vo.BookLabelVo;
import com.example.demo.mapper.LabelMapper;
import com.example.demo.service.BookLabelService;
import com.example.demo.service.LabelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
@Service
@Slf4j
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label> implements LabelService {
    @Resource
    private BookLabelService bookLabelService;


    @Override
    public List<Label> getLabelsByBookId(Long bookId) {
        // 查询bookId对应的标签Id
        LambdaQueryWrapper<BookLabel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookLabel::getBookId, bookId);
        List<BookLabel> bookLabelsId = bookLabelService.list(wrapper);

        List<Label> labels = new ArrayList<>();
        // 查询bookId对应的标签
        for (BookLabel booklabel : bookLabelsId) {
            LambdaQueryWrapper<Label> wrapper2 = new LambdaQueryWrapper<>();
            wrapper2.eq(Label::getId, booklabel.getLabelId());
            labels.add(this.getOne(wrapper2));
        }

        return labels;
    }

    @Override
    @Transactional
    public void deleteLabel(Integer id) {
        this.removeById(id);
        LambdaQueryWrapper<BookLabel> bookLabelWrapper = new LambdaQueryWrapper<>();
        bookLabelWrapper.eq(BookLabel::getLabelId, id);
        bookLabelService.remove(bookLabelWrapper);
    }
}

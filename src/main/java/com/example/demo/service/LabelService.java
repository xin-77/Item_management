package com.example.demo.service;

import com.example.demo.entity.Label;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
public interface LabelService extends IService<Label> {

    List<Label> getLabelsByBookId(Long bookId);

    void deleteLabel(Integer id);
}

package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.vo.BookQuery;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
public interface BookService extends IService<Book> {

    String uploadFileAvatar(MultipartFile file);

    Page<Book> findPage(Page<Book> bookPage, String search1, String search2,String search3,String search4);

    void deleteFile(String objectName);
}

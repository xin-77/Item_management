package com.example.demo.service.impl;

import com.aliyun.oss.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.commom.R;
import com.example.demo.entity.*;
import com.example.demo.entity.vo.BookLabelVo;
import com.example.demo.entity.vo.BookShelfVo;
import com.example.demo.mapper.BookMapper;
import com.example.demo.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.ConstantPropertiesUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xin
 * @since 2023-02-02
 */
@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Resource
    private LabelService labelService;
    @Resource
    private BookLabelService bookLabelService;
    @Resource
    private BookshelfService bookshelfService;
    @Resource
    private BookWithShelfService bookWithShelfService;



    @Override
    public R findPage(Page<Book> bookPage, String search1, String search2, String search3, String search4) {

        LambdaQueryWrapper<Book> wrappers = new LambdaQueryWrapper<>();
        // 判断标签条件是否为空 如果不为空查询标签表
        if (!StringUtils.isEmpty(search4)) {
            LambdaQueryWrapper<Label> labelWrapper = new LambdaQueryWrapper<>();
            labelWrapper.eq(Label::getTitle, search4);
            Label label = labelService.getOne(labelWrapper);
            if (label == null) {
                return R.ok().message("标签不存在！");
            }

            // 查询联系表获取BookId
            LambdaQueryWrapper<BookLabel> bookLabelWrapper = new LambdaQueryWrapper<>();
            bookLabelWrapper.eq(BookLabel::getLabelId, label.getId());
            List<BookLabel> list = bookLabelService.list(bookLabelWrapper);
            List<Long> ids = new ArrayList<>();
            for (BookLabel bookLabel : list) {
                ids.add(bookLabel.getBookId());
            }
            wrappers.in(!ids.isEmpty(), Book::getId, ids);
        }




        wrappers.eq(!StringUtils.isEmpty(search1), Book::getIsbn, search1);
        wrappers.like(!StringUtils.isEmpty(search2), Book::getName, search2);
        wrappers.like(!StringUtils.isEmpty(search3), Book::getAuthor, search3);



        this.page(bookPage, wrappers);

        List<Book> records = bookPage.getRecords();
        log.info(records.toString());
        List<BookLabelVo> bookLabelVo = new ArrayList<>();
        for (Book record : records) {
            BookLabelVo vo = new BookLabelVo();
            // 判断书架Id是否为空 如果不为空查询书架表,添加书架信息
            BookWithShelf one = bookWithShelfService.getOne(new LambdaQueryWrapper<BookWithShelf>().eq(BookWithShelf::getBookId, record.getId()));
            if (one != null) {
                BookShelfVo bookShelfVo = bookshelfService.getBookShelfById(one.getShelfId());
                List<BookShelfVo> bookshelves = new ArrayList<>();
                bookshelves.add(bookShelfVo);
                vo.setBookShelfVo(bookshelves);
            }

            List<Label> labels = labelService.getLabelsByBookId(Long.valueOf(record.getId()));
            BeanUtils.copyProperties(record, vo);
            vo.setLabels(labels);

            bookLabelVo.add(vo);
        }

        return R.ok().data("bookLabelVos", bookLabelVo).data("Size",  bookPage.getSize())
                .data("total",bookPage.getTotal()).data("current",bookPage.getCurrent());
    }

    @Override
    public void deleteFile(String objectName) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        // 工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;



        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String replace = objectName.replace("https://xin-guli-1010.oss-cn-guangzhou.aliyuncs.com/", "");
        System.out.println(replace);
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, replace);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }


    }


    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // 工具类获取值
        String endpoint = ConstantPropertiesUtils.END_POIND;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;

        try {
            // 创建OSS实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //获取文件名称
            String fileName = file.getOriginalFilename();

            //1 在文件名称里面添加随机唯一的值
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            // yuy76t5rew01.jpg
            fileName = uuid;

            //2 把文件按照日期进行分类
            //获取当前日期
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //拼接
            //  2019/11/12/ewtqr313401.jpg
            fileName = datePath + "/" + fileName;

            //调用oss方法实现上传
            //第一个参数  Bucket名称
            //第二个参数  上传到oss文件路径和文件名称   aa/bb/1.jpg
            //第三个参数  上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //把上传之后文件路径返回
            //需要把上传到阿里云oss路径手动拼接出来
            //  https://xin-guli-1010.oss-cn-beijing.aliyuncs.com/01.jpg
            String url = "https://" + bucketName + "." + endpoint + "/" + fileName;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}


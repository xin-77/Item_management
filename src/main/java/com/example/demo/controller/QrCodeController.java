package com.example.demo.controller;


import com.example.demo.commom.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xin
 * @since 2023/3/8 9:54
 */

@RestController
public class QrCodeController {

    // 获取验证码
    @GetMapping
    public R getQrCode() {

        return R.ok();
    }
}

package com.fast.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页控制器，处理根路径请求
 *
 * @author fast
 */
@RestController
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String home() {
        return "你好呀, 恭喜你成功启动了后端";
    }
}

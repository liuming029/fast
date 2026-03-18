package com.fast.system.controller;

import com.fast.system.domain.AjaxResult;
import com.fast.system.config.fastConfig;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 通用请求处理
 *
 * @author fast
 */
@RestController
@RequestMapping("/common")
public class CommonController {
    /**
     * 文件上传请求
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 获取上传路径
            String uploadDir = fastConfig.getProfile() + "/upload";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
            String uniqueFilename = originalFilename + "_" + UUID.randomUUID().toString().replaceAll("-", "") + extension;

            // 保存文件
            Path filePath = Paths.get(uploadDir, uniqueFilename);
            Files.write(filePath, file.getBytes());

            // 返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("url", "/profile/upload/" + uniqueFilename);
            result.put("fileName", "/profile/upload/" + uniqueFilename);
            result.put("newFileName", uniqueFilename);
            result.put("originalFilename", originalFilename);

            return AjaxResult.success(result);
        } catch (IOException e) {
            return AjaxResult.error("文件上传失败: " + e.getMessage());
        }
    }
}
package com.fast.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author fast
 */
@Component
@ConfigurationProperties(prefix = "fast")
public class fastConfig {
    /**
     * 上传路径
     */
    private static String profile;

    public static String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        fastConfig.profile = profile;
    }
}

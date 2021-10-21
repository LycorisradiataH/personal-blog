package com.hua.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/17 17:41
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class OssConfigProperties {

    /**
     * oss域名
     */
    private String url;

    /**
     * bucket名称
     */
    private String bucketName;

    /**
     * 访问密钥id
     */
    private String accessKey;

    /**
     * 访问密钥密码
     */
    private String accessSecretKey;

}

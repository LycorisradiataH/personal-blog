package com.hua.util;

import com.alibaba.fastjson.JSON;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * OSS 工具类
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 15:37
 */
@Component
public class QiniuUtils {

    private static String url;

    private static String accessKey;

    private static String accessSecretKey;

    @Value("${qiniu.url}")
    public void setUrl(String url) {
        QiniuUtils.url = url;
    }

    @Value("${qiniu.accessKey}")
    public void setAccessKey(String accessKey) {
        QiniuUtils.accessKey = accessKey;
    }

    @Value("${qiniu.accessSecretKey}")
    public void setAccessSecretKey(String accessSecretKey) {
        QiniuUtils.accessSecretKey = accessSecretKey;
    }

    /**
     * 上传图片
     * @param file 文件
     * @param targetAddr 目标路径
     * @return OSS地址路径
     */
    public static String upload(MultipartFile file, String targetAddr) {
        // 文件原始名
        String originalFilename = file.getOriginalFilename();
        // 不重复的新文件名
        String fileName = UUID.randomUUID() + "."
                + StringUtils.substringAfterLast(originalFilename, ".");
        // 获取文件存储的相对位置（带文件名）
        String relativeAddr = targetAddr + fileName;
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        // 其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        // 生成上传凭证，然后准备上传
        String bucket = "hua-blog";
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        try {
            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, accessSecretKey);
            String upToken = auth.uploadToken(bucket);
            Response response = uploadManager.put(uploadBytes, relativeAddr, upToken);
            // 解析上传成功的结果
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url + relativeAddr;
    }

}

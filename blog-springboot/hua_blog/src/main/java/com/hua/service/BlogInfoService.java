package com.hua.service;

import com.hua.pojo.vo.BlogBackInfoVO;
import com.hua.pojo.vo.BlogHomeInfoVO;
import com.hua.pojo.vo.WebsiteConfigVO;
import com.hua.pojo.vo.param.BlogInfoParam;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/11 17:27
 */
public interface BlogInfoService {

    /**
     * 获取博客首页数据
     * @return 博客首页数据
     */
    BlogHomeInfoVO getBlogInfo();

    /**
     * 获取关于我数据
     * @return 关于我数据
     */
    String getAboutMe();

    /**
     * 获取后台首页数据
     * @return 后台首页数据
     */
    BlogBackInfoVO getBlogBackInfo();

    /**
     * 获取网站配置
     * @return 网站配置
     */
    WebsiteConfigVO getWebsiteConfig();

    /**
     * 保存或更新网站配置
     * @param websiteConfigVO 网站配置
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

    /**
     * 修改关于我数据
     * @param blogInfoParam 关于我数据
     */
    void updateAboutMe(BlogInfoParam blogInfoParam);

    /**
     * 上传访客信息
     */
    void report();

}

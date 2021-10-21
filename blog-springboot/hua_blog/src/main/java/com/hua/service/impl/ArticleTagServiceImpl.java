package com.hua.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.ArticleTagMapper;
import com.hua.pojo.entity.ArticleTag;
import com.hua.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/13 15:43
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
}

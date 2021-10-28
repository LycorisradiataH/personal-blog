package com.hua.common.consumer;

import com.alibaba.fastjson.JSON;
import com.hua.mapper.ElasticsearchMapper;
import com.hua.pojo.dto.MaxwellDataDTO;
import com.hua.pojo.entity.Article;
import com.hua.pojo.vo.ArticleSearchVO;
import com.hua.util.BeanCopyUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.hua.common.constant.MqPrefixConst.MAXWELL_QUEUE;

/**
 * maxwell 监听数据
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/21 19:50
 */
@Component
@RabbitListener(queues = MAXWELL_QUEUE)
public class MaxWellConsumer {

    @Autowired
    private ElasticsearchMapper elasticsearchMapper;

    @RabbitHandler
    public void process(byte[] data) {
        // 获取监听信息
        MaxwellDataDTO maxwellDataDTO = JSON.parseObject(new String(data), MaxwellDataDTO.class);
        // 获取文章数据
        Article article = JSON.parseObject(JSON.toJSONString(maxwellDataDTO.getData()), Article.class);
        // 判断操作类型
        switch (maxwellDataDTO.getType()) {
            case "insert":
            case "update":
                // 更新es文章
                elasticsearchMapper.save(BeanCopyUtils.copyObject(article, ArticleSearchVO.class));
                break;
            case "delete":
                // 删除文章
                elasticsearchMapper.deleteById(article.getId());
                break;
            default:
                break;
        }
    }

}

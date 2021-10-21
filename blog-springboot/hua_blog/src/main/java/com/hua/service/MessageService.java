package com.hua.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hua.pojo.entity.Message;
import com.hua.pojo.vo.MessageBackVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.AuditParam;
import com.hua.pojo.vo.param.MessageParam;
import com.hua.pojo.vo.param.QueryParam;

import java.util.List;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/14 11:54
 */
public interface MessageService extends IService<Message> {

    /**
     * 查看留言列表
     * @return 留言列表
     */
    Result listMessage();

    /**
     * 添加留言
     * @param messageParam 留言对象
     */
    void saveMessage(MessageParam messageParam);

    /**
     * 查看后台留言列表
     * @param queryParam 条件
     * @return 留言列表
     */
    PageResult<MessageBackVO> listBackMessage(QueryParam queryParam);

    /**
     * 审核留言
     * @param auditParam 审核参数
     */
    void updateMessagesAudit(AuditParam auditParam);

    /**
     * 物理删除留言
     * @param messageIdList 留言id列表
     */
    void deleteMessage(List<Integer> messageIdList);

}

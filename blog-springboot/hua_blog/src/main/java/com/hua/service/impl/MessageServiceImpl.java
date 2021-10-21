package com.hua.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hua.mapper.MessageMapper;
import com.hua.pojo.entity.Message;
import com.hua.pojo.vo.MessageBackVO;
import com.hua.pojo.vo.MessageVO;
import com.hua.pojo.vo.PageResult;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.AuditParam;
import com.hua.pojo.vo.param.MessageParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.BlogInfoService;
import com.hua.service.MessageService;
import com.hua.util.BeanCopyUtils;
import com.hua.util.IpUtils;
import com.hua.util.PageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.hua.common.constant.CommonConst.FALSE;
import static com.hua.common.constant.CommonConst.TRUE;

/**
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/14 11:54
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private BlogInfoService blogInfoService;

    @Resource
    private HttpServletRequest request;

    @Override
    public Result listMessage() {
        // 查询留言列表
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar,
                        Message::getMessageContent, Message::getTime)
                .eq(Message::getIsAudit, TRUE));
        List<MessageVO> messageVOList = BeanCopyUtils.copyList(messageList, MessageVO.class);
        return Result.success(messageVOList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMessage(MessageParam messageParam) {
        // 判断是否需要审核
        Integer isMessageAudit = blogInfoService.getWebsiteConfig().getIsMessageAudit();
        // 获取用户ip
        String ipAddr = IpUtils.getIpAddr(request);
        String ipSource = IpUtils.getIpSource(ipAddr);
        Message message = BeanCopyUtils.copyObject(messageParam, Message.class);
        message.setIpAddr(ipAddr);
        message.setIpSource(ipSource);
        message.setIsAudit(isMessageAudit == TRUE ? FALSE : TRUE);
        messageMapper.insert(message);
    }

    @Override
    public PageResult<MessageBackVO> listBackMessage(QueryParam queryParam) {
        // 分页查询留言列表
        Page<Message> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<Message> messagePage = messageMapper.selectPage(page, new LambdaQueryWrapper<Message>()
                .like(StringUtils.isNotEmpty(queryParam.getKeywords()), Message::getNickname,
                        queryParam.getKeywords())
                .eq(Objects.nonNull(queryParam.getIsAudit()), Message::getIsAudit,
                        queryParam.getIsAudit())
                .orderByDesc(Message::getId));
        // 转换VO
        List<MessageBackVO> messageBackVOList =
                BeanCopyUtils.copyList(messagePage.getRecords(), MessageBackVO.class);
        return new PageResult<>(messageBackVOList, (int) messagePage.getTotal());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMessagesAudit(AuditParam auditParam) {
        // 修改留言审核状态
        List<Message> messageList = auditParam.getIdList().stream().map(id -> Message.builder()
                    .id(id)
                    .isAudit(auditParam.getIsAudit())
                    .build())
                .collect(Collectors.toList());
        this.updateBatchById(messageList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMessage(List<Integer> messageIdList) {
        messageMapper.deleteBatchIds(messageIdList);
    }
}

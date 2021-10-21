package com.hua.controller;

import com.hua.common.annotation.OptLog;
import com.hua.pojo.vo.Result;
import com.hua.pojo.vo.param.AuditParam;
import com.hua.pojo.vo.param.MessageParam;
import com.hua.pojo.vo.param.QueryParam;
import com.hua.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.hua.common.constant.OptTypeConst.REMOVE;
import static com.hua.common.constant.OptTypeConst.UPDATE;

/**
 * 留言模块
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/14 11:55
 */
@Api(tags = "留言模块")
@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 查看留言列表
     * @return 留言列表
     */
    @ApiOperation(value = "查看留言列表")
    @GetMapping("/message")
    public Result listMessage() {
        return Result.success(messageService.listMessage());
    }

    /**
     * 添加留言
     * @param messageParam 留言信息
     * @return {@link Result}
     */
    @ApiOperation(value = "添加留言")
    @PostMapping("/message")
    public Result saveMessage(@Valid @RequestBody MessageParam messageParam) {
        messageService.saveMessage(messageParam);
        return Result.success();
    }

    /**
     * 查看后台留言列表
     * @param queryParam 条件
     * @return 留言列表
     */
    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/admin/message")
    public Result listBackMessage(QueryParam queryParam) {
        return Result.success(messageService.listBackMessage(queryParam));
    }

    /**
     * 审核留言
     *
     * @param auditParam 审核信息
     * @return {@link Result}
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "审核留言")
    @PutMapping("/admin/messages/audit")
    public Result updateMessagesReview(@Valid @RequestBody AuditParam auditParam) {
        messageService.updateMessagesAudit(auditParam);
        return Result.success();
    }

    /**
     * 删除留言
     * @param messageIdList 留言id列表
     * @return {@link Result}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除留言")
    @DeleteMapping("/admin/message")
    public Result deleteMessageByIds(@RequestBody List<Integer> messageIdList) {
        messageService.deleteMessage(messageIdList);
        return Result.success();
    }

}

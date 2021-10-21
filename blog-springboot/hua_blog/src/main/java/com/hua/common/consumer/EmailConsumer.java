package com.hua.common.consumer;

import com.alibaba.fastjson.JSON;
import com.hua.pojo.dto.EmailDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static com.hua.common.constant.MqPrefixConst.EMAIL_QUEUE;

/**
 * 通知邮箱
 * @author Lin Hua
 * @version 1.0
 * @date 2021/10/10 23:44
 */
@Component
@RabbitListener(queues = EMAIL_QUEUE)
public class EmailConsumer {

    /**
     * 邮箱号
     */
    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private JavaMailSender mailSender;

    @RabbitHandler
    public void process(byte[] data) {
        EmailDTO emailDTO = JSON.parseObject(new String(data), EmailDTO.class);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email);
            helper.setTo(emailDTO.getEmail());
            helper.setSubject(emailDTO.getSubject());
            helper.setText(emailDTO.getContent(), true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("HTML格式的邮件发送失败");
            e.printStackTrace();
        }
    }

}

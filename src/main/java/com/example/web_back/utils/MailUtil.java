package com.example.web_back.utils;

import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component("")
public class MailUtil {
    @Value("${spring.mail.username}")
    String from;

    @Resource
    JavaMailSender mailSender;

    public void sendMail(String to, String subject, String info) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(from);
        helper.setSubject(subject);
        helper.setTo(to);
        //不加参数默认是文本，加上 true 之后支持 html 格式文件
        helper.setText(info, true);
        mailSender.send(helper.getMimeMessage());
    }
}

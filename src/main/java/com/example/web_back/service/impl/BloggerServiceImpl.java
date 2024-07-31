package com.example.web_back.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.entity.po.BloggerPo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.BloggerMapper;
import com.example.web_back.utils.MailUtil;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class BloggerServiceImpl {
    private final Logger log = LoggerFactory.getLogger(BloggerServiceImpl.class);

    @Resource
    BloggerMapper bloggerMapper;

    @Resource
    MailUtil mailUtil;

    public void joinWeb(String phone, String sessionId, String ip) throws MessagingException, BusinessException {
        if (bloggerMapper.selectByPhone(phone) == null) {
            throw new BusinessException("账号不存在");
        }

        BloggerPo po = new BloggerPo();
        po.setPhone(phone);
        po.setSessionID(sessionId);
        po.setIp(ip);
        Date date = new Date(System.currentTimeMillis());
        po.setLoginTime(date);

        log.info(JSON.toJSONString(po));
        bloggerMapper.updateJoinInfo(po);
        //mailUtil.sendMail("Yomiger@163.com", ">>> <<<", JSON.toJSONString(po));
    }
}

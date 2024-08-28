package com.example.web_back.service.impl;

import com.alibaba.fastjson2.JSON;
import com.example.web_back.entity.po.BloggerPo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.BloggerMapper;
import com.example.web_back.utils.MailUtil;
import com.example.web_back.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class BloggerServiceImpl {
    private final String blogConstants = "blogger:";

    private final Logger log = LoggerFactory.getLogger(BloggerServiceImpl.class);

    @Resource
    RedisUtils redisUtils;

    @Resource
    BloggerMapper bloggerMapper;

    @Resource
    MailUtil mailUtil;

    public void joinWeb(String phone, String sessionId, String ip) throws MessagingException, BusinessException {
        if (phone.length() != 11) {
            throw new BusinessException("输入错误");
        }

        if (validateSession(phone, sessionId)) {
            throw new BusinessException("请稍后访问");
        }

        if (bloggerMapper.selectByPhone(phone) == null) {
            throw new BusinessException("账号不存在");
        }

        BloggerPo po = new BloggerPo();
        po.setPhone(phone);
        po.setSessionID(sessionId);
        po.setIp(ip);
        ZonedDateTime nowInBeijing = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        Date date = Date.from(nowInBeijing.toInstant());
        po.setLoginTime(date);

        log.info(JSON.toJSONString(po));
        bloggerMapper.updateJoinInfo(po);
        //mailUtil.sendMail("Yomiger@163.com", ">>> <<<", JSON.toJSONString(po));
    }

    private boolean validateSession(String phone, String id) {
        log.info("phone {}", phone);
        // sessionId 变了的情况下 限制一个账号 12小时内只能访问一次
        if (redisUtils.hasKey(blogConstants + phone)) {
            String val = (String) redisUtils.get(blogConstants + phone);
            return !val.equals(id);
        }

        redisUtils.set(blogConstants + phone, id, 60 * 60 * 2);
        return false;
    }
}

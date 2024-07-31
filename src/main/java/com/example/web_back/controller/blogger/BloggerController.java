package com.example.web_back.controller.blogger;

import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.impl.BloggerServiceImpl;
import com.example.web_back.utils.FileUtil;
import com.example.web_back.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin("*")
@RestController
@RequestMapping("/blogger")
public class BloggerController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(BloggerController.class);
    private final String blogConstants = "blogger:";
    private final String HAS_READ = "HAS_READ:";

    @Resource
    FileUtil fileUtil;
    @Resource
    BloggerServiceImpl bloggerService;

    @Resource
    RedisUtils redisUtils;

    /*
        TODO jwt验证
     */
    @RequestMapping("/get_md")
    public ResponseVo getMd(
            HttpServletRequest httpServletRequest,
            HttpSession session,
            String phone,
            String fileName) throws IOException, MessagingException, BusinessException {
        if (validateSession(phone, session.getId())) {
            return resFail("请稍后访问", null);
        }

        if (redisUtils.hasKey(HAS_READ + phone)) {
            return resFail("内容不存在", null);
        }

        String ip = httpServletRequest.getRemoteAddr();
        bloggerService.joinWeb(phone, session.getId(), ip);
        String content = fileUtil.readFile(fileName);
        return resSuccess(fileName + ">获取成功", content);
    }

    @PostMapping("/md-upload")
    public ResponseVo uploadMdFile(
            @RequestParam("fileData") MultipartFile fileData,
            @RequestParam("filename") String filename,
            @RequestParam("currentChunk") String currentChunk) throws IOException {
        fileUtil.saveMdFile(filename, fileData, currentChunk);
        return resSuccess(filename + ">上传成功", null);
    }

    @RequestMapping("/is_read")
    public ResponseVo isRead(String phone) {
        redisUtils.set(HAS_READ + phone, null);
        return resSuccess("", null);
    }

    private boolean validateSession(String phone, String id) {
        log.info("phone {}", phone);
        // sessionId 变了的情况下 限制一个账号 12小时内只能访问一次
        if (redisUtils.hasKey(blogConstants + phone)) {
            String val = (String) redisUtils.get(blogConstants + phone);
            log.info("val {}", val);
            log.info("ID {}", id);

            return !val.equals(id);
        }
        redisUtils.set(blogConstants + phone, id, 60 * 60 * 12);
        return false;
    }
}

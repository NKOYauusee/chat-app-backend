package com.example.web_back.controller.user;

import cn.hutool.core.util.RandomUtil;
import com.example.web_back.annotation.RepeatSubmit;
import com.example.web_back.constants.UserConstants;
import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.po.VerifiedUser;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.UserService;
import com.example.web_back.utils.MailUtil;
import com.example.web_back.utils.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController extends BaseController {
    @Resource
    MailUtil mailUtil;

    @Resource
    UserService userService;

    @Resource
    RedisUtils redisUtils;

//    @Resource
//    ValidateCodeUtil codeUtil;

    @RequestMapping("/login")
    public ResponseVo login(
            HttpSession session,
            @RequestBody @Validated VerifiedUser verifiedUser) throws BusinessException {
        String key = session.getId() + UserConstants.MAIL_CODE;
        if (!validateCode(key, verifiedUser.getCode())) {
            return resFail("验证码错误", null);
        }

        String jwtToken = userService.getToken(verifiedUser.getUser());

        return resSuccess("登录成功", jwtToken);
    }

    @RequestMapping("/update")
    public ResponseVo update(
            HttpSession session,
            @RequestBody @Validated VerifiedUser verifiedUser) {
        // TODO 验证 Token
        String key = session.getId() + UserConstants.VERIFY_CODE;

        if (!validateCode(key, verifiedUser.getCode())) {
            return resFail("验证码错误", null);
        }
        userService.update(verifiedUser.getUser());
        return resSuccess("修改成功", null);
    }

    @RequestMapping("/register")
    public ResponseVo register(
            HttpSession session,
            @RequestBody @Validated VerifiedUser verifiedUser) throws BusinessException {
        String key = session.getId() + UserConstants.VERIFY_CODE;

        if (!validateCode(key, verifiedUser.getCode())) {
            return resFail("验证码错误", null);
        }

        userService.register(verifiedUser.getUser());

        return resSuccess("注册成功", null);
    }

    @RepeatSubmit(interval = 10)
    @RequestMapping("/send-mail")
    public ResponseVo sendMail(
            HttpSession session,
            String to) throws MessagingException {
        String key = session.getId() + ":mailCode";
//        if (redisUtils.getExpire(key) > 30) {
//            return resFail("发送频繁，请稍后再试", null);
//        }
        String code = RandomUtil.randomNumbers(6);
        redisUtils.set(session.getId() + UserConstants.MAIL_CODE, code, 60);

        mailUtil.sendMail(to, "验证码", code);
        return resSuccess("发送成功，注意查收", null);
    }

    @RequestMapping("/captcha")
    public ResponseVo getCaptcha(HttpSession session) {
        //String base64Img = codeUtil.getRandomCodeBase64();
        // TODO 常量表达
        //redisUtils.set(session.getId() + UserConstants.VERIFY_CODE, codeUtil.getCode(), 90);
        return resSuccess("", null);
    }


    boolean validateCode(String key, String code) {
        if (code == null || key == null) {
            return false;
        }
        String ans = (String) redisUtils.get(key);
        redisUtils.del(key);
        return ans.equals(code);
    }
}

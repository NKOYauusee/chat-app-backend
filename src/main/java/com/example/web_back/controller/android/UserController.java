package com.example.web_back.controller.android;


import com.example.web_back.annotation.FucLogger;
import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.dao.UserDao;
import com.example.web_back.entity.po.ChatBean;
import com.example.web_back.entity.po.User;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.ChatService;
import com.example.web_back.service.UserService;
import com.example.web_back.service.impl.MediaServiceImpl;
import com.example.web_back.utils.MyJwtUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("android_UserController")
@RequestMapping("/android/user")
public class UserController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    UserService userService;


    @Resource
    MyJwtUtil myJwtUtil;

    // 手机端登录
    // token 用作与记忆登录免验证码
    @RequestMapping("/login_direct")
    public ResponseVo directLogin(String email, String password, String token) {

        // TODO
        return resSuccess();
    }

    // 手机端登录
    // token 用作与记忆登录免验证码
    @RequestMapping("/updateToken")
    public ResponseVo updateToken(String email, String password, String code) throws BusinessException {
        //if (code.isEmpty() || code == null)
        //    return resFail();

        // TODO 验证 code

        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setPassword(password);
        String token = userService.getToken(user);
        return resSuccess("登录成功", token);
    }


    @RequestMapping("/login")
    public ResponseVo login(String email, String password, String code) throws BusinessException {
        if (email.isEmpty() || password.isEmpty())
            return resFail();

        //if (code.isEmpty() || code == null)
        //    return resFail();

        // TODO 验证 code

        User user = new User();
        user.setEmail(email.toLowerCase());
        user.setPassword(password);

        UserDao userDao = userService.login(user);
        return resSuccess("登录成功", userDao);
    }


    @RequestMapping("/register")
    public ResponseVo register(String username, String email, String password, String code) throws BusinessException {
        //if (code.isEmpty() || code == null)
        //    return resFail();

        // TODO 验证 code

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);

        return resSuccess(null, userService.register(user));
    }

    @RequestMapping("/verifyToken")
    public ResponseVo verifyToken(String email, String token) {
        if (myJwtUtil.validateToken(token) && email.equals(myJwtUtil.getJwtVal(token)))
            return resSuccess("", myJwtUtil.generateToken(email));

        return resFail();
    }


    @Resource
    ChatService chatService;

    // 加载用户离线时没有接受到的最近两天消息
    @FucLogger
    @RequestMapping("/loadOfflineMsg")
    public ResponseVo loadOfflineMsg(String key) {
        List<ChatBean> res = chatService.get2DayOfflineMsg(key);
        return resSuccess("", res);
    }

    @Resource
    MediaServiceImpl mediaService;

    @PostMapping("/registerWithProfile")
    public ResponseVo registerWithProfile(
            @RequestParam("fileData") MultipartFile fileData,
            @RequestParam("email") String email,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("code") String code) throws BusinessException {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        var res = userService.registerWithProfile(user, fileData);
        return resSuccess(null, res);
    }


    @PostMapping("/uploadProfile")
    public ResponseVo uploadProfile(
            @RequestParam("fileData") MultipartFile fileData,
            @RequestParam("userId") String userId,
            @RequestParam("email") String email) throws BusinessException {
        String url = mediaService.uploadProfile(fileData, userId, email);
        return resSuccess(null, url);
    }


    //
    //@FucLogger("searchContact")
    @PostMapping("/search")
    public ResponseVo searchContact(String searchContent, int page) {
        var res = userService.search(searchContent, page);
        return resSuccess(null, res);
    }
}

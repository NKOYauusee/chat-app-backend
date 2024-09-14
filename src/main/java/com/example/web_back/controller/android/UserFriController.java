package com.example.web_back.controller.android;


import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.dao.UserFriendDao;
import com.example.web_back.entity.po.UserApply;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.UserFriService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@CrossOrigin("*")
@RestController("UserFriController")
@RequestMapping("/android/fri")
public class UserFriController extends BaseController {
    @Resource
    UserFriService userFriService;

    // 获取好友列表
    //@FucLogger("getFriendList")
    @RequestMapping("/getFriends")
    public ResponseVo getFriendList(String email) {
        List<UserFriendDao> res = userFriService.getFriendList(email);
        return resSuccess(null, res);
    }

    // 发送好友申请
    //@FucLogger("好友申请")
    @RequestMapping("/addFriend")
    public ResponseVo addFriend(@RequestBody UserApply userApply) throws BusinessException {
        userFriService.insertApplyFriend(userApply);
        return resSuccess();
    }

    // 好友申请处理
    // @FucLogger("好友申请处理")
    @RequestMapping("/setApplyStatus")
    public ResponseVo setApplyStatus(@RequestBody UserApply userApply) {
        userFriService.setApplyStatus(userApply);
        return resSuccess();
    }

    // 获取申请列表处理状态
    //@FucLogger("查看好友申请状态")
    @RequestMapping("/getApplyList")
    public ResponseVo getApplyList(String email) {
        List<UserApply> res = userFriService.selectUserApplyStatus(email);
        return resSuccess("", res);
    }

    // 判断是否有申请请求数据
    @RequestMapping("/getHasApplyList")
    public ResponseVo getHasApplyList(String email) {
        return resSuccess("", userFriService.getHasFriendApplyList(email));
    }

    // 删除好友
    @RequestMapping("/delFriend")
    public ResponseVo deleteFriend(String email, String friend) {
        userFriService.deleteFriend(email, friend);
        return resSuccess();
    }

    // 设置好友状态 如 拉黑
    @RequestMapping("/setFriStatus")
    public ResponseVo setFriStatus(@RequestBody UserFriendDao userFriend) throws BusinessException {
        userFriService.setFriendStatus(userFriend);
        return resSuccess();
    }

    // 批量删除好友
    @RequestMapping("/batchDelFriends")
    public ResponseVo deleteFriends(@RequestBody List<UserFriendDao> friendDaoList) {
        userFriService.batchDeleteFriend(friendDaoList);
        return resSuccess();
    }
}

package com.example.web_back.controller.android;

import com.example.web_back.annotation.FucLogger;
import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.po.GroupBean;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.GroupService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin("*")
@RestController
@RequestMapping("/android/group")
public class GroupController extends BaseController {

    @Resource
    GroupService groupService;

    @FucLogger("GroupController")
    @RequestMapping("/create")
    public ResponseVo createGroup(@RequestBody GroupBean groupInfo) {
        groupService.createGroup(groupInfo);
        return resSuccess();
    }

    // 邀请某人进群
    @FucLogger("GroupController")
    @RequestMapping("/apply")
    public ResponseVo applyGroup(String who, String groupId) throws BusinessException {
        groupService.applyGroup(who, groupId);
        return resSuccess();
    }


}

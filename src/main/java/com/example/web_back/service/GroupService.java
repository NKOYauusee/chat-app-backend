package com.example.web_back.service;

import com.example.web_back.entity.po.GroupBean;
import com.example.web_back.exception.BusinessException;

public interface GroupService {

    void createGroup(GroupBean groupBean);

    void applyGroup(String who,String groupId) throws BusinessException;
}

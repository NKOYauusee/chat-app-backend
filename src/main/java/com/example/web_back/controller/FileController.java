package com.example.web_back.controller;

import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {
    @Resource
    FileService fileService;

    @RequestMapping("/start")
    public ResponseVo checkHasUpload(String key) {
        return resSuccess("", fileService.checkHasUploadSegmentFile(key));
    }

}

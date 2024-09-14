package com.example.web_back.controller.android;


import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.impl.MediaServiceImpl;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@CrossOrigin("*")
@RequestMapping("/android/media")
public class MediaController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Resource
    MediaServiceImpl mediaService;


    @PostMapping("/upload")
    public ResponseVo upload(
            @RequestParam("fileData") MultipartFile fileData,
            @RequestParam("userId") String userId,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileType") String fileType,
            @RequestParam("fileSize") Long fileSize) throws BusinessException {
        String url = mediaService.upload(fileData, userId);
        return resSuccess(null, url);
    }
}

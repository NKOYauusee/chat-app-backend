package com.example.web_back.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.example.web_back.entity.po.User;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.mapper.UserMapper;
import com.example.web_back.utils.FileUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MediaServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Value("${nko.filePath}")
    String savePath;


    @Resource
    FileUtil fileUtil;

    @Resource
    UserMapper userMapper;


    public String upload(MultipartFile fileData,
                         String userId) throws BusinessException {

        if (fileData == null || fileData.isEmpty())
            throw new BusinessException("文件为空");
        //logger.info("待上传文件大小 —> {}", fileData.getSize());
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // 格式化日期
        String formattedDate = today.format(formatter);

        return savaFile(fileData, userId, formattedDate);
    }


    public String uploadProfile(MultipartFile fileData,
                                String userId,
                                String email) throws BusinessException {

        if (fileData == null || fileData.isEmpty())
            throw new BusinessException("文件为空");

        User user = new User();
        String avatar = savaFile(fileData, userId, email);

        user.setEmail(email);
        user.setAvatar(avatar);
        userMapper.updateByEmailSelective(user);
        return avatar;

    }

    private String savaFile(MultipartFile fileData, String userId, String fileName) throws BusinessException {
        try {
            byte[] bytes = fileData.getBytes();
            //要存入本地的地址放到path里面
            String saveP = savePath + userId + "/" + fileName;
            Path path = Paths.get(savePath);
            //如果没有files文件夹，则创建
            if (!Files.isWritable(path)) {
                Files.createDirectories(path);
            }

            String extension = fileUtil.getFileExtension(fileData);  //获取文件后缀
            String name = fileName + "_" + RandomUtil.randomString(12) + extension;

            fileUtil.getFileByBytes(bytes, saveP, name);
            return userId + "/" + fileName + "/" + name;
        } catch (Exception e) {
            throw new BusinessException("上传失败");
        }
    }
}

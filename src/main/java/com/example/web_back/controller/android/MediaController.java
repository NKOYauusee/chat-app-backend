package com.example.web_back.controller.android;


import com.example.web_back.controller.BaseController;
import com.example.web_back.entity.vo.ResponseVo;
import com.example.web_back.exception.BusinessException;
import com.example.web_back.service.impl.MediaServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@CrossOrigin("*")
@RequestMapping("/android/media")
public class MediaController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Value("${nko.filePath}")
    String path;


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

    @PostMapping("/fileUpload")
    public ResponseVo fileUpload(
            @RequestParam("fileData") MultipartFile fileData,
            @RequestParam("fileName") String fileName,
            @RequestParam("chunkIndex") int chunkIndex,
            @RequestParam("userId") int userId) {

//        logger.info("size {}", fileData.getSize());
//        logger.info("fileName {}", fileName);
//        logger.info("chunkIndex {}", chunkIndex);
//        logger.info("userId {}", userId);

        return resSuccess(null, mediaService.saveMultiPartFile(fileData, fileName, String.valueOf(userId), chunkIndex));
    }


    @GetMapping("/download")
    public void download(HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam("filePath") String filePath) throws IOException {

        response.reset();
        File file = new File(path + filePath);
        long fileLength = file.length();
        // 随机读文件
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");
        long range = 0;
        //rangeString: range=bytes=index- (index是读取的起始位置)
        if (rangeString != null) {
            range = Long.parseLong(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
        }
        //获取响应的输出流
        OutputStream outputStream = response.getOutputStream();
        //设置内容类型
        // 动态设置内容类型
        String mimeType = Files.probeContentType(Paths.get(file.getPath()));
        response.setHeader("Content-Type", mimeType);

        response.setHeader("Content-Length", String.valueOf(fileLength));
        //返回码需要为206，代表只处理了部分请求，响应了部分数据(必须设置)
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        // 移动访问指针到指定位置
        randomAccessFile.seek(range);
        // 每次请求只返回1MB的视频流
        byte[] bytes = new byte[1024 * 1024 * 2];
        int len = randomAccessFile.read(bytes);
        //设置此次相应返回的数据长度
        response.setContentLength(len);
        //设置此次相应返回的数据范围
        response.setHeader("Content-Range", "bytes " + range + "-" + (fileLength - 1) + "/" + fileLength);
        // 将这2MB的视频流响应给客户端
        outputStream.write(bytes, 0, len);
        outputStream.close();
        randomAccessFile.close();
    }

    @GetMapping("/getSize")
    public void getSize(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam("filePath") String filePath) throws IOException {

        response.reset();
        File file = new File(path + filePath);
        long fileLength = file.length();
        //设置此次相应返回的数据长度
        response.setContentLength((int) fileLength);
    }
}

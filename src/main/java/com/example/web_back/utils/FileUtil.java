package com.example.web_back.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileUtil {
    private final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    @Value("${nko.directoryName}")
    private String directoryName;

    public String readFile(String fileName) throws IOException {
        String currentDir = getSavePath();

        String filePath = currentDir + "/" + fileName;

        return Files.readString(Paths.get(filePath));
    }

    public void saveMdFile(String fileName, MultipartFile fileData, String currentChunk) throws IOException {
        String savePath = getSavePath();

        InputStream fileInputStream = fileData.getInputStream();

        File tempFile = new File(savePath + "\\" + fileName + ".md");
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        long chunkSize = 1024 * 1024;

        // 计算当前分片的序号，从0开始
        int chunk = Integer.parseInt(currentChunk);

        // 计算当前分片的偏移量
        long offset = chunkSize * chunk;
        // 定位到该分片的位置
        raf.seek(offset);

        // 写入该分片数据
        raf.write(fileData.getBytes());

        // 关闭输入流和随机访问文件对象
        fileInputStream.close();
        raf.close();
    }

    public String getSavePath() throws IOException {
        String currentDir = new File("").getCanonicalPath() + directoryName;
        File directory = new File(currentDir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        logger.info("当前保存路径为：{}", currentDir);
        return currentDir + "\\";
    }
}

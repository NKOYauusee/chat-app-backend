package com.example.web_back.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component("nkoFileUtil")
public class FileUtil {
    private final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    @Value("${nko.directoryName}")
    private String directoryName;

    public String readFile(String fileName) throws IOException {
        String currentDir = getSavePath();

        String filePath = currentDir + fileName;
        logger.info("filePath: {}", filePath);
        return Files.readString(Paths.get(filePath));
    }

    public void saveMdFile(String fileName, MultipartFile fileData, String currentChunk) throws IOException {
        String savePath = getSavePath();

        InputStream fileInputStream = fileData.getInputStream();

        File tempFile = new File(savePath + "/" + fileName + ".md");
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
        return mkdirByName(directoryName) + "/";
    }

    public String getSavePath(String path) throws IOException {
        return mkdirByName(path) + "/";
    }

    public String mkdirByName(String name) throws IOException {
        String currentDir = new File("/app/").getCanonicalPath() + "/" + name;
        File directory = new File(currentDir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        logger.info("文件路径->{}", currentDir);
        return currentDir;
    }

    public String getFileNameWithoutSuffix(String fileName) {
        int suffixIndex = fileName.lastIndexOf('.');
        if (suffixIndex < 0)
            return fileName;
        return fileName.substring(0, suffixIndex);
    }

    public String getFileSuffix(String fileName) {
        int suffixIndex = fileName.lastIndexOf('.');
        if (suffixIndex < 0)
            return "";
        return fileName.substring(suffixIndex + 1);
    }

    public String getSegmentName(String fileName, int segmentIndex) {
        return fileName + "#" + segmentIndex;
    }

    public String createSaveFileName(String key, String fileName) {
        String suffix = getFileSuffix(fileName);
        return key + "." + suffix;
    }

    /**
     * 将文件转换成Byte数组
     */
    public byte[] getBytesByFile(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            logger.error("文件转换成Byte数组失败");
        }
        return null;
    }

    /**
     * 将Byte数组转换成文件
     */
    public void getFileByBytes(byte[] bytes, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {// 判断文件目录是否存在
                var res = dir.mkdirs();
            }

            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            logger.info("filePath {} {}", filePath, new File(filePath).exists());
            logger.info("fileName {}", fileName);
            logger.info("fileExist {}", new File(filePath + "/" + fileName).exists());
            logger.error("Byte数组转换成文件失败", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("bos 关闭错误");
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    logger.error("fos 关闭错误");
                }
            }
        }
    }

    /**
     * 获取文件后缀
     */
    public String getFileExtension(MultipartFile File) {
        String originalFileName = File.getOriginalFilename();
        if (originalFileName != null) {
            return originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        return null;
    }
}

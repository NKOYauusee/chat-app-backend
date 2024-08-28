package com.example.web_back.service.impl;

import com.example.web_back.entity.po.SegmentFile;
import com.example.web_back.mapper.SegmentFileMapper;
import com.example.web_back.service.FileService;
import com.example.web_back.utils.FileUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("FileServiceImpl")
public class FileServiceImpl implements FileService {
    @Resource
    SegmentFileMapper fileMapper;

    @Resource
    FileUtil fileUtil;


    @Override
    public boolean checkHasUploadSegmentFile(String key) {
        SegmentFile segmentFile = fileMapper.getProgressByMd5(key);
        if (segmentFile == null) {
            segmentFile = new SegmentFile();
            segmentFile.setMd5Key(key);
            return false;
        }
        return true;
    }
}

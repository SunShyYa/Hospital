package com.sun.yygh.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.sun.yygh.oss.service.FileService;
import com.sun.yygh.oss.utils.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 10:38
 **/
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile file) {
        String endpoint = ConstantOssPropertiesUtils.EDNPOINT;
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        String bucketName = ConstantOssPropertiesUtils.BUCKET;

        OSS oss = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            InputStream inputStream = file.getInputStream();

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        originalFilename = uuid + originalFilename;
        String timeUrl = new DateTime().toString("yyyy/MM/dd");
        originalFilename = timeUrl + "/" + originalFilename;
        oss.putObject(bucketName,originalFilename, inputStream);
        oss.shutdown();
        String url = "https://" + bucketName + "." + endpoint + "/" + originalFilename;
        return url;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

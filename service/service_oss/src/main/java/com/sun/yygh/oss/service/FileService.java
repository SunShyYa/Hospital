package com.sun.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 10:38
 **/
public interface FileService {
    String upload(MultipartFile file);
}

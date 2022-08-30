package com.sun.yygh.oss.controller;

import com.sun.yygh.common.result.Result;
import com.sun.yygh.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 10:37
 **/
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {
    @Autowired
    private FileService fileService;

    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file) {
        String upload = fileService.upload(file);
        return Result.ok(upload);
    }

}

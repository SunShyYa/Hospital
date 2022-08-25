package com.sun.yygh.cmn.controller;

import com.sun.yygh.cmn.service.DictService;
import com.sun.yygh.common.result.Result;
import com.sun.yygh.model.cmn.Dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-23 11:09
 **/
@RestController
@RequestMapping("/admin/cmn/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id) {
        List<Dict> childData = dictService.findChildData(id);
        return Result.ok(childData);
    }

    @GetMapping("exportData")
    public void exportDict(HttpServletResponse httpServletResponse) {
        dictService.exportDictData(httpServletResponse);

    }

    @PostMapping("importData")
    public Result importDict(MultipartFile file) {
        dictService.importDictData(file);
        return Result.ok();
    }

    @GetMapping("getName/{dictCode}/{value}")
    public String getName(@PathVariable String dictCode,
                          @PathVariable String value){
        String name = dictService.getDictName(dictCode, value);
        return name;
    }

    @GetMapping("getName/{value}")
    public String getName(@PathVariable String value) {
        String name = dictService.getDictName("", value);
        return name;
    }

    @GetMapping("findByDictCode/{dictCode}")
    public Result findByDictCode(@PathVariable String dictCode) {
        List<Dict> list = dictService.findDictCode(dictCode);
        return Result.ok(list);
    }
}

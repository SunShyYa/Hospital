package com.sun.yygh.cmn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.yygh.model.cmn.Dict;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 13:38
 **/
public interface DictService extends IService<Dict> {
    List<Dict> findChildData(Long id);

    void exportDictData(HttpServletResponse httpServletResponse);

    void importDictData(MultipartFile file);
}

package com.sun.yygh.cmn.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.yygh.cmn.listener.DictListener;
import com.sun.yygh.cmn.mapper.DictMapper;
import com.sun.yygh.cmn.service.DictService;
import com.sun.yygh.model.cmn.Dict;
import com.sun.yygh.vo.cmn.DictEeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-22 13:39
 **/
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {


    @Override
    @Cacheable(value = "dict",keyGenerator="keyGenerator")
    public List<Dict> findChildData(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        List<Dict> dicts = baseMapper.selectList(wrapper);
        for (Dict dict : dicts) {
            Long dictId = dict.getId();
            boolean children = this.isChildren(dictId);
            dict.setHasChildren(children);
        }
        return dicts;
    }

    @Override
    public void exportDictData(HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setCharacterEncoding("utf-8");
// 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = null;
        try {
            fileName = URLEncoder.encode("数据字典", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpServletResponse.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");
        List<Dict> dicts = baseMapper.selectList(null);
        List<DictEeVo> list = new ArrayList<>();
        for (Dict dict : dicts) {
            DictEeVo dictEeVo = new DictEeVo();
            BeanUtils.copyProperties(dict, dictEeVo);
            list.add(dictEeVo);
        }
        try {
            EasyExcel.write(httpServletResponse.getOutputStream(), DictEeVo.class)
                    .sheet("数据字典").doWrite(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Dict> findDictCode(String dictCode) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(wrapper);
        List<Dict> childData = this.findChildData(dict.getId());
        return childData;

    }

    @Override
    public String getDictName(String dictCode, String value) {
        if (StringUtils.isEmpty(dictCode)) {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("value", value);
            Dict dict = baseMapper.selectOne(wrapper);
            return dict.getName();
        } else {
            QueryWrapper<Dict> wrapper = new QueryWrapper<>();
            wrapper.eq("dict_code", dictCode);
            Dict dict = baseMapper.selectOne(wrapper);
            Long id = dict.getId();
            Dict dict1 = baseMapper.selectOne(new QueryWrapper<Dict>()
                    .eq("parent_id", id)
                    .eq("value", value));
            return dict1.getName();
        }
    }

    @Override
    @CacheEvict(value = "dict", allEntries = true)
    public void importDictData(MultipartFile multipartFile) {
        try {
            EasyExcel.read(multipartFile.getInputStream(),DictEeVo.class, new DictListener(baseMapper))
            .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isChildren(Long id) {
        QueryWrapper<Dict> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", id);
        Integer integer = baseMapper.selectCount(wrapper);
        return integer > 0;
    }
}

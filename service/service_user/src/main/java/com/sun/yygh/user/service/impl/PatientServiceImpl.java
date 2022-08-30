package com.sun.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.yygh.cmn.client.DictFeignClient;
import com.sun.yygh.enums.DictEnum;
import com.sun.yygh.model.user.Patient;
import com.sun.yygh.user.mapper.PatientMapper;
import com.sun.yygh.user.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 13:02
 **/
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper, Patient> implements PatientService {

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public Patient getPatientId(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }

    @Override
    public List<Patient> findAllUserId(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        List<Patient> patients = baseMapper.selectList(wrapper);
        patients.stream().forEach(item -> {
            this.packPatient(item);
        });

        return patients;
    }
    private Patient packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体指
        String certificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());//联系人证件
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;
    }

}

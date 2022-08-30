package com.sun.yygh.user.client;

import com.sun.yygh.model.user.Patient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-29 20:16
 **/
@FeignClient("service-user")
@Service
public interface PatientFeignClient {
    @GetMapping("/api/user/patient/inner/get/{id}")
    Patient getPatient(@PathVariable("id") Long id);
}

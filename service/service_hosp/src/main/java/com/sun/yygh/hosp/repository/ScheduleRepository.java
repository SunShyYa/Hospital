package com.sun.yygh.hosp.repository;

import com.sun.yygh.model.hosp.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @program: yygh_parent
 * @description:
 * @author: SunShy
 * @create: 2022-08-24 12:08
 **/
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    Schedule getScheduleByHoscodeAndHosScheduleId(String hoscode, String hosScheduleId);
}

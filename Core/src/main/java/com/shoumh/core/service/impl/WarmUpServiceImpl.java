package com.shoumh.core.service.impl;

import com.google.gson.Gson;
import com.shoumh.core.common.SystemConstant;
import com.shoumh.core.dao.CourseDao;
import com.shoumh.core.dao.RedisUtil;
import com.shoumh.core.dao.StudentDao;
import com.shoumh.core.pojo.Course;
import com.shoumh.core.pojo.CourseCapacity;
import com.shoumh.core.service.CourseService;
import com.shoumh.core.service.StudentService;
import com.shoumh.core.service.WarmUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WarmUpServiceImpl implements WarmUpService {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private Gson gson;
    @Autowired
    private RedisUtil redisUtil;

    private final String redisWarmUpKey = "warmed_up";

    @Override
    public void warmUp() {
        if (redisUtil.hasKey(redisWarmUpKey)) {
            return;
        }

        List<Course> courses;
        int i = 0;
        do {
            courses = courseDao.selectByYearAndSemester(SystemConstant.YEAR, SystemConstant.SEMESTER, i*50, i*50+50);
            String pageKey = redisUtil.concatKeys("pub_course_key", SystemConstant.YEAR.toString(),
                    SystemConstant.SEMESTER.toString(), "page", String.valueOf(i*50), String.valueOf(i*50+50));
            redisUtil.setWithExpiration(pageKey, gson.toJson(courses), 2, TimeUnit.DAYS);
            for (Course course: courses) {
                String courseId = course.getCourseId();
                String countKey = redisUtil.concatKeys("course", "avail", courseId);
                String capacityKey = redisUtil.concatKeys("course", "capacity", courseId);

                CourseCapacity capacity = courseDao.getCapacity(course);
                if (capacity == null) {
                    log.warn("error get capacity with course {}, capacity {}", course, capacity);
                } else {
                    redisUtil.setWithExpiration(capacityKey, String.valueOf(capacity.getCapacity()), 2, TimeUnit.DAYS);
                    redisUtil.setWithExpiration(countKey, String.valueOf(capacity.getCapacity() - capacity.getSelection()), 2, TimeUnit.DAYS);
                }
            }
            i++;
        } while (! courses.isEmpty());

        redisUtil.setWithExpiration(redisWarmUpKey, "OK", 2, TimeUnit.DAYS);
    }
}

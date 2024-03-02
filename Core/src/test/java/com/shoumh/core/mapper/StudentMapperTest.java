package com.shoumh.core.mapper;

import com.shoumh.core.pojo.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StudentMapperTest {
    @Autowired
    private StudentMapper studentMapper;

    @Test
    public void test() {
        Student record;
        String id = "123123";
        log.debug("student mapper test start ----");

        log.debug("- select by id");
        record = studentMapper.selectById("123123");

        log.debug("- delete by id");
        studentMapper.deleteById("123123");

        log.debug("- insert");
        record = new Student("123123", "new stu", "male", null, null, null, null);
        studentMapper.insert(record);

        log.debug("- insert selective");
        record.setStuId("123124");
        studentMapper.insertSelective(record);

        log.debug("- update by id");
        studentMapper.updateById(record);

        log.debug("- update by id selective");
        studentMapper.updateByIdSelective(record);

        log.debug("student mapper test end. ----");
    }

}
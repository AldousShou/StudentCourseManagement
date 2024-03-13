package com.shoumh.core.mapper;

import com.shoumh.core.pojo.LogSheet;
import org.apache.ibatis.annotations.Mapper;

/**
* @author siriusshou
* @description 针对表【log(全局 log)】的数据库操作Mapper
* @createDate 2024-03-13 11:08:35
* @Entity com.shoumh.core.pojo.LogMapper
*/
@Mapper
public interface LogMapper {
    public void log(LogSheet sheet);
}

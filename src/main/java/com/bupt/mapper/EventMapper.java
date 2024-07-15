package com.bupt.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ Author: Jiaxuan Chen
 * @ Version: v1.0
 */
@Mapper
public interface EventMapper {
    @Insert("insert into event (metric, describe, timestamp, timestamp_unix) values (#{metric}, #{describe}, #{timestamp}, #{timestamp_unix})")
    void insert(@Param("metric") String metric, @Param("describe") String describe, @Param("timestamp") String timestamp, @Param("timestamp_unix") Long timestamp_unix);
}

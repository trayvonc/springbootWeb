package com.fan.mapper;

import com.fan.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Mapper
@Repository
public interface UserMapper {
    public User queryUserByName(String name);
    public void updateTimeByName(@Param("day") Date day,@Param("name") String name);
}

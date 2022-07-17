package com.fan.service;

import com.fan.mapper.UserMapper;
import com.fan.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService{


    //业务简单时可以省略，加到control中操作
    @Autowired
    UserMapper userMapper;

    @Override
    public User queryUserByName(String name) {
        return userMapper.queryUserByName(name);
    }
    @Override
    public void updateTimeByName(Date day,String name) {
        userMapper.updateTimeByName(day,name);
    }
}

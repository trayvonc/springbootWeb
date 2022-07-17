package com.fan.service;

import com.fan.pojo.User;

import java.util.Date;

public interface UserService {
    public User queryUserByName(String name);
    public void updateTimeByName(Date day,String name);
}

//package com.fan.utils;
//
//import org.redisson.api.RedissonClient;
//import org.redisson.client.codec.StringCodec;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//
//@Component
//public class Test{
//
//    @Autowired
//    private RedissonClient client;
//
//    @PostConstruct
//    public void listener(){
//        RedisMessageUnrepeatable redisMq = new RedisMessageUnrepeatable(client, "test", StringCodec.INSTANCE);
//        redisMq.addListener(String.class, (t, m) -> System.out.printf("topic: %s, message: %s \n", t, m));
//    }
//}

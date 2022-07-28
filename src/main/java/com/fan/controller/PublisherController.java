//package com.fan.controller;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * Created by GK_LZS  ON 2019/4/5
// */
//@RestController
//@RequestMapping
//public class PublisherController {
//    private static final Logger log = LoggerFactory.getLogger(PublisherController.class);
//    @Autowired
//    private RedisTemplate redisTemplate;
//    @GetMapping(value = "pub/{id}")
//    public String pubMsg(@PathVariable String id){
//        redisTemplate.convertAndSend("phone","223333");
//        redisTemplate.convertAndSend("phoneTest2","34555665");
//        log.info("Publisher sendes Topic... ");
//        return "success";
//    }
//}

//package com.fan.myredis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
///**
//
//
// @Author: qlq
//
//
// @Description
//
//
// @Date: 21:29 2018/10/9
// */
//public class MessageProducer extends Thread {
//    @Autowired
//    RedisTemplate redisTemplate;
//    public static final String CHANNEL_KEY = "channel:1";
//    private volatile int count;
//    public void putMessage(String message) {
////        Jedis jedis = JedisPoolUtils.getJedis();
//        redisTemplate.convertAndSend(CHANNEL_KEY,message);
////        Long publish = jedis.publish(CHANNEL_KEY, message);//返回订阅者数量
////        System.out.println(Thread.currentThread().getName() + " put message,count=" + count+",subscriberNum="+publish);
////        count++;
//    }
//    @Override
//    public synchronized void run() {
//        for (int i = 0; i < 5; i++) {
//            putMessage("message" + count);
//        }
//    }
//    public static void main(String[] args) {
//        MessageProducer messageProducer = new MessageProducer();
//        Thread t1 = new Thread(messageProducer, "thread1");
//        Thread t2 = new Thread(messageProducer, "thread2");
//        Thread t3 = new Thread(messageProducer, "thread3");
//        Thread t4 = new Thread(messageProducer, "thread4");
//        Thread t5 = new Thread(messageProducer, "thread5");
//        t1.start();
//        t2.start();
//        t3.start();
//        t4.start();
//        t5.start();
//    }
//}

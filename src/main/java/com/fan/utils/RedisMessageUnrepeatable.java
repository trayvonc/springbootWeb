//package com.fan.utils;
//
//import lombok.NonNull;
//import org.redisson.api.RAtomicLong;
//import org.redisson.api.RReadWriteLock;
//import org.redisson.api.RTopic;
//import org.redisson.api.RedissonClient;
//import org.redisson.api.listener.MessageListener;
//import org.redisson.client.codec.Codec;
//import org.redisson.client.codec.StringCodec;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.concurrent.atomic.AtomicLong;
//@Component
//public class RedisMessageUnrepeatable {
//
//    private static final String INDEX_SUFFIX = "_unrepeatable";
//
//    private static final String RW_SUFFIX = "_rwLock";
//
//    private final RedissonClient client;
//
//    // 主题名
//    private final String topic;
//
//    // 序列化方式
//    private final Codec codec;
//
//    private final RTopic rTopic;
//
//    private final RAtomicLong topicIndex;
//
//    private final RReadWriteLock rwLock;
//
//    public RedisMessageUnrepeatable(@NonNull RedissonClient client, @NonNull String topic, Codec codec) {
//        this.client = client;
//        this.topic = topic;
//        this.codec = codec;
//        this.rTopic = client.getTopic(topic, codec);
//        this.topicIndex = client.getAtomicLong(topic + INDEX_SUFFIX);
//        rwLock = client.getReadWriteLock(topic + RW_SUFFIX);
//    }
//
//    public <M> void addListener(Class<M> type, MessageListener<M> listener) {
//        // 获取写锁, 保证在注册新的消费者的时候 topicIndex 不会被修改.
//        // 若在注册的时候 topicIndex 在改变, 可能导致与 currentIndex 不一致,
//        // 从而导致 currentIndex 永远比 topicIndex 小, 造成该消费者永远消费不到消息.
//        rwLock.writeLock().lock();
//        try {
//            final AtomicLong currentIndex = new AtomicLong(topicIndex.get());
//            rTopic.addListener(type, (charSequence, s) -> {
//                // 获取读锁, 读锁是共享锁.
//                rwLock.readLock().lock();
//                try {
//                    if (!topicIndex.compareAndSet(currentIndex.get(),
//                            currentIndex.incrementAndGet())) {
//                        return;
//                    }
//                    listener.onMessage(charSequence, s);
//                }finally {
//                    rwLock.readLock().unlock();
//                }
//            });
//        }finally {
//            rwLock.writeLock().unlock();
//        }
//    }
//
//    public RedissonClient getClient() {
//        return client;
//    }
//
//    public String getTopic() {
//        return topic;
//    }
//
//    public Codec getCodec() {
//        return codec;
//    }
//}
//
//
//

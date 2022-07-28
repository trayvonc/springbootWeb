package com.fan.config;

//import net.cc.support.MessageReceiverSupport;
import com.fan.handler.HttpHandler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author yueyang
 * @since 2021/02/05 14:47
 **/
@Configuration
//@AutoConfigureAfter({MessageReceiverSupport.class})
public class RedisPubSubConfig {


    /**
     * Redis消息监听器容器
     * 这个容器加载了RedisConnectionFactory和消息监听器
     * 可以添加多个监听不同话题的redis监听器，只需要把消息监听器和相应的消息订阅处理器绑定，该消息监听器
     * 通过反射技术调用消息订阅处理器的相关方法进行一些业务处理
     * @param connectionFactory 链接工厂
     * @param adapter 适配器
     * @return redis消息监听容器
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //可以添加多个 messageListener
        container.addMessageListener(adapter, new PatternTopic(HttpHandler.AUDIT_LOG_CHANNEL));
        return container;
    }


    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * 将MessageReceiver注册为一个消息监听器，可以自定义消息接收的方法（handleMessage）
     * 如果不指定消息接收的方法，消息监听器会默认的寻找MessageReceiver中的onMessage这个方法作为消息接收的方法
     * @param messageReceiver 消息接受
     * @return 适配器
     */
    @Bean
    public MessageListenerAdapter adapter(HttpHandler messageReceiver) {
        return new MessageListenerAdapter(messageReceiver, "onMessage");
    }

}

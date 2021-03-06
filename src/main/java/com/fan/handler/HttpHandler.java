package com.fan.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fan.service.UserService;
import com.fan.service.UserServiceImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
//import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
//import redis.clients.jedis.Jedis;
import com.fan.utils.RequestUtils;
import com.fan.utils.UUIDUtils;
import com.fan.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.AbstractDocument;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;

/**
 * http拦截器，用于处理客户端的行为
 *
 * @author Gjing
 **/
//@Slf4j
@Component
@ChannelHandler.Sharable
public class HttpHandler extends SimpleChannelInboundHandler<HttpObject>implements MessageListener {

//    private String STORE_PATH = "/FaceRecognition/unknown_pictures/";
    public  final static String AUDIT_LOG_CHANNEL = "channel:1";
    private final static String uploadFaceIdQueue = "upload_faceid_queue_";

    private final static String faceRecogintionQueue = "face_recogintion_queue_";

    private final static int uploadProcessCount = 5;

    private final static int recognitionProcessCount = 5;
//    private AbstractDocument.Content content;
    @Autowired
    UserService userService;
    @Autowired
    RedisTemplate redisTemplate;
//    @Autowired
//    HttpServletRequest request; //通过注解获取一个request
    ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    //响应
    private void response(ChannelHandlerContext ctx, Result result) {

        // 1.设置响应
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(JSONObject.toJSONString(result), CharsetUtil.UTF_8));

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
        ////设置跨域
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.headers().set(ACCESS_CONTROL_ALLOW_HEADERS, "Origin, X-Requested-With, Content-Type, Accept");
        response.headers().set(ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT,DELETE");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        System.out.println("服务器回复：" + JSONObject.toJSONString(result));
//        Subject subject = SecurityUtils.getSubject();
//        Integer id= (Integer) subject.getSession().getAttribute("userId");
//        userService.updateTimeById(new Date(),id);
//        System.out.println("handler"+request.getSession().getAttribute("userId"));
//        request.getSession().setAttribute("hasSign","已签到");
//        userService=new UserServiceImpl();
        System.out.println();
        userService.updateTimeByName(new Date(),result.getMessage());

        // 2.发送
        // 注意必须在使用完之后，close channel
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        setCtx(ctx);
        Channel channel = ctx.channel();
        System.out.println(channel.toString());
        String mychannel=channel.toString();
        System.out.println("收到http连接");
        //获取请求体
        FullHttpRequest request = (FullHttpRequest) msg;

        //获取路径
        URI uri = new URI(request.uri());
        System.out.println(uri);
        Result result = new Result(100, "123");
        // 对图标的请求不做任何处理
        if ("/favicon.ico".equals(uri.getPath())) {
            return;
        }
        if (!"/checkFace".equals(uri.getPath()) && !"/uploadFace".equals(uri.getPath())) {
            // 过滤非人脸识别和上传的api
            result.setStatus(404);
            result.setMessage("Not Found！");
            ctx.writeAndFlush(result);
            response(ctx, result);
            return;
        }
        Map<String, Object> stringStringMap = RequestUtils.parseParameter(request);
        Long startTime = (new Date()).getTime();
        Integer cols = Integer.parseInt((String) stringStringMap.get("cols"));
        Integer rows = Integer.parseInt((String) stringStringMap.get("rows"));
        String personName = (String) stringStringMap.get("personName");
        System.out.println("Face Id：" + personName);
        List<Integer> dataList = Arrays.stream(((String) stringStringMap.get("data")).
                split(",")).
                map(Integer::parseInt).
                collect(Collectors.toList());
        if (!stringStringMap.containsKey("data") || cols == null
                || rows == null || dataList.size() != cols * rows * 3
                || "/uploadFace".equals(uri.getPath())
                && (personName == null || "".equals(personName))) {
            result.setStatus(402);
            result.setMessage("数据出错，数据不全或者格式不对");
            response(ctx, result);
            return;
        }
        String imgKey = UUIDUtils.getCode();
//        String resultKey = UUIDUtils.getCode();
        try {
            if (personName != null && "/uploadFace".equals(uri.getPath()))
            redisTemplate.opsForHash().put(imgKey, "personName", personName);
            redisTemplate.opsForHash().put(imgKey, "data", JSON.toJSONString(dataList));
            redisTemplate.opsForHash().put(imgKey, "cols", String.valueOf(cols));
            redisTemplate.opsForHash().put(imgKey, "rows", String.valueOf(rows));
            System.out.println("图片临时存储在redis的:" + imgKey);
            System.out.println("rows:" + String.valueOf(rows));
            System.out.println("cols:" + String.valueOf(cols));
            int randomId = 0;
            if ("/checkFace".equals(uri.getPath())) {
                // 执行人脸识别
                randomId = new Random().nextInt(recognitionProcessCount);
                System.out.println("发布人脸识别：" + "checkFace"+randomId);
                redisTemplate.convertAndSend("checkFace"+randomId,imgKey + "_" + mychannel);

            } else if ("/uploadFace".equals(uri.getPath())) {
                System.out.println("Face Id：" + personName);
                // 执行人脸id上传
                randomId = new Random().nextInt(uploadProcessCount);
                System.out.println("人脸识别：" + "uploadFace"+randomId);
                redisTemplate.convertAndSend("uploadFace"+randomId,imgKey + "_" + mychannel);
//
            }
            System.out.println("消息的key：" + imgKey + "_" + mychannel);

        } catch (Exception e) {
            e.printStackTrace();
            result = new Result(500, "程序执行出错");
        }

    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
//        Channel channel=message.getChannel();
        Channel channel= getCtx().channel();
        System.out.println(channel);

        RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
        String msg= redisSerializer.deserialize(message.getBody());
        if(msg.split("_")[2].equals(channel.toString()))
            System.out.println("接收到的消息是："+ msg);
            ChannelHandlerContext ctx1 = getCtx();


            Result result = new Result(Integer.parseInt(msg.split("_")[0]),
                    msg.split("_")[1]);
            System.out.println("服务器回复:" + result.getStatus() + "---" + result.getMessage());
    //        System.out.println(((new Date()).getTime() - startTime) / 1000.0 + "秒");

            response(ctx, result);


    }

}

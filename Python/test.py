#!/usr/bin/python
#coding:utf-8
#服务器端
import redis
import json

r = redis.Redis(host='127.0.0.1',port='6379') #连接redis
p = r.pubsub()        #开启订阅
p.subscribe('6666') #接收订阅的数据,订阅的频道


for item in p.listen(): #读取接收的数据
    # print(item)
    if item['type'] == 'message': #判断数据是否是用户发布的数据
        data = item['data']     #取出用户要发布的数据
        # result = str(data, 'utf-8')
        # result = json.loads(result)
        # print(result)
        print(data)   #打印要发布的数据

        if item['data'] == 'Q' or item['data'] == 'q':
            break;       #退出程序

p.unsubscribe('6666') #关闭频道
print('取消订阅')
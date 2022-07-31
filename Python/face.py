# -*- coding: utf-8 -*-
import face_recognition
import sys
import redis
from datetime import datetime
import time
from PIL import Image
import os
import uuid
import numpy as np
from numpy import float64
import json
from multiprocessing import Process
from multiprocessing import Pool

def recognize_face(item):
    redisCli = redis.Redis(host="127.0.0.1", port=6379)
    imgKey, resultKey = item['data'].split("_")
    if imgKey==None or resultKey==None:
        return
    result_status = 200
    result_msg = ""
    # 1.加载未知人脸图片
    cols = int(redisCli.hget(imgKey, "cols"))
    rows = int(redisCli.hget(imgKey, "rows"))
    data = redisCli.hget(imgKey, "data")
    unknown_face = np.array(json.loads(data), dtype=np.uint8)
    unknown_face = unknown_face.reshape(rows, cols, 3)
    redisCli.delete(imgKey)
    # 2.对未知人脸图片进行编码
    unknown_face_encodings = face_recognition.face_encodings(unknown_face)
    if len(unknown_face_encodings) == 0:
        # redisCli.set(resultKey, str(500) + "_" + "未检测到人脸")
        redisCli.publish("channel:1", str(500) + "_" + "未检测到人脸"+ "_" +resultKey)
        return
    unknown_face_encoding = unknown_face_encodings[0]
    # 3.加载所有已知人脸
    known_faces_encoding = []
    face_infos = redisCli.lrange('face_infos', 0, -1)
    for face_info in face_infos:
        face_info = eval(str(face_info, encoding='utf-8'))
        face_encoding = np.array(json.loads(face_info[1]))
        known_faces_encoding.append(face_encoding)
    if len(known_faces_encoding)==0:
        # redisCli.set(resultKey, str(500) + "_" + "人脸库为空，先录入人脸")
        redisCli.publish("channel:1", str(500) + "_" + "人脸库为空，先录入人脸"+ "_" +resultKey)
        return
    # 4.识别人脸
    face_distances = face_recognition.face_distance(known_faces_encoding, unknown_face_encoding)
    min_index = np.argmin(face_distances)
    if face_distances[min_index] <= 0.4:
        result_msg = eval(str(face_infos[min_index], encoding='utf-8'))[0]
    else:
        result_msg = "未知人脸"
    # 5.返回结果
    # redisCli.set(resultKey, str(result_status) + "_" + result_msg)
    redisCli.publish("channel:1", str(result_status) + "_" + result_msg+ "_" +resultKey)
    print("结果:", str(result_status) + "_" + result_msg+ "_" +resultKey)



def upload_faceid(item):
    redisCli = redis.Redis(host="127.0.0.1", port=6379)
    imgKey,resultKey = item['data'].split("_")
    if imgKey==None or resultKey==None:
        return 
    result_status = 200
    result_msg = "人脸录入成功"
    # print(redisCli.hkeys(imgKey))
    personName = str(redisCli.hget(imgKey, "personName"), encoding='UTF-8')
    print('personName:'+personName)
    cols = int(redisCli.hget(imgKey, "cols"))
    rows = int(redisCli.hget(imgKey, "rows"))
    data = redisCli.hget(imgKey, "data")
    upload_image = np.array(json.loads(data), dtype=np.uint8)
    upload_image = upload_image.reshape(rows, cols, 3)
    # 将人脸删除
    redisCli.delete(imgKey)
    # 2.对人脸图片进行编码，只要第一张人脸。
    image_face_encodings = face_recognition.face_encodings(upload_image)
    if len(image_face_encodings) == 0:
        # redisCli.set(resultKey, str(500) + "_" + "未检测到人脸")
        redisCli.publish("channel:1", str(500) + "_" + "未检测到人脸"+ "_" +resultKey)
        return
    image_face_encoding = image_face_encodings[0]
    # 3.将人脸图片截取保存到文件夹
    top, right, bottom, left = face_recognition.face_locations(upload_image)[0]
    face_img = upload_image[top:bottom, left:right]
    pil_img = Image.fromarray(face_img)
    # 3.1 生成随机的文件名，拼接保存路径
    save_name = str(uuid.uuid1())
    save_path = "/FaceRecognition/pictures_of_people_i_know/" + personName
    if not os.path.exists(save_path):
        os.makedirs(save_path)
    save_path += "/" + save_name + ".jpg"
    # 3.2保存人脸图片
    pil_img.save(save_path)
    print(os.getcwd())
    print(save_path)
    # 4.将人脸的128维编码信息存到redis中
    face_info = [personName, str(image_face_encoding.tolist()), "save_path"]
    redisCli.rpush("face_infos", str(face_info))
    # redisCli.set(resultKey,str(result_status)+"_"+result_msg)
    redisCli.publish("channel:1", str(result_status) + "_" + result_msg+ "_" +resultKey)
    print("结果:",str(result_status)+"_"+result_msg+ "_" +resultKey)




# if __name__ == '__main__':
#     uploadProcessCount = 5
#     recognitionProcessCount = 5
#     processPool = Pool(uploadProcessCount+recognitionProcessCount)
#     for i in range(recognitionProcessCount):
#         processPool.apply_async(recognize_face, ("checkFace"+str(i),))
#     #创建上传人脸的进程池
#     for i in range(uploadProcessCount):
#         processPool.apply_async(upload_faceid, ("uploadFace"+str(i),))
#     # 关闭子进程池
#     processPool.close()
#     # 等待所有子进程结束
#     processPool.join()

rc = redis.Redis(host="127.0.0.1", port=6379, decode_responses=True)
ps = rc.pubsub()
ps1 = rc.pubsub()
ps2 = rc.pubsub()
ps3 = rc.pubsub()
ps4 = rc.pubsub()
ps5 = rc.pubsub()
ps6 = rc.pubsub()
ps7 = rc.pubsub()
ps8 = rc.pubsub()
ps9 = rc.pubsub()

ps.subscribe(**{"checkFace1": recognize_face})
ps1.subscribe(**{"checkFace2": recognize_face})
ps2.subscribe(**{"checkFace3": recognize_face})
ps3.subscribe(**{"checkFace4": recognize_face})
ps4.subscribe(**{"checkFace0": recognize_face})
ps5.subscribe(**{"uploadFace1": upload_faceid})
ps6.subscribe(**{"uploadFace2": upload_faceid})
ps7.subscribe(**{"uploadFace3": upload_faceid})
ps8.subscribe(**{"uploadFace4": upload_faceid})
ps9.subscribe(**{"uploadFace0": upload_faceid})
# ps.subscribe(**{"234": handle}) 
ps.run_in_thread(0.03)
ps1.run_in_thread(0.03)
ps2.run_in_thread(0.03)
ps3.run_in_thread(0.03)
ps4.run_in_thread(0.03)
ps5.run_in_thread(0.03)
ps6.run_in_thread(0.03)
ps7.run_in_thread(0.03)
ps8.run_in_thread(0.03)
ps9.run_in_thread(0.03)




# if __name__ == '__main__':
#     uploadProcessCount = 0
#     recognitionProcessCount = 5
#     processPool = Pool(uploadProcessCount+recognitionProcessCount)
#     #创建人脸识别的线程池
#     for i in range(recognitionProcessCount):
#         processPool.apply_async(recognize_face, ("checkFace"+str(i),))
#     #创建上传人脸的进程池
#     # for i in range(uploadProcessCount):
#     #     processPool.apply_async(upload_faceid, ("uploadFace"+str(i),))
#     # 关闭子进程池
#     processPool.close()
#     # 等待所有子进程结束
#     processPool.join()


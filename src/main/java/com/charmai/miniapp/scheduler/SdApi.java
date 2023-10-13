package com.charmai.miniapp.scheduler;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class SdApi {
    private static Logger logger = LoggerFactory.getLogger(SdApi.class);
    public static final String ipPort = "http://127.0.0.1:8078";
    public static final String loraTrainUrl = "/lora_train";
    public static final String getTrainStatusUrl = "/check_train_status";
    public static final String generatePhoto = "/submit";
    public static final String uploadModel = "/upload_model";
    public static final String getModelList = "/get_model_list";
    public static String loraTrain(List<File> images, String userId, String gpuAddress) {
        String url = ipPort + loraTrainUrl;
        HttpRequest post = HttpRequest.post(url);

        File[] array = new File[images.size()];
        for (int i = 0; i < images.size(); i++) {
            array[i] = images.get(i);
        }
        HttpResponse response = post
                .form("images", array)
                .form("ip", gpuAddress)
                .form("output_name", userId)
                .form("template_name", "default")
                .header("Content-Type", "multipart/form-data")
                .execute();
        logger.info("调用:" + loraTrainUrl);
        int status = response.getStatus();
        logger.info("请求响应status:" + status);
        if (status != 200) {
            return null;
        }
        String body = response.body();
        logger.info("请求响应body:"+body);
        JSONObject jsonObject = JSONObject.parseObject(body);
        Object code = jsonObject.get("code");
//        logger.info("请求响应code:" + code);
        if ((Integer) code == 1) {
            String taskId = (String) jsonObject.get("task_id");
            logger.info(taskId);
            return taskId;
        } else {
            logger.error("loraTrain失败:" + userId + jsonObject.get("detail"));
        }
        return null;
    }

    public static TrainInfo getTrainStatus(String userId, String taskId, String gpuAddress) {
        String url = ipPort + getTrainStatusUrl + "?task_id=" + taskId + "&ip=" + gpuAddress;
        HttpResponse response = HttpRequest.get(url)
                .header("Content-Type", "application/json;charset=UTF-8").execute();
        logger.info("调用:" + getTrainStatusUrl);
        int status = response.getStatus();
        logger.info("请求响应status:" + status);
        if (status != 200) {
            return null;
        }
        String body = response.body();
        logger.info("请求响应body:"+body);
        JSONObject jsonObject = JSONObject.parseObject(body);

        Object code = jsonObject.get("code");
//        logger.info("请求响应code:" + code);
        if ((Integer) code == 1) {
            TrainInfo trainInfo = JSONObject.parseObject(jsonObject.get("data").toString(), TrainInfo.class);

            logger.info("返回的trainInfo:" +trainInfo.toString());

            return trainInfo;
        } else {
            logger.error("userId getTrainStatus失败:" + userId + jsonObject.get("detail"));
        }
        return null;
    }

    public static List<String> generatePhoto(String userId, String templateName, String prompt, String negative_prompt, File image, File faceFile, String gpuAddress) {
        try {
            logger.info("prompt：" + prompt + "，ip：" + gpuAddress);
            logger.info("negative_prompt：" + negative_prompt);
            logger.info("faceFile：" + faceFile);
            logger.info("template_name：" + templateName + "，controlnet_image：" + image);
            String url = ipPort + generatePhoto;
            HttpResponse response = HttpRequest.post(url)
                    .form("prompt", prompt)
                    .form("ip", gpuAddress)
                    .form("negative_prompt", negative_prompt)
                    .form("roop_image", faceFile)
                    .form("template_name", templateName)
                    .form("batch_size", 2)
                    .form("controlnet_image", image)
                    .header("Content-Type", "multipart/form-data").execute();
            int status = response.getStatus();
            logger.info("调用:" + generatePhoto);
            logger.info("请求响应status:" + status);
            if (status != 200) {
                return null;
            }
            String body = response.body();
//            logger.info("请求响应body:" + body);
            JSONObject jsonObject = JSONObject.parseObject(body);

            Object code = jsonObject.get("code");
        logger.info("请求响应code:" + code);
//        logger.info("请求响应images:" + jsonObject.toJSONString());

            if ((Integer) code == 1) {
                JSONArray iamges = JSONObject.parseArray(jsonObject.get("images").toString());
                List<String> list = iamges.toJavaList(String.class);
                logger.info("返回的图片:" + list.size());
                return list;
            } else {
                logger.error("generatePhoto失败:" + userId + ", " + jsonObject.get("detail"));
            }
        } catch (Exception e) {
            logger.error("generatePhoto失败:" + userId + ", " + e);
        }
        return null;
    }

    public static Boolean uploadModel(String userId, String modelPath, String modelType,String modelName, String gpuAddress) {
        if (modelType == null) {
            modelType = "lora";
        }
        String url = ipPort + uploadModel;
        HttpResponse response = HttpRequest.post(url)
                .form("model_path", modelPath)
                .form("ip", gpuAddress)
                .form("model_type", modelType)
                .form("model_name",modelName)
                .header("Content-Type", "multipart/form-data").execute();
        int status = response.getStatus();
        logger.info("调用:" + uploadModel);
        logger.info("请求响应status:" + status);
        if (status != 200) {
            return null;
        }
        String body = response.body();
        logger.info("请求响应body:"+body);
        JSONObject jsonObject = JSONObject.parseObject(body);

        Object code = jsonObject.get("code");
//        logger.info("请求响应code:" + code);

        if ((Integer) code == 1) {

            return true;
        } else {
            logger.error("uploadModel失败:" + userId + jsonObject.get("detail"));
        }
        return null;
    }

    public static List<ModelExist> getModelList(String modelType, String gpuAddress) {
        if (modelType == null) {
            modelType = "lora";
        }
        String url = ipPort + getModelList + "?model_type=" + modelType + "&ip=" + gpuAddress;
        HttpResponse response = HttpRequest.get(url)
                .header("Content-Type", "application/json;charset=UTF-8").execute();
        logger.info("调用:" + getModelList);
        int status = response.getStatus();
        logger.info("请求响应status:" + status);
        if (status != 200) {
            return null;
        }
        String body = response.body();
        logger.info("请求响应body:"+body);
        JSONObject jsonObject = JSONObject.parseObject(body);

        Object code = jsonObject.get("code");
//        logger.info("请求响应code:" + code);
        if ((Integer) code == 1) {
            List<ModelExist> modelList = JSONObject.parseArray(jsonObject.get("model_list").toString(), ModelExist.class);

            logger.info("返回的 modelList:" + modelList.size());

            return modelList;
        } else {
            logger.error("getModelList失败:" + jsonObject.get("detail"));
        }
        return null;
    }
}

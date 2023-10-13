package com.charmai.miniapp.scheduler;

import cn.hutool.core.codec.Base64Encoder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class SdApiTest {

//    public static final String gpuAddress = "http://127.0.0.1:7860";
    public static final String gpuAddress = "http://127.0.0.1:28000";
    @Test
    public void loraTrain() throws IOException {
        String folderPath = "C:\\Users\\Administrator\\Downloads\\25PoseCollection_v10\\r6.png"; // 文件夹路径
        String base64Image1 = convertToBase64(folderPath);
        List<String> jpgFiles = getJpgFiles(folderPath);
        List<String> base64File = new ArrayList<>();
        List<File> files = new ArrayList<>();

        int i = 0;
        for (String file : jpgFiles) {
            if (i > 0) {
                break;
            }
            System.out.println(file);

            String base64Image = convertToBase64(file);
            base64File.add(base64Image);
            files.add(new File(file));
            i++;
            // TODO: 进行具体的处理，比如将 Base64 编码的图像进行解码或其他操作
        }
        String taskId = SdApi.loraTrain(files, "bestTest", gpuAddress);
        System.out.println(taskId);
    }
    private static List<String> getJpgFiles(String folderPath) {
        List<String> jpgFiles = new ArrayList<>();

        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().toLowerCase().endsWith(".jpg")) {
                        jpgFiles.add(file.getAbsolutePath());
                    }
                }
            }
        }

        return jpgFiles;
    }

    public static String convertToBase64(String filePath) {
        String base64Image = "";
        try {
            Path path = Paths.get(filePath);
            byte[] imageBytes = Files.readAllBytes(path);
            base64Image = Base64Encoder.encode(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64Image;
    }
    @Test
    public void getTrainStatus() {
        String taskId="f744972a-2c8a-11ee-83ba-f8b54dee6b4f";
        TrainInfo trainStatus = SdApi.getTrainStatus("hyc", taskId, gpuAddress);
        System.out.println(trainStatus.toString());
    }

    @Test
    public void getModelList() {
        List<ModelExist> modelList = SdApi.getModelList(null, gpuAddress);
        System.out.println(modelList.toString());
    }

    @Test
    public void uploadModel() throws IOException {
        String modelPath="https://test001-1311934233.cos.ap-nanjing.myqcloud.com/test/20230730000940_C__Users_Administrator_Documents_WeChat_Files_hyc_weixin_FileStorage_File_2023_07_test.safetensors";
        String modelType = "lora";
        String modelName = "20230730000940_test.safetensors";

        Boolean result = SdApi.uploadModel("hyc", modelPath, modelType, modelName , gpuAddress);
        System.out.println("返沪结果：" + result);
    }

    @Test
    public void generatePhoto() throws IOException {
        String filePath="C:\\Users\\Administrator\\Downloads\\微信图片_20230727233652.png";
//        Path path = Paths.get(filePath);
//        byte[] imageBytes = Files.readAllBytes(path);
//        String base64Image = Base64Encoder.encode(imageBytes);
        File file = new File(filePath);

        String prompt = "<lora:develop_20230715201709-000004:0.7>,<lora:FilmVelvia3 (1):0.4>,<lora:its_20230725125120:0.7>,<lora:get_20230724195948:0.5>";
        String templateName = "aaaaa-test";
        List<String> list = SdApi.generatePhoto("hyc", "default", "123","123", file, file, gpuAddress);
        for (String s : list) {
            System.out.println(s);
        }
    }
}
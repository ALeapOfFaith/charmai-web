package com.charmai.miniapp.scheduler;

import cn.hutool.core.codec.Base64Encoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static final String BaseDir = "/tmp/";
//    private static final String BaseDir = "F:\\tmp\\";
    private static Logger logger = LoggerFactory.getLogger(FileUtils.class);
    public static File getPicFile(String userId, String imageUrl){
        String name = imageUrl.substring(imageUrl.lastIndexOf("/"));
        // 下载图片到本地文件

        String filePath = BaseDir + userId + name;
//        if(filePath.endsWith(".png")){
//            filePath = filePath.replace(".png", ".jpg");
//        }
        Path userIdDirectory = Path.of(BaseDir + userId);
        if (Files.exists(userIdDirectory)) {
//            logger.info("目录已存在：" + userIdDirectory.toAbsolutePath());
        } else {
            try {
                Files.createDirectories(userIdDirectory);
                logger.info("目录已成功创建：" + userIdDirectory.toAbsolutePath());
            } catch (IOException e) {
                logger.error("目录创建失败：" + userIdDirectory.toAbsolutePath());
            }
        }
        Path imagePath = Path.of(filePath);
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file;
            } else {
                downloadImage(imageUrl, imagePath);
                return new File(filePath);
            }
        } catch (IOException e) {
            logger.error("图片下载失败：" + imageUrl);
        }
        return null;
    }

    private static void downloadImage(String imageUrl, Path outputPath) throws IOException {
        URL url = new URL(imageUrl);
        InputStream inStream = url.openStream();

        Files.copy(inStream, outputPath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("图片已成功下载到：" + outputPath.toAbsolutePath());
    }

    public static void main(String[] args) throws IOException {
//        getPicFile("123456","https://test001-1311934233.cos.ap-nanjing.myqcloud.com/1684943766369370113_06a6cab9-ff31-44f8-9968-a9c1a8e4d49c.png");
        deleteDirectory(Path.of("/tmp"+"/"+"1684943766369370113"));
    }
    public static void deleteDirectory(Path directory) {
        if (Files.isDirectory(directory)) {
            // 获取目录下所有文件和子目录的路径
            try (var entries = Files.list(directory)) {
                for (var entry : entries.toArray(Path[]::new)) {
                    // 递归删除子目录和文件
                    deleteDirectory(entry);
                }
            } catch (IOException e) {
                logger.error("删除目录失败：" + e.getMessage());
            }
        }
        // 删除空目录或文件
        try {
            Files.deleteIfExists(directory);
        } catch (IOException e) {
            logger.error("删除目录失败：" + e.getMessage());
        }
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

    public static String downloadUserLoraToTmp(String fileUrl, String loraName) {


        try {
            String savePath = "/tmp/"+loraName;  // 替换为保存到的路径和文件名
            URL url = new URL(fileUrl);
            InputStream inputStream = url.openStream();

            // 下载文件到指定路径
            Path outputPath = Path.of(savePath);
            Files.copy(inputStream, outputPath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File download ：" + savePath);
            return savePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<File> getControlNetImage(String folderPath) {
        List<File> imageFiles = new ArrayList<>();

        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
//                        String base64Image = convertToBase64(file.getAbsolutePath());
                        imageFiles.add(file);
                    }
                }
            }
        }

        return imageFiles;
    }

    public static String getJsonString(String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }

            return content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

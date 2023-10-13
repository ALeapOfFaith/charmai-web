package com.charmai.miniapp.service.impl;

import cn.hutool.core.codec.Base64Encoder;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Base64MultipartFile implements MultipartFile {
    public static void main(String[] args) throws IOException {
        String filePath="C:\\Users\\Administrator\\Downloads\\微信图片_20230727233652.png";
        Path path = Paths.get(filePath);
        byte[] imageBytes = Files.readAllBytes(path);
        String base64Image = Base64Encoder.encode(imageBytes);

        // 解码Base64数据为二进制
        byte[] imageData = Base64Utils.decodeFromString(base64Image);

        // 创建MultipartFile对象
        MultipartFile multipartFile = new Base64MultipartFile(imageData, "image.png", "image/png");

        // 使用MultipartFile对象进行相应操作
        // ...

        System.out.println("转换成功");
    }
    private final byte[] content;
    private final String name;
    private final String contentType;

    public Base64MultipartFile(byte[] content, String name, String contentType) {
        this.content = content;
        this.name = name;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getOriginalFilename() {
        return this.name;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
    public long getSize() {
        return this.content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return this.content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(this.content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        Files.write(dest.toPath(), this.content);
    }
}

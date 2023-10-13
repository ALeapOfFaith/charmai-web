package com.charmai.oss.client.entity;

public class UserImage {
    private int oss_id;
    private String file_name;
    private String original_name;
    private String file_suffix;
    private String url;

    public UserImage(int oss_id, String file_name, String original_name, String file_suffix, String url) {
        this.oss_id = oss_id;
        this.file_name = file_name;
        this.original_name = original_name;
        this.file_suffix = file_suffix;
        this.url = url;
    }

    public int getOss_id() {
        return oss_id;
    }

    public void setOss_id(int oss_id) {
        this.oss_id = oss_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
    }

    public String getFile_suffix() {
        return file_suffix;
    }

    public void setFile_suffix(String file_suffix) {
        this.file_suffix = file_suffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

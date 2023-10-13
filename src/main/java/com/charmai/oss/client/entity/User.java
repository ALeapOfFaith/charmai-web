package com.charmai.oss.client.entity;

public class User {  //实体类，与数据库保持一致，entity用于做数据库映射
    private int pid;
    private String name;
    private String des;
    private String pic;
    private float price;
    private int cid;

    public User(int pid, String name, String des, String pic, float price, int cid) {
        this.pid = pid;
        this.name = name;
        this.des = des;
        this.pic = pic;
        this.price = price;
        this.cid = cid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}

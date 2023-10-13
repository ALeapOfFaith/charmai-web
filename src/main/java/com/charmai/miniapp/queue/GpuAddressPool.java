package com.charmai.miniapp.queue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GpuAddressPool {
    private final Set<String> loraIpPool;
    private final Set<String> ipPool;

    public GpuAddressPool() {
        // 使用线程安全的Set实现，确保并发操作时不会出现问题
        loraIpPool = Collections.synchronizedSet(new HashSet<>());
        ipPool = Collections.synchronizedSet(new HashSet<>());
    }

    public synchronized String getIP() {
        if (ipPool.isEmpty()) {
            return null; // IP池为空，返回null
        }

        // 从IP池中获取一个IP
        String ip = ipPool.iterator().next();
        System.out.println("get Ip:" + ip);
        ipPool.remove(ip);
        return ip;
    }

    public synchronized void addIP(String ip) {
        System.out.println("add Ip:" + ip);
        ipPool.add(ip);
    }

    public synchronized void removeIP(String ip) {
        ipPool.remove(ip);
    }

    public synchronized Boolean containsIp(String ip) {
        return ipPool.contains(ip);
    }

    public synchronized Boolean containsLoraIp(String ip) {
        return loraIpPool.contains(ip);
    }

    public synchronized String getLoraIp() {
        if (loraIpPool.isEmpty()) {
            return null; // IP池为空，返回null
        }

        // 从IP池中获取一个IP
        String ip = loraIpPool.iterator().next();
        loraIpPool.remove(ip);
        System.out.println("get Lora Ip:" + ip);
        return ip;
    }

    public synchronized void addLoraIp(String ip) {
        System.out.println("add Lora Ip:" + ip);
        loraIpPool.add(ip);
    }

    public synchronized void removeLoraIp(String ip) {
        loraIpPool.remove(ip);
    }

    public synchronized void removeAllLoraIP() {
        if (loraIpPool != null && loraIpPool.size() > 0) {
            Iterator<String> iterator = loraIpPool.iterator();
            while (iterator.hasNext()) {
                iterator.remove();
            }
        }
    }

    public synchronized void removeAllIP() {
        if (ipPool != null && ipPool.size() > 0) {
            Iterator<String> iterator = ipPool.iterator();
            while (iterator.hasNext()) {
                iterator.remove();
            }
        }
    }

    public synchronized Set<String> getCacheNode() {
        Set<String> cacheNodeAddress = new HashSet<>();
        cacheNodeAddress.addAll(ipPool);
        cacheNodeAddress.addAll(loraIpPool);
        return cacheNodeAddress;

    }

}


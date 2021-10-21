package com.asura.jdbc.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 单例线程池
 * 单例模式优缺点
 * 1.饿汉式
 * 2.懒汉式
 * 3.
 *
 */
public class PoolsUtil {
    private static class PoolsUtilBuider {
        private static final PoolsUtil poolsUtil = new PoolsUtil();
    }
    private int initPoolSize = 10;
    private int maxPoolSize = 40;
    private String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC";
    private String username = "root";
    private String password = "hello_123456789";
    private List<Connection> userdConnections;
    private List<Connection> validConnections;
    private Lock extendLock;

    private PoolsUtil() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        extendLock = new ReentrantLock();
        userdConnections = new CopyOnWriteArrayList<>();
        validConnections = new CopyOnWriteArrayList<>();
        List<Connection> initConns = createConnections(initPoolSize);
        validConnections.addAll(initConns);
        new Thread(() -> reBuildValidConnections()).start(); // 初始化时候开启自动回收
    }

    private List<Connection> createConnections(int count) {
        if (count < 1) {
            return Collections.emptyList();
        }
        List<Connection> initConns =  IntStream.range(0, count).mapToObj(num -> {
                    try {
                        return createConn(url, username, password);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    return null;
                }
        ).collect(Collectors.toList());
        return initConns;
    }

    private Connection createConn(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public static PoolsUtil getInstance() {
        return PoolsUtilBuider.poolsUtil;
    }

    public Connection getConnection() {
        autoExtentPools(); // 自动扩容 须保证多线程
        Connection connection = null;
        boolean isValid = calcConnecValid(); // 检查并等待连接池已被使用达到最大
        if (!isValid) {
            System.out.println("等待超时!连接池满没有空闲链接");
            throw new RuntimeException("连接池满！没有空闲链接");
        } else {
            connection = getValidConnection(); // 获取空闲连接  需要等待空闲链接避免该过程链接已空
        }
        return connection;
    }

    private void autoExtentPools() {
        extendLock.lock();
        int currentSize = userdConnections.size() + validConnections.size();
        if (validConnections.size() < initPoolSize / 2 && currentSize < maxPoolSize) {
            int size = (currentSize + initPoolSize) > maxPoolSize ? maxPoolSize - currentSize : initPoolSize;
            System.out.println("prepare extend num:" + size + "|userd:" + userdConnections.size() + "|validConnections" + validConnections.size());
            if (size > 0 && currentSize + size <= maxPoolSize) { // 多个线程同时都判断扩容超过最大容量或又存在了可用线程视为无效
                System.out.println("real extend num" + size);
                validConnections.addAll(createConnections(size));
            }
        }
        extendLock.unlock();
    }

    private void reBuildValidConnections() {
        int count = 0;
        int maxSize = 0;
        try {
            while (true) {
                if (count > 10) { // 10分钟整理一下可用链接
                    if (maxSize > initPoolSize) {
                        validConnections = validConnections.subList(0, maxSize - 1);
                    }
                }
                maxSize = maxSize > validConnections.size() ? maxSize : validConnections.size();
                System.out.println("check validConns:" + maxSize);
                Thread.sleep(1000 * 60);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private synchronized Connection getValidConnection() {
        Connection connection = null;
        if (checkValidConne()) {
            Iterator<Connection> iterator = validConnections.iterator();
            while (iterator.hasNext()) {
                connection = iterator.next();
                if (connection != null) {
                    userdConnections.add(connection);
                    break;
                }
            }
            validConnections.remove(connection);
        } else {
            System.out.println("没有空闲链接");
            throw new RuntimeException("没有空闲链接");
        }
        return connection;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            userdConnections.remove(connection);
            validConnections.add(connection);
        }
    }

    private boolean calcConnecValid() {
        boolean isValid = true;
        int count = 0;
        while (userdConnections.size() >= maxPoolSize) {
            if (count > 30) { // 1分钟超时
                isValid = false;
                break;
            }
            try {
                Thread.sleep(1000);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }

    private boolean checkValidConne() {
        boolean isValid = true;
        int count = 0;
        while (validConnections.size() == 0) {
            if (count > 30) { // 30s超时
                isValid = false;
                break;
            }
            try {
                Thread.sleep(1000);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return isValid;
    }

    public void print() {
        System.out.println(validConnections.size() + "_" + userdConnections.size());
    }
}

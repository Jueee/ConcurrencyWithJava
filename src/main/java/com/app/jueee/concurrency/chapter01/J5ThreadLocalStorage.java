package com.app.jueee.concurrency.chapter01;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 线程局部存储模式
 */
public class J5ThreadLocalStorage {

    /**
     * 数据库链接管理：
     *
     * 这段代码在单线程中使用是没有任何问题。
     *
     * 在多线程中使用会存在线程安全问题：
     * 第一，这里面的2个方法都没有进行同步，很可能在openConnection方法中会多次创建connect；
     * 第二，由于connect是共享变量，那么必然在调用connect的地方需要使用到同步来保障线程安全，因为很可能一个线程在使用connect进行数据库操作，而另外一个线程调用closeConnection关闭链接。
     */
    private static Connection connect = null;
    public static Connection openConnection() throws Exception{
        if(connect == null){
            connect = DriverManager.getConnection("DB_URL");
        }
        return connect;
    }
    public static void closeConnection()  throws Exception{
        if(connect!=null)
            connect.close();
    }

    /**
     * ThreadLocal 解决 数据库连接
     */
    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<Connection>() {
        @Override
        protected Connection initialValue() {
            try{
                return DriverManager.getConnection("DB_URL");
            }catch (Exception e){
                return null;
            }
        }
    };

    public static Connection getConnection() {
        return connectionHolder.get();
    }
}

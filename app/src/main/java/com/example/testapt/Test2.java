package com.example.testapt;

public class Test2 {

    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
        threadLocal.set("父类数据:threadLocal");
        inheritableThreadLocal.set("父类数据:inheritableThreadLocal");

        new Thread(new Runnable() {
            @Override
            public void run() {
                //此时子线程直接去获取主线程ThreadLocal的数据时空的
                System.out.println("子线程获取父类threadLocal数据：" + threadLocal.get());
                //InheritableThreadLocal实现了线程间的数据共享
                System.out.println("子线程获取父类inheritableThreadLocal数据：" + inheritableThreadLocal.get());
            }
        }).start();
    }
}

package com.example.testapt;

public class ThreadLocalTest {

    //会出现内存泄漏的问题,下文会描述
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("主线程main");
        new Thread(new A()).start();
        new Thread(new B()).start();
        System.out.println(threadLocal.get());

    }

    static class A implements Runnable {
        @Override
        public void run() {
            threadLocal.set("线程A");
            System.out.println(threadLocal.get());
        }
    }

    static class B implements Runnable {
        @Override
        public void run() {
            threadLocal.set("线程B");
            System.out.println(threadLocal.get());
        }
    }
}

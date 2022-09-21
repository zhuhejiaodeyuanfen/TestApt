package com.example.testapt;

public class MyThreadLocalTest {
    public static void main(String[] args) {

        MyThread thread = new MyThread();

        MyThreadLocal<String> myThreadLocal1 = new MyThreadLocal<>(thread);
        myThreadLocal1.set("第1个");

        MyThreadLocal<String> myThreadLocal2 = new MyThreadLocal<>(thread);
        myThreadLocal2.set("第2个");


        MyThreadLocal<String> myThreadLocal3 = new MyThreadLocal<>(thread);
        myThreadLocal3.set("第3个");





        MyThreadLocal<String> myThreadLocal4 = new MyThreadLocal<>(thread);
        myThreadLocal4.set("第4个");



        MyThreadLocalMap map = thread.getMap();
        map.remove(myThreadLocal1);
        map.remove(myThreadLocal2);


        MyThreadLocal<String> myThreadLocal5 = new MyThreadLocal<>(thread);
        myThreadLocal4.set("第5个");

    }
}

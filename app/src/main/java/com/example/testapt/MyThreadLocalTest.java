package com.example.testapt;

import java.lang.ref.WeakReference;

public class MyThreadLocalTest {
    public static void main(String[] args) {
//        int n=100;
//
//        System.out.println(n>>>=1);
//        System.out.println(n>>>=1);
//        System.out.println(n>>>=1);
//        System.out.println(n>>>=1);
//        System.out.println(n>>>=1);

        MyThread thread = new MyThread();

        MyThreadLocal myThreadLocal1 = new MyThreadLocal(thread);
        myThreadLocal1.set(1);

        MyThreadLocal myThreadLocal2 = new MyThreadLocal(thread);
        myThreadLocal2.set(2);


        MyThreadLocal myThreadLocal3 = new MyThreadLocal(thread);
        myThreadLocal3.set(3);


        MyThreadLocal myThreadLocal4 = new MyThreadLocal(thread);
        myThreadLocal4.set(1);


        MyThreadLocal myThreadLocal5 = new MyThreadLocal(thread);
        myThreadLocal5.set(5);


        MyThreadLocal myThreadLocal6 = new MyThreadLocal(thread);
        myThreadLocal6.set(6);

        MyThreadLocal myThreadLocal7 = new MyThreadLocal(thread);
        myThreadLocal7.set(7);
        myThreadLocal1 = null;
        System.gc();

        System.out.println("for循环前");
        for (int i = 10; i < 20; i++) {
            MyThreadLocal myThreadLocal = new MyThreadLocal(thread);
            myThreadLocal.set(i);
        }
        System.gc();
        System.out.println("for循环后");

        System.out.println("=======gc过后========" + myThreadLocal7.get());
        myThreadLocal7 = null;
        System.gc();


        MyThreadLocalMap map = thread.getMap();

        map.remove(myThreadLocal3);
//        map.remove(myThreadLocal2);


        MyThreadLocal myThreadLocal8 = new MyThreadLocal(thread);
        myThreadLocal4.set(8);

        MyThreadLocal myThreadLocal9 = new MyThreadLocal(thread);
        myThreadLocal4.set(9);


        System.out.println("========gc========");
        String test = new String("dd");
        WeakReference<String> weakReference = new WeakReference<String>(test);

        System.out.println(weakReference.get());

        System.gc();
        System.out.println(weakReference.get());

        test = null;
        System.gc();
        System.out.println(weakReference.get());

    }
}

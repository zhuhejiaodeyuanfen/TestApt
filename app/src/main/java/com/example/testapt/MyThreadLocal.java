package com.example.testapt;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThreadLocal<T> {


    private MyThread myThread;

    public MyThreadLocal(MyThread myThread) {
        this.myThread = myThread;
    }

    public final int threadLocalHashCode = nextHashCode();


    private static AtomicInteger nextHashCode =
            new AtomicInteger();


    private static final int HASH_INCREMENT = 0x61c88647;


    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }


    public T get() {
        MyThreadLocalMap map = myThread.getMap();
        if (map != null) {
            MyThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T) e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    public MyThreadLocalMap.Entry getEntry() {
        MyThreadLocalMap map = myThread.getMap();
        if (map != null) {
            MyThreadLocalMap.Entry e = map.getEntry(this);
           return e;
        }
       return null;
    }

    protected T initialValue() {
        return null;
    }

    private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        MyThreadLocalMap map = myThread.getMap();
        if (map != null)
            map.set(this, value);
        else
            createMap(value);
        return value;
    }


    public void set(T value) {
        MyThreadLocalMap map = myThread.getMap();
        if (map != null)
            map.set(this, value);
        else
            createMap(value);
    }


    void createMap(T firstValue) {

        myThread.threadLocalMap = new MyThreadLocalMap(this, firstValue);
    }
}

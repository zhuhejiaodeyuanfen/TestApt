package com.example.testapt;

import java.lang.ref.WeakReference;

public class MyThreadLocalMap {


    static class Entry extends WeakReference<MyThreadLocal> {
        Object value;

        Entry(MyThreadLocal k, Object v) {
            super(k);
            value = v;
        }
    }


    private static final int INITIAL_CAPACITY = 16;


    private Entry[] table;


    private int size = 0;


    private int threshold; // Default to 0

    private void setThreshold(int len) {
        threshold = len * 2 / 3;
    }


    private static int nextIndex(int i, int len) {
        int index = (i + 1 < len) ? i + 1 : 0;
        System.out.println("往后查找===" + index);
        return index;
    }


    private static int prevIndex(int i, int len) {
        int index = (i - 1 >= 0) ? i - 1 : len - 1;
        System.out.println("往前查找===" + index);
        return index;
    }


    MyThreadLocalMap(MyThreadLocal firstKey, Object firstValue) {
        table = new Entry[INITIAL_CAPACITY];
        int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
        table[i] = new Entry(firstKey, firstValue);
        size = 1;
        setThreshold(INITIAL_CAPACITY);
    }


    public Entry getEntry(MyThreadLocal<?> key) {
        int i = key.threadLocalHashCode & (table.length - 1);
        Entry e = table[i];
        // Android-changed: Use refersTo()
        if (e != null && e.get().equals(key))
            return e;
        else
            return getEntryAfterMiss(key, i, e);
    }


    private Entry getEntryAfterMiss(MyThreadLocal<?> key, int i, Entry e) {
        Entry[] tab = table;
        int len = tab.length;

        while (e != null) {
            // Android-changed: Use refersTo()
            if (e.get().equals(key))
                return e;
            if (e.get() == null)
                expungeStaleEntry(i);
            else
                i = nextIndex(i, len);
            e = tab[i];
        }
        return null;
    }


    public void set(MyThreadLocal<?> key, Object value) {


        Entry[] tab = table;
        int len = tab.length;
        int i = key.threadLocalHashCode & (len - 1);
        System.out.println("hash散列的数组下标为" + i);

        for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
            System.out.println("进到这个循环的e不为Null");
            MyThreadLocal<?> k = e.get();

            if (k == key) {
                e.value = value;
                return;
            }

            if (k == null) {
                System.out.println("replaceStaleEntry");
                replaceStaleEntry(key, value, i);
                return;
            }
        }
        System.out.println("查找到数组的下标为" + i);

        tab[i] = new Entry(key, value);
        System.out.println("方法set==位置第" + i + "位置" + "元素===value===" + value + "======");
        int sz = ++size;
        if (!cleanSomeSlots(i, sz) && sz >= threshold) {
            System.out.println("需要rehash");
            rehash();
        } else {
            System.out.println("不需要rehash--也不需要扩容");
        }
        System.out.println("方法set==位置第" + i + "位置" + "元素===value===" + value + "======");
    }


    public void remove(MyThreadLocal<?> key) {
        System.out.printf("remove");
        Entry[] tab = table;
        int len = tab.length;
        int i = key.threadLocalHashCode & (len - 1);
        for (Entry e = tab[i];
             e != null;
             e = tab[i = nextIndex(i, len)]) {
            if (e.get() == key) {
                e.clear();
                expungeStaleEntry(i);
                return;
            }
        }
    }


    private void replaceStaleEntry(MyThreadLocal<?> key, Object value, int staleSlot) {
        System.out.println("replaceStaleEntry");
        Entry[] tab = table;
        int len = tab.length;
        Entry e;

        int slotToExpunge = staleSlot;
        for (int i = prevIndex(staleSlot, len);
             (e = tab[i]) != null;
             i = prevIndex(i, len))
            if (e.get() == null)
                slotToExpunge = i;


        for (int i = nextIndex(staleSlot, len);
             (e = tab[i]) != null;
             i = nextIndex(i, len)) {
            MyThreadLocal<?> k = e.get();

            if (k == key) {
                e.value = value;

                tab[i] = tab[staleSlot];
                tab[staleSlot] = e;

                if (slotToExpunge == staleSlot)
                    slotToExpunge = i;
                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                return;
            }


            if (k == null && slotToExpunge == staleSlot)
                slotToExpunge = i;
        }

        tab[staleSlot].value = null;
        tab[staleSlot] = new Entry(key, value);

        if (slotToExpunge != staleSlot)
            cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
    }


    private int expungeStaleEntry(int staleSlot) {

        System.out.println("expungeStaleEntry");
        Entry[] tab = table;
        int len = tab.length;

        tab[staleSlot].value = null;
        tab[staleSlot] = null;
        size--;

        Entry e;
        int i;
        for (i = nextIndex(staleSlot, len);
             (e = tab[i]) != null;
             i = nextIndex(i, len)) {
            MyThreadLocal<?> k = e.get();
            if (k == null) {
                e.value = null;
                tab[i] = null;
                size--;
            } else {
                int h = k.threadLocalHashCode & (len - 1);
                if (h != i) {
                    tab[i] = null;

                    while (tab[h] != null)
                        h = nextIndex(h, len);
                    tab[h] = e;
                }
            }
        }
        return i;
    }

    private boolean cleanSomeSlots(int i, int n) {
        System.out.println("cleanSomeSlots=====i===="+i+"==位置下标=n=="+n+"=====");
        boolean removed = false;
        Entry[] tab = table;
        int len = tab.length;
        do {
            i = nextIndex(i, len);
            Entry e = tab[i];
            if (e != null && e.get() == null) {
                System.out.println("查找到脏数据了");
                n = len;
                removed = true;
                i = expungeStaleEntry(i);
            } else {
                System.out.println("没有脏数据呀~");
            }
        } while ((n >>>= 1) != 0);
        //n >>>= 1 说明要循环log2N次。在没有发现脏Entry时，会一直往后找下个位置的entry是否是脏的，如果是的话，就会使 n = 数组的长度。然后继续循环log2新N 次。
        return removed;
    }


    private void rehash() {
        System.out.println("rehash");
        expungeStaleEntries();


        if (size >= threshold - threshold / 4)
            resize();
    }

    private void resize() {
        Entry[] oldTab = table;
        int oldLen = oldTab.length;
        int newLen = oldLen * 2;
        Entry[] newTab = new Entry[newLen];
        int count = 0;

        for (int j = 0; j < oldLen; ++j) {
            Entry e = oldTab[j];
            if (e != null) {
                MyThreadLocal<?> k = e.get();
                if (k == null) {
                    e.value = null; // Help the GC
                } else {
                    int h = k.threadLocalHashCode & (newLen - 1);
                    while (newTab[h] != null)
                        h = nextIndex(h, newLen);
                    newTab[h] = e;
                    count++;
                }
            }
        }

        setThreshold(newLen);
        size = count;
        table = newTab;
    }

    private void expungeStaleEntries() {
        System.out.println("expungeStaleEntries");
        Entry[] tab = table;
        int len = tab.length;
        for (int j = 0; j < len; j++) {
            Entry e = tab[j];
            if (e != null && e.get() == null)
                expungeStaleEntry(j);
        }
    }
}

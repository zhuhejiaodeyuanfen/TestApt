package com.example.testapt;

public class Test {

    private static final int HASH_INCREMENT = 0x61c88647;
    public static void main(String[] args) {
        magicHash(16); //初始大小16
        magicHash(32); //扩容一倍
    }

    private static void magicHash(int size){
        int hashCode = 0;
        for(int i=0;i<size;i++){
            hashCode = i*HASH_INCREMENT+HASH_INCREMENT;
            System.out.print((hashCode & (size-1))+" ");
        }
        System.out.println();
    }
}

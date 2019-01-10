package com.esbook.thread.volatiletest;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Singleton {
    private static Singleton instance = null;
    private int a;
    private Object[] obj;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

    public int getA() {
        return a;
    }

    public Object[] getObj() {
        return obj;
    }
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);

        } catch (Exception e) {
        }
        return null;
    }



    public static void main(String[] args) throws InterruptedException {
        int nThreads = 2;
        Unsafe unsafe = getUnsafe();
        System.out.println("unafe ");
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        for(int i=0;i<nThreads;i++){
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    //System.out.println("n"+ unsafe.getObject(singleton,));
                    Singleton singleton = Singleton.getInstance();
                    System.out.println("nafter  "+singleton.getA());
                }
            });
        }
        executorService.shutdown();

        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            Thread.sleep(100);
        }

    }

    public void testPrint(){
    }



}

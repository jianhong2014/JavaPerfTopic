package com.perf.thread.volatiletest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Publisher {

    private static volatile Sample  sample = new Sample();

    private static int a = 0;
    private static volatile boolean flag = false;

    public static void main(String[] args) throws InterruptedException {
        testVisibility();
        //testReOrder();
    }

    public static void testReOrder() throws InterruptedException {
        for(int i=0;i<10000;i++){
            testReOrderOnce();
        }
    }

    public static void testReOrderOnce() throws InterruptedException {
        //volatile Sample  sample = new Sample();
       // System.out.println("start test");
        //sample.setA(0);
        //sample.setFlag(false);
        a =0;
        flag=false;
        int nThreads = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        Runnable getTask = new Runnable() {
            @Override
            public void run() {
                if(flag){
                    a=a+2;
                   // sample.setA(a);
                }
               // System.out.println("reorder  "+sample.isFlag()+","+sample.getA());
               // boolean test1 = sample.getA() == 0;
               // boolean test2 = sample.getA() == 0;
                boolean tf = flag;
                int ta = a;
                System.out.println("reorder "+tf+","+ta);
                if(a == 0 ){
                   // System.out.println("reorder A "+sample.getA());
                   // System.out.println("reorder "+flag+","+a);
                    //A还是0，如果另外一个线程是按照顺序执行的，说明flag应该还是false
                    //如果flag为true，且没有重新排序，那么A应该是1.
                    //System.out.println("reorder happens "+sample.getA());
                    /*if(sample.isFlag()){
                        System.out.println("reorder happens "+sample.getA());
                        //System.out.println("reorder A "+sample.getA());
                        System.exit(1);
                    }*/
                }

            };
        };

        Runnable setTask = new Runnable() {
            @Override
            public void run() {
                //for(long i=0;i<10000000;i++);
                //sample.setA(1);
              //  AtomicLong at = new AtomicLong(10000000000l);
                a = 1 ;
                flag = true;
                //System.out.println("set A "+sample.getA());
                //sample.setFlag(true);
                //System.out.println("set  flag "+sample.isFlag());
            }
        };
        executorService.submit(setTask);
        executorService.submit(getTask);
        executorService.shutdown();

        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            Thread.sleep(100);
        }
    }

    /**
     * 测试多线程环境下，多线程共享对象的可见性
     * @throws InterruptedException
     */

    public static void testVisibility() throws InterruptedException {
        SharedObject sharedObject = new SharedObject();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for(int i=0;i<1;i++){
            Thread task = new Thread(new Runnable() {
                @Override
                public void run() {
                    long count = 0;
                    //如果没收到指令，就睡一会
                    while(!sharedObject.isRunning()){
                        //下面这句执行比较快，会导致主存数据来不及fetch，所以不可见
                        //如果换做一个耗时的语句，就概率比较大不会出现这个问题了
                        //System.out.println("hello");
                        count++;

                    }
                    System.out.println("收到指令，开始点火 ");
                }
            });
            executorService.submit(task);
        }
        Thread leader = new Thread(new Runnable() {
            @Override
            public void run() {
                long count = 300000000l;
                for(long i=0;i<count;i++){
                }
                sharedObject.setRunning(true);
                System.out.println("吉时已到,领导发出点火指令 ");
            }
        });
        executorService.submit(leader);
        executorService.shutdown();

        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            Thread.sleep(100);
        }
    }


}

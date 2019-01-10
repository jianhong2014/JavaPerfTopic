package com.esbook.thread.volatiletest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.*;

public class Test {

    public static void main(final String[] args) throws InterruptedException {
        //testVolatileAtom();
        //testVolatileStatus();
        testCocurrentPerf();
    }

    //测试可见性，原子性问题代码
    private static void testVolatileAtom() throws InterruptedException {
        SyncResource syncResource = new SyncResource();
        Thread thread1 = new Thread(new VolatitleJob(syncResource, 10));
        Thread thread2 = new Thread(new VolatitleJob(syncResource, 10));
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();


    }

    //场景状态依volatile的写-读建立的happens before关系
    private static void testVolatileStatus() throws InterruptedException {
        SyncResource syncResource = new SyncResource();
        syncResource.setRunningStatus(true);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread 1 is running");
                while (syncResource.isRunningStatus()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("thread 1 is stopped");
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread 2 is running");
                while (syncResource.isRunningStatus()) {
                    try {
                        Thread.sleep(100);
                        long optCount = syncResource.incOptCount();
                        if(optCount == 20){
                            syncResource.setRunningStatus(false);
                            System.out.println("thread 1,2 is to be stopped");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("thread 2 is stopped");
            }
        });
      thread1.start();
      thread2.start();
      thread1.join();
      thread2.join();
    }

    private static void testCocurrentPerf() throws InterruptedException {
        int testTimes = 20;
        for(int i=0;i<testTimes;i++){
            System.out.println("第"+i+"轮性能对比测试开始，");
            long syncPer = testSyncePerf();
            long rwLockPerf = testReadWritePerf();
            long casPerf = testCasPerf();
            System.out.println("第"+i+"轮性能对比测试结果: Synchronized性能:"+syncPer+",volatile读写锁:"+rwLockPerf+",CAS无锁性能:"+casPerf);
        }

    }

    private static long testCasPerf() throws InterruptedException {
        Function<SyncResource,Long> rwLockGetter=(syncSrc) -> syncSrc.getSyncCount();
        BiConsumer<SyncResource,Long> rwLockSetter=(syncSrc, data) -> syncSrc.incSyncCount();
        return testPerf(rwLockGetter,rwLockSetter);
    }

    private static long testReadWritePerf() throws InterruptedException {
        Function<SyncResource,Long> rwLockGetter=(syncSrc) -> syncSrc.getReadWriteData();
        BiConsumer<SyncResource,Long> rwLockSetter=(syncSrc, data) -> syncSrc.setReadWriteData(data);
        return testPerf(rwLockGetter,rwLockSetter);
    }

    private static long testSyncePerf() throws InterruptedException {
        Function<SyncResource,Long> rwSyncGetter=(syncSrc) -> syncSrc.getSyncRwData();
        BiConsumer<SyncResource,Long> rwSyncSetter=(syncSrc, data) -> syncSrc.setSyncRwData(data);
        return testPerf(rwSyncGetter,rwSyncSetter);
    }

    //任务比较短时候，sync比较短，自选锁就可以了，一旦load导致任务长时间占锁，性能对比就明显了
    private static long testPerf(Function<SyncResource,Long> getter,BiConsumer<SyncResource,Long> setter) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        SyncResource syncResource = new SyncResource();
        syncResource.setRunningStatus(true);
        long load = 800 * 10000;
        long startTime = System.currentTimeMillis();
        int nReadTask = 7;
        for(int i=0;i<nReadTask;i++){
            executorService.submit(new ReadTask(syncResource,load,getter));

        }
        executorService.submit(new WriteTask(syncResource,load,setter));
       //启动计算job
        syncResource.setRunningStatus(true);
        executorService.shutdown();

        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            Thread.sleep(100);
        }
        return System.currentTimeMillis() -startTime;
    }
}

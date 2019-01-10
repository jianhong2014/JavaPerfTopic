package com.perf.thread.falsesharing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {

    /**
     * 伪共享的性能测试
     *
     * @param args
     * @throws InterruptedException
     */

    public static void main(final String[] args) throws InterruptedException {
        int numThreads = 4;
        long writeCount = 100 * 100 * 20000;
        int testTimes = 10;

        List<Long> goodPerfPaddingData = new ArrayList<>(testTimes);
        List<Long> goodPerfContendData = new ArrayList<>(testTimes);
        List<Long> goodPerfLockData = new ArrayList<>(testTimes);
        List<Long> badPerfData = new ArrayList<>(testTimes);
        System.out.println("开始伪共享性能对比测试：");
        for(int i=0;i<testTimes;i++){
            long goodPadding = goodPerfPadding(numThreads,writeCount);
            long goodConend = goodPerfContended(numThreads,writeCount);
            long bad = badPerfTest(numThreads,writeCount);
            long lockPerf =   syncPerfTest(numThreads,writeCount);
            goodPerfPaddingData.add(goodPadding);
            goodPerfContendData.add(goodConend);
            goodPerfLockData.add(lockPerf);
            badPerfData.add(bad);
            System.out.println("第"+(i+1)+"测试，测试结果如下：");
            System.out.println("没有伪共享共享(padding方案)时间耗时:"+goodPadding);
            System.out.println("没有伪共享共享(java8 Contend方案)时间耗时:"+goodConend);
            //System.out.println("没有伪共享共享(lock方案)时间耗时:"+lockPerf);
            System.out.println("伪共享时间耗时:"+bad+"ms");
        }

       // long goodPerfCost = goodPerfPadding(numThreads,writeCount);
        //long badPerfCost = badPerfTest(numThreads,writeCount);
        System.out.println("总体测试结果如下：");
        System.out.println("没有false sharing(padding方案)多线程耗时：" + goodPerfPaddingData);
        System.out.println("没有false sharing(java8 Contend方案)多线程耗时：" + goodPerfContendData);
      //  System.out.println("没有false sharing(synchronized方案)多线程耗时：" + goodPerfLockData);
        System.out.println("有false shaing多线程耗时：" + badPerfData);
    }

    private static long goodPerfPadding(int numThreads, long writeCount) throws InterruptedException {
        long start = System.currentTimeMillis();
        long sum = 0;
        //padding 没有false sharing的临界资源
        VolatileNoShaingObjWithPadding[] noSharingObjs = new VolatileNoShaingObjWithPadding[numThreads];
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            noSharingObjs[i] = new VolatileNoShaingObjWithPadding();
        }
        for (int i = 0; i < numThreads; i++) {
            executorService.submit(new NoFalseSharigPaddingJob(i, noSharingObjs,writeCount));
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            //System.out.println("current "+executorService.);
            Thread.sleep(100);
        }
        long goodPerfCost = System.currentTimeMillis() - start;
        return goodPerfCost;
    }

    private static long badPerfTest(int numThreads,long writeCount) throws InterruptedException {
        long start = System.currentTimeMillis();
        long sum = 0;
        //padding 有false sharing的临界资源
        VolatileShaingObj[] sharingObjs = new VolatileShaingObj[numThreads];
        for (int i = 0; i < numThreads; i++) {
            sharingObjs[i] = new VolatileShaingObj();
        }
        ExecutorService badExecutorService = Executors.newFixedThreadPool(numThreads);
        start = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            badExecutorService.submit(new FalseSharigJob(i, sharingObjs,writeCount));
        }
        badExecutorService.shutdown();
        while (!badExecutorService.awaitTermination(2, TimeUnit.SECONDS)) {
            //System.out.println("current "+executorService.);
            Thread.sleep(100);
        }
        long badPerfCost = System.currentTimeMillis() - start;
        return badPerfCost;
    }

    private static long goodPerfContended(int numThreads,long writeCount) throws InterruptedException{
        long start = System.currentTimeMillis();
        long sum = 0;
        //padding 没有false sharing的临界资源
        VolatileNoShaingObjWithContend[] noSharingObjs = new VolatileNoShaingObjWithContend[numThreads];
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            noSharingObjs[i] = new VolatileNoShaingObjWithContend();
        }
        for (int i = 0; i < numThreads; i++) {
            executorService.submit(new NoFalseSharigContendJob(i, noSharingObjs,writeCount));
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(2, TimeUnit.SECONDS)) {
            //System.out.println("current "+executorService.);
            Thread.sleep(100);
        }
        long goodPerfCost = System.currentTimeMillis() - start;
        return goodPerfCost;
    }

    private static long syncPerfTest(int numThreads,long writeCount) throws InterruptedException {
        long start = System.currentTimeMillis();
        long sum = 0;
        //padding 有false sharing的临界资源
        SyncNoSharingObject[] sharingObjs = new SyncNoSharingObject[numThreads];
        for (int i = 0; i < numThreads; i++) {
            sharingObjs[i] = new SyncNoSharingObject();
        }
        ExecutorService badExecutorService = Executors.newFixedThreadPool(numThreads);
        start = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            badExecutorService.submit(new SyncNoSharingJob(i, sharingObjs,writeCount));
        }
        badExecutorService.shutdown();
        while (!badExecutorService.awaitTermination(2, TimeUnit.SECONDS)) {
            //System.out.println("current "+executorService.);
            Thread.sleep(100);
        }
        long badPerfCost = System.currentTimeMillis() - start;
        return badPerfCost;
    }
}

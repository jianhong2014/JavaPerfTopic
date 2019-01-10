package com.esbook.thread.volatiletest;

import java.util.concurrent.atomic.AtomicLong;

public class SyncResource {

    private volatile long optCount = 0;

    private volatile boolean runningStatus = false;

    private AtomicLong syncCount = new AtomicLong(0);

    //读写分离，读多写少的时候，不互斥
    private volatile long readWriteData = 0;
    public long getReadWriteData(){
        return readWriteData;
    }
    public synchronized void setReadWriteData(long newData){
        readWriteData = newData;
    }

    //sync ，读写都是互斥的
    private long syncRwData;
    public  synchronized long getSyncRwData(){
        return syncRwData;
    }
    public synchronized void setSyncRwData(long data){
        syncRwData = data;
    }

    public long incOptCount() {
        //step1,read from 主内存
        long tmp = optCount;
        //step2 局部变量加1
        tmp = tmp+1;
        doOtherWork();
        //step3 assign 并写回主内存
        optCount = tmp;
        return tmp;
    }
    private void doOtherWork(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long incSyncCount(){
        return syncCount.incrementAndGet();
    }

    public long getSyncCount() {
        return syncCount.get();
    }

    public boolean isRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(boolean runningStatus) {
        this.runningStatus = runningStatus;
    }

    public long getOptCount() {
        return optCount;
    }
}

package com.esbook.thread.volatiletest;

import java.util.function.Function;

public class ReadTask implements Runnable {

    private SyncResource syncResource;
    private long load;
    private Function<SyncResource,Long> dataGetter;

    public ReadTask(SyncResource syncResource,long workLoad,Function<SyncResource,Long> getter){
           this.syncResource = syncResource;
           this.load = workLoad;
           this.dataGetter = getter;
    }

    @Override
    public void run() {
        //System.out.println("start read task");
        while(!syncResource.isRunningStatus()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long start = System.currentTimeMillis();
        long data = dataGetter.apply(syncResource);
        while (data < load) {
            data = dataGetter.apply(syncResource);
        }
        long duration = System.currentTimeMillis() - start;
         //System.out.println("read task duration "+duration);
    }
}

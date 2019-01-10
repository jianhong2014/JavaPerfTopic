package com.perf.thread.volatiletest;

import java.util.function.BiConsumer;

//read more and write less
public class WriteTask  implements Runnable{
    private SyncResource syncResource;
    private long load;
    private BiConsumer<SyncResource,Long> dataSetter;

    public WriteTask(SyncResource syncResource, long workLoad, BiConsumer<SyncResource,Long> setter){
        this.syncResource = syncResource;
        this.load = workLoad;
        this.dataSetter = setter;
    }

    @Override
    public void run() {
        //System.out.println("start write task");
        while(!syncResource.isRunningStatus()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<load+2;i++){
             dataSetter.accept(syncResource, (long) i);
        }
        //System.out.println("write task ended");
    }
}

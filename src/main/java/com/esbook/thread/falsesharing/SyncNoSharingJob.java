package com.esbook.thread.falsesharing;

public class SyncNoSharingJob implements Runnable{
    private int modifyIdx =0;
    //多线程共享的临界资源
    private SyncNoSharingObject[] sharingObjs;

    private long writeCount= 0;

    public SyncNoSharingJob(int arrayIndex, SyncNoSharingObject[] sharing, long writeCount) {
        this.modifyIdx = arrayIndex;
        this.sharingObjs = sharing;
        this.writeCount = writeCount;
    }

    public void run() {
        Thread.currentThread().setName("SyncNoSharingObject "+modifyIdx);
        for (int i=0;i<writeCount;i++) {
            sharingObjs[modifyIdx].setValue(i);;
        }
        //System.out.println("thread "+Thread.currentThread().getName()+" test finished  ");
    }
}

package com.perf.thread.falsesharing;

public class NoFalseSharigPaddingJob implements Runnable{
    //此job要修改自己的index，如果数组里面的每一个元素都占用一行cacheline
    //不会引起伪共享问题，否则会引起false sharing
    private int modifyIdx =0;
    //多线程共享的临界资源
    private VolatileNoShaingObjWithPadding[] sharingObjs;

    private long writeCount= 0;

    public NoFalseSharigPaddingJob(int arrayIndex, VolatileNoShaingObjWithPadding[] sharing, long writeCount) {
        this.modifyIdx = arrayIndex;
        this.sharingObjs = sharing;
        this.writeCount = writeCount;
    }

    public void run() {
        Thread.currentThread().setName("NoFalseSharigPaddingJob "+modifyIdx);
        for (int i=0;i<writeCount;i++) {
            sharingObjs[modifyIdx].value = i;
        }
        //System.out.println("thread "+Thread.currentThread().getName()+" test finished  ");
    }
}

package com.esbook.thread.falsesharing;

public class NoFalseSharigContendJob implements Runnable{
    //此job要修改自己的index，如果数组里面的每一个元素都占用一行cacheline
    //不会引起伪共享问题，否则会引起false sharing
    private int modifyIdx =0;
    //多线程共享的临界资源
    private VolatileNoShaingObjWithContend[] sharingObjs;

    private long writeCount= 0;

    public NoFalseSharigContendJob(int arrayIndex, VolatileNoShaingObjWithContend[] sharing, long writeCount) {
        this.modifyIdx = arrayIndex;
        this.sharingObjs = sharing;
        this.writeCount = writeCount;
    }

    //每一个线程都只更新自己的数组元素，不和其他线程竞争写
    public void run() {
        Thread.currentThread().setName("VolatileNoShaingObjWithContend Job "+modifyIdx);
        for (int i=0;i<writeCount;i++) {
            sharingObjs[modifyIdx].value = i;
        }
        //System.out.println("thread "+Thread.currentThread().getName()+" test finished  ");
    }
}

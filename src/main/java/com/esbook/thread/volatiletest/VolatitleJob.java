package com.esbook.thread.volatiletest;

public class VolatitleJob implements Runnable {

    private SyncResource syncResource;
    private long workLoad = 100;

    public VolatitleJob(SyncResource syncResource, long load) {
        this.syncResource = syncResource;
        this.workLoad = load;
    }

    //因为volatile不能保证原子性，所以多线程环境下
    //syncResource里面optCount可能相互覆盖
    private void volatitleReadWriteWork() {
        long count = syncResource.incOptCount();
        System.out.println(Thread.currentThread().getName() + " updated opt count " + count);
    }

    private void syncVolatileReadWriteWork() {
        synchronized (syncResource) {
            volatitleReadWriteWork();
        }
    }
    private void twoStepReadWriteWork() {
        try {
            Thread.sleep(workLoad);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //下面两句，其中每一句是原子的，但是两句就不是原子性了,如何保证原子呢？两种思路：合并成一句原子操作，或者用lock
        syncResource.incSyncCount();
        long updateSyncCount = syncResource.getSyncCount();

        System.out.println(Thread.currentThread().getName() + " updated sync count " + updateSyncCount);
    }

    private void singleReadWriteWork() {
        try {
            Thread.sleep(workLoad);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //下面这一句读写是原子的，
        long updateSyncCount = syncResource.incSyncCount();
        System.out.println(Thread.currentThread().getName() + " updated sync count " + updateSyncCount);
        //System.out.println(Thread.currentThread().getName()+" updated sync count "+syncResource.getSyncCount());
    }


    /**
     * 解决volatile原子性的问题，用CAS或者LOCK方式来实现
     */
    @Override
    public void run() {
        //volatile写操作，无法保证原子性
       // volatitleReadWriteWork();
        //volatile写操作，lock版本，可以保证
        //syncVolatileReadWriteWork();
        //对于ATOM类型操作而言，两句中的每一句是原子，但是整个方法不是原子的
        //twoStepReadWriteWork();
        //合并两句成一句原子操作指令
        //singleReadWriteWork();

    }
}

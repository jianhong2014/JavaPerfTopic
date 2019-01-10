package com.perf.thread.falsesharing;

public class SyncNoSharingObject {
    private  long value = 0L;

    public synchronized void setValue(long data){
        this.value = data;
    }
}

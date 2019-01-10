package com.esbook.thread.volatiletest;

public class SharedObject {
    //成员变量控制线程是否干活
    private   boolean isRunning = false;
    public boolean isRunning() {
        return isRunning;
    }
    public void setRunning(boolean running) {
        isRunning = running;
    }
}

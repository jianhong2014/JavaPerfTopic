package com.perf.thread.falsesharing;


//代价就是牺牲了空间，换时间
//把padding放在基类里面，可以避免优化,能否自动填充
public class VolatileNoShaingObjWithPadding {
    public volatile long value = 0L;
    //如果不被copy，有可能被java7优化掉，没有验证java7的方案
    public long[] padding = {0,0,0,0,0,0,0};
}

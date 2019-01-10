package com.esbook.thread.falsesharing;


import sun.misc.Contended;

//必须加上虚拟机参数-XX:-RestrictContended
//java9里面已经不支持使用，
@Contended
public class VolatileNoShaingObjWithContend {
    public volatile long value = 0L;
}

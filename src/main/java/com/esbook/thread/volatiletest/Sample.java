package com.esbook.thread.volatiletest;

public class Sample {
    private   int a;
    //private long[] ar = {1,2,3,4,5,6,7,8};
    private  boolean flag;

    private Object[] obj;

    public Sample(){
        a = 0;
        obj = new String[10000000];
        flag = true;
    }
    public  int getA() {
        return a;
    }

    public void incA(){
        a = a+1;
    }
    public  void setA(int a) {
        this.a = a;
    }

    public  boolean isFlag() {
        return flag;
    }

    public   void setFlag(boolean flag) {
        this.flag = flag;
    }

    private void doWork(){
        long count = 10000000000l;
        for(long i=0;i<count;i++);
    }


}

package com.esbook.jna;

import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * Elasticsearch通过jna用到了一些系统调用，
 */
public class DemoJna {
    static {
        System.out.println("register " + Platform.C_LIBRARY_NAME);
        Native.register(Platform.C_LIBRARY_NAME);
    }

   // static native int mlockall(int flags);

    static native int _getpid();

    public static void main(String[] args) {
        System.out.println("get uid " + _getpid());
        boolean test = true;
        while(test){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

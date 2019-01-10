package com.perf.esprobe;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/**
 * 获得进程cpu负载情况
 */
public class GetProcessCpuLoad {
    public static void main(String[] argv) throws Exception {
        OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean)
                ManagementFactory.getOperatingSystemMXBean();
        double load;
        for(int i=0; i<10; i++) {
            load = ((com.sun.management.OperatingSystemMXBean) mbean).getProcessCpuLoad();
            System.out.println("cpu load "+load);
            if((load<0.0 || load>1.0) && load != -1.0) {
                throw new RuntimeException("getProcessCpuLoad() returns " + load
                        +   " which is not in the [0.0,1.0] interval");
            }
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

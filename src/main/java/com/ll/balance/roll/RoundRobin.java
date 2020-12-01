package com.ll.balance.roll;

import com.ll.balance.ServerIp;

import java.util.stream.IntStream;

/**
 * 轮询算法
 */
public class RoundRobin {
    private static Integer pos = 0;

    public static String getServer() {
        String ip = null;
        synchronized (pos) {
            if (pos >= ServerIp.LIST.size()) {
                pos = 0;
            }
            ip = ServerIp.LIST.get(pos);
            pos++;
        }

        return ip;
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(0, 100).forEach(ele -> System.out.println(getServer()));
    }

}

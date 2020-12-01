package com.ll.balance;

import java.util.*;

public class ServerIp {
    public static final List<String> LIST = Arrays.asList(
            "192.168.222.1",
            "192.168.222.2",
            "192.168.222.3",
            "192.168.222.4",
            "192.168.222.5",
            "192.168.222.6",
            "192.168.222.7",
            "192.168.222.8",
            "192.168.222.9",
            "192.168.222.10"
    );

    public static final Map<String, Integer> LIST_WEIGHT = new LinkedHashMap<>();
    static {
        // 权重之和为50
        LIST_WEIGHT.put("192.168.222.1", 5);
        LIST_WEIGHT.put("192.168.222.2", 3);
        LIST_WEIGHT.put("192.168.222.3", 2);
//        LIST_WEIGHT.put("192.168.222.4", 4);
//        LIST_WEIGHT.put("192.168.222.5", 5);
//        LIST_WEIGHT.put("192.168.222.6", 6);
//        LIST_WEIGHT.put("192.168.222.7", 7);
//        LIST_WEIGHT.put("192.168.222.8", 8);
//        LIST_WEIGHT.put("192.168.222.9", 9);
//        LIST_WEIGHT.put("192.168.222.10", 100);
    }
}

package com.ll.balance.random;

import com.ll.balance.ServerIp;

import java.util.stream.IntStream;

public class WeightRandomV2 {

    public static String getServer() {
        // 生成一个随机数作为List的下标值
        int totalWeight = 0;
        for (Integer weight : ServerIp.LIST_WEIGHT.values()) {
            totalWeight += weight;
        }

        java.util.Random random = new java.util.Random();
        int rand = random.nextInt(totalWeight);

        // 当所有机器的权重都一样的是后这一步可以优化
        for (String ip : ServerIp.LIST_WEIGHT.keySet()) {
            Integer weight = ServerIp.LIST_WEIGHT.get(ip);
            if(rand < weight) {
                return ip;
            }

            rand -= weight;
        }
        return "";
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(0, 100).forEach(ele -> System.out.println(getServer()));
    }

}

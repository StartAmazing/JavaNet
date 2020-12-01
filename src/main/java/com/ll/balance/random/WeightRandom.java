package com.ll.balance.random;

import com.ll.balance.ServerIp;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 第一种思路，根据权重大小放在一个List中，权重越大，List中的复制也就越多
 */
public class WeightRandom {

    public static String getServer() {
        // 生成一个随机数作为List的下标值
        List<String> ips = new ArrayList<>();
        for (String ip : ServerIp.LIST_WEIGHT.keySet()) {
            Integer weight = ServerIp.LIST_WEIGHT.get(ip);
            // 按照权重进行复制，内存效率低
            for(int i = 0; i < weight; i++) {
                ips.add(ip);
            }
        }

        java.util.Random random = new java.util.Random();
        int rand = random.nextInt(ips.size());
        return ips.get(rand);
    }

    public static void main(String[] args) {
        IntStream.rangeClosed(0, 100).forEach(ele -> System.out.println(getServer()));
    }

}

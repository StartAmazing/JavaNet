package com.ll.balance.random;

import com.ll.balance.ServerIp;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 负载均衡 - 随机算法
 * 缺点：
 *     没有办法区分服务器之间的性能好坏，即权重
 */
public class Random {
    public static String getServer() {
        java.util.Random random = new java.util.Random();
        int rand = random.nextInt(ServerIp.LIST.size());
        return ServerIp.LIST.get(rand);
    }


    public static void main(String[] args) {
        IntStream.rangeClosed(0, 100).forEach(ele -> System.out.println(getServer()));
    }
}

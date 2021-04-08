package com.ll.io.msb.nio.simpleNetty;

public class MainThread {
    public static void main(String[] args) {
        // 这里不做关于IO和业务的事情

        // 1. 创建IO Thread(一个或多个)
        SelectorThreadGroup boss = new SelectorThreadGroup(3);
        SelectorThreadGroup worker = new SelectorThreadGroup(3);
        boss.setWorker(worker);
//        SelectorThreadGroup selectorThreadGroup = new SelectorThreadGroup(3);  // 混杂模式，只有一个线程负责accept，每个都会被分配client进行读写
        // 2. 应该把监听的Server注册到某一个selector上？
        boss.bind(9999);
    }
}

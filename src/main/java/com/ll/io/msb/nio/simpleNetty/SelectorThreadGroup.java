package com.ll.io.msb.nio.simpleNetty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {

    int threadNum = 0; // 线程数量
    SelectorThread[] threads;
    ServerSocketChannel serverChannel = null;
    AtomicInteger xid = new AtomicInteger(0);

    SelectorThreadGroup stg = this;
    public void setWorker(SelectorThreadGroup stg) {
        this.stg = stg;
    }

    public SelectorThreadGroup(int threadNum) {
        this.threadNum = threadNum;
        threads = new SelectorThread[threadNum];
        for (int i = 0; i < threadNum; i++) {
            threads[i] = new SelectorThread(this);
            new Thread(threads[i]).start();
        }
    }

    public void bind(int port) {
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.bind(new InetSocketAddress(port));

            // 注册到哪个selector上呢？
            nextSelector(serverChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // 无论当前的管道是ServerSocketChannel或是SocketChannel都复用这个方法
    public void nextSelector(Channel channel) {
        try {
            if (channel instanceof ServerSocketChannel) {
                SelectorThread st = nextSelectorIndex();
                st.channelQueue.put(channel);
                st.setWorker(stg);
                st.selector.wakeup();
            } else {
                SelectorThread st = nextSelectorIndexV3();
                st.channelQueue.put(channel);
                st.selector.wakeup();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        SelectorThread curSelector = nextSelectorIndex();
//        // 重点： c有可能是server, 有可能是client
//        // 1. 通过队列传递数据/消息
//        curSelector.channelQueue.add(channel);
//        // 2. 通过打断阻塞，让对应的线程去自己在打断后完成注册selector
//        curSelector.selector.wakeup();
    }


    private SelectorThread nextSelectorIndex() {
        int index = xid.incrementAndGet() % threads.length;
        return threads[index];
    }

    private SelectorThread nextSelectorIndexV3() {
        int index = xid.incrementAndGet() % stg.threads.length;
        return stg.threads[index];
    }
}

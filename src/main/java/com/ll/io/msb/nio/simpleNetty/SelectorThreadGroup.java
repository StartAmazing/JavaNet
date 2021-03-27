package com.ll.io.msb.nio.simpleNetty;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectorThreadGroup {

    int threadNum = 0; // 线程数量
    SelectorThread[] threads;
    ServerSocketChannel serverChannel = null;
    AtomicInteger xid = new AtomicInteger(0);

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
        SelectorThread curSelector = nextSelectorIndex();
        // 重点： c有可能是server, 有可能是client
//        ServerSocketChannel server = (ServerSocketChannel) channel;
//        try {
//            curSelector.selector.wakeup(); // 功能是让selector的select()方法立刻返回，不阻塞
//            server.register(curSelector.selector, SelectionKey.OP_ACCEPT);  // 呼应上
//        } catch (ClosedChannelException e) {
//            e.printStackTrace();
//        }
        // 1. 通过队列传递数据/消息
        curSelector.channelQueue.add(channel);
        // 2. 通过打断阻塞，让对应的线程去自己在打断后完成注册selector
        curSelector.selector.wakeup();
    }


    private SelectorThread nextSelectorIndex() {
        int index = xid.incrementAndGet() % threads.length;
        return threads[index];
    }
}

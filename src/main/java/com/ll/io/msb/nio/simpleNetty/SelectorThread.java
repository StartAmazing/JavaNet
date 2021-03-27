package com.ll.io.msb.nio.simpleNetty;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author LL
 * @create 2021/3/12
 *
 *
 * 没每个线程对应一个selector
 * 多线程情况下，该主机，该程序的并发客户端被分配到多个selector上
 * 注意，每个客户端，只绑定到其中一个selector
 * 其实不会有交互问题
 */
public class SelectorThread implements Runnable {

    Selector selector = null;
    LinkedBlockingDeque<Channel> channelQueue = new LinkedBlockingDeque<>();
    SelectorThreadGroup group;

    SelectorThread(SelectorThreadGroup group){
        this.group = group;
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public LinkedBlockingDeque<Channel> getChannelQueue() {
        return channelQueue;
    }

    public void setChannelQueue(LinkedBlockingDeque<Channel> channelQueue) {
        this.channelQueue = channelQueue;
    }

    @Override
    public void run() {

        // Loop
        while (true) {
            try {
                // 1. select()
                int nums = selector.select();   // 阻塞 wakeup()
                // 2. 处理selectKeys
                if (nums > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {        // 线性处理
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if(key.isAcceptable()) {        // 复杂，就是接收客户端的过程（接收之后，要注册， 多线程模型下，新的客户端注册到哪个selector上？）
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        } else if (key.isWritable()) {

                        }
                    }
                }

                // 3. 处理一些task
                if(!channelQueue.isEmpty()) {
                    Channel channel = channelQueue.take();
                    if (channel instanceof ServerSocketChannel) {
                        ServerSocketChannel serverChannel = (ServerSocketChannel) channel;
                        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
                    } else if(channel instanceof SocketChannel) {
                        SocketChannel rwChannel = (SocketChannel) channel;
                        ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
                        rwChannel.register(selector, SelectionKey.OP_READ, buffer);
                    }
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void readHandler(SelectionKey key) {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        SocketChannel clientChannel = (SocketChannel)key.channel();
        buffer.clear();
        while (true) {
            try {
                int readBytes = clientChannel.read(buffer);
                if (readBytes > 0) {
                    buffer.flip();      // 将读到的内容反转，然后直接写出
                    while (buffer.hasRemaining()) {
                        clientChannel.write(buffer);
                    }
                    buffer.clear();
                } else if (readBytes == 0) {
                    break;
                } else {   // 客户端断开连接
                    System.out.println("client: " + clientChannel.getRemoteAddress() + " closed!");
                    key.cancel();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void acceptHandler(SelectionKey key) {
        ServerSocketChannel ServerChannel = (ServerSocketChannel) key.channel();
        try {
            SocketChannel clientChannel = ServerChannel.accept();
            clientChannel.configureBlocking(false);

            // 选择一个处理R/W的selector去注册
            group.nextSelector(clientChannel);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

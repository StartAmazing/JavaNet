package com.ll.io.msb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class SocketMultiplexingSingleThreadv1 {

    private ServerSocketChannel server = null;
    private Selector selector = null;   //linux多路复用器（select,poll,epoll,kqueue）  nginx event{}
    int port = 9090;

    /**
     * socket -> fd4
     * bind
     * listen fd4
     */
    public void initServer() {
        try {
            server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.bind(new InetSocketAddress(port));

            // 如果是epoll模型下，open ----> epoll_create() -> fd4
            selector = Selector.open();  //  select  poll  *epoll  优先选择：epoll 但是可以通过-D JVM修正

            // server 约等于listen状态的fd4
            /**
             * 如果
             * select, poll：jvm里 开辟一个数组fd5放进去,jvm自己会维护一个集合，用于发生事件时将fds传递个操作系统用于系统选择有状态的fd
             * epoll: epoll_ctl(fd4, ADD, fd5. EPOLLIN)
             */
            server.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        initServer();
        System.out.println("服务器启动了。。。。。");
        try {
            while (true) {  // 死循环

                Set<SelectionKey> keys = selector.keys();
                System.out.println(keys.size()+"   size");

                // 1. 调用多路复用器（select, poll or epoll）
                /**
                 * select()是啥意思：
                 * 1.对于select,poll 其实是内核的select(fd4)
                 * 2. 对于epoll: 其实是内核调用epoll_wait()
                 * 参数可以带时间：没有时间，0：阻塞，有时间则为超时时间
                 * select.wakeup()   结果容易返回0
                 */
                while (selector.select(500) > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();    // 返回的有状态的fd集合
                    Iterator<SelectionKey> iter = selectionKeys.iterator();

                    // 所以不管是什么多路复用器，只能给程序状态，还是得一个一个去处理他们的R/W。【同步！！！】
                    // NIO的时候，自己会对于每个fd调用系统调用，浪费资源，那么现在的select会调用一次select方法，就可以知道具体哪些fd有状态啊了
                    // socket  -> listen     通信      R/W
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        iter.remove();      // set 不移除会重复处理
                        if (key.isAcceptable()) {           // 建立连接
                            // 重点，如果要去接受一个新的连接
                            // 语义上，accept会返回新连接的FD
                            // 那新的FD会怎么办
                            // select,poll,因为他们内核没有空间，那么在jvm中就会保存和前边fd5那个listen的一起
                            // epoll: 我们希望通过epoll_ctl把新的客户端fd注册到内核空间中
                            acceptHandler(key);
                        } else if (key.isReadable()) {      // 读事件
                            readHandler(key);   // 在单线程情况下可能会阻塞
                            // IO THREADS
                            // redis 是不是用了epoll,redis有个 io threads的概念，redis是不是单线程的
                            // tomcat 8,9 异步处理方式，在io和处理上解耦
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void acceptHandler(SelectionKey key) {
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();  // 目的是为了调用accept接受客户端
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            client.register(selector, SelectionKey.OP_READ, buffer);
            System.out.println("-------------------------------------------");
            System.out.println("新客户端：" + client.getRemoteAddress());
            System.out.println("-------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readHandler(SelectionKey key) {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        client.write(buffer);
                    }
                    buffer.clear();
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SocketMultiplexingSingleThreadv1 service = new SocketMultiplexingSingleThreadv1();
        service.start();
    }
}

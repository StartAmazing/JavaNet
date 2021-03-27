package com.ll.io.msb.nio;

import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class SocketNIO {

    public static void main(String[] args) throws Exception {

        LinkedList<SocketChannel> clients = new LinkedList<>();

        ServerSocketChannel ss = ServerSocketChannel.open();
        ss.bind(new InetSocketAddress(9090));
        ss.configureBlocking(false); //重点  OS  NONBLOCKING!!!

        ss.setOption(StandardSocketOptions.TCP_NODELAY, false);
//        StandardSocketOptions.TCP_NODELAY
//        StandardSocketOptions.SO_KEEPALIVE
//        StandardSocketOptions.SO_LINGER
//        StandardSocketOptions.SO_RCVBUF
//        StandardSocketOptions.SO_SNDBUF
//        StandardSocketOptions.SO_REUSEADDR




        while (true) {
            Thread.sleep(1000);
            SocketChannel client = ss.accept(); //不会阻塞？  -1 / NULL
            //accept 调用内核了: 1. 没有客户端连接进来，返回值？在BIO的时候会被阻塞着，但是在NIO的时候内核返回-1，java客户端会返回NULL,
            // 如果有客户端的连接进入，accept返回的是这个客户端的fd,对应java程序返回socketChannel Object
            // NONBlocking 就是代码能够继续往下走，只不过有不同的情况

            if (client == null) {
                System.out.println("null.....");            // 没有客户端连接的情况
            } else {
                client.configureBlocking(false);            // 有新客户端连接进入的情况，加入集合，这个非阻塞指的是服务端等待客户端发送数据的时候非阻塞，即数据传输的非阻塞，而上一个非阻塞指的是连接建立的非阻塞，即服务器等待客户端与其建立连接的非阻塞
                int port = client.socket().getPort();
                System.out.println("client...port: " + port);
                clients.add(client);
            }

            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);  //可以在堆里   堆外

            for (SocketChannel c : clients) {   //串行化！！！！  多线程！！
                int num = c.read(buffer);  // >0  -1  0   //不会阻塞
                if (num > 0) {
                    buffer.flip();
                    byte[] aaa = new byte[buffer.limit()];
                    buffer.get(aaa);

                    String b = new String(aaa);
                    System.out.println(c.socket().getPort() + " : " + b);
                    buffer.clear();
                }


            }
        }
    }

}

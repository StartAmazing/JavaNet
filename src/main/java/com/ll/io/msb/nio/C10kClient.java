package com.ll.io.msb.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class C10kClient {
    public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        InetSocketAddress serverAddr = new InetSocketAddress("192.168.222.14",9090);

        for (int i = 10000; i < 65000; i ++) {
            try{
                SocketChannel client1 = SocketChannel.open();
                SocketChannel client2 = SocketChannel.open();
                client1.bind(new InetSocketAddress("192.168.222.1", i));
                client1.connect(serverAddr);
                boolean c1 = client1.isOpen();
                clients.add(client1);
//                client2.bind(new InetSocketAddress("192.168.0.102", i));
//                client2.connect(serverAddr);
//                boolean c2 = client1.isOpen();
//                clients.add(client2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("clients: " + clients.size());
    }
}

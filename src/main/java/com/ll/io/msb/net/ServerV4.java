package com.ll.io.msb.net;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class ServerV4 {
    ServerSocketChannel ssc;

    public void listen(int port) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(port));

        // Reactive / Reactor
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, ssc.validOps(), null);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 16);
        for (;;) {
            int numberOfKeys = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel channel = ssc.accept();
                    if (channel == null) {
                        continue;
                    }

                    // Kernel -> mmap(buffer) -> Channel -> User(Buffer)
                    channel.configureBlocking(false);
                    channel.register(selector, SelectionKey.OP_READ);
                } else {
                    SocketChannel channel = (SocketChannel)key.channel();
                    // _ _ _ _ _ _ _ _
                    //       P(position)
                    //       L
                    buffer.clear();
                    channel.read(buffer);
                    String request = new String(buffer.toString());
                    // Logic ...
                    buffer.clear();
                    buffer.put("HTTP/1.1 200 ok\n\nHello NIO!".getBytes(StandardCharsets.UTF_8));
                    // H T T P / 1 . 1 ... ! _ _
                    //                       P
                    buffer.flip();
                    channel.close();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerV4 serverV4 = new ServerV4();
        serverV4.listen(8090);
    }

}

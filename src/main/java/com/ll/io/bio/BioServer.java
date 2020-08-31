package com.ll.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {

    private static int PORT = 8080;

    private static ServerSocket server;

    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        if (server != null) {
            return;
        }

        try {
            server = new ServerSocket(PORT);
            System.out.println("服务器启动成功，准备接受请求...");
            while (true) {
                // 阻塞
                Socket socket = server.accept();
                executorService.submit(new ServerHandler(socket));
            }
        } finally {
            if (server != null) {
                System.out.println("服务器关闭...");
                server.close();
                server = null;
            }
        }
    }
}

class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket _socket) {
        this.socket = _socket;
    }

    @Override
    public void run() {
        InputStream is = null;
        OutputStream os = null;
        try {
            byte[] recv = new byte[1024];
            is = socket.getInputStream();
            is.read(recv);
            System.out.println("服务器接收到消息: " + new String(recv));
            os = socket.getOutputStream();
            os.write("hello client".getBytes());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                is = null;
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                os = null;
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                socket = null;
            }
        }
    }
}

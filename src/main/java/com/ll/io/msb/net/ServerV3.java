package com.ll.io.msb.net;

import com.ll.io.msb.net.service.IHandlerInterface;
import com.ll.io.msb.net.service.Request;
import com.ll.io.msb.net.service.Response;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerV3 {
    ServerSocket serverSocket;
    IHandlerInterface httpHandler;

    public ServerV3(IHandlerInterface httpHandler) {
        this.httpHandler = httpHandler;
    }
    public void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            this.accept();
        }
    }

    private void accept() throws IOException{
        Socket socket = serverSocket.accept();
        new Thread(() -> {
            try {
                this.handler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void handler(Socket socket) throws IOException {
        Request request = new Request(socket);
        Response response = new Response(socket);
        this.httpHandler.Handler(request, response);
    }

    public static void main(String[] args) throws IOException {
        new ServerV3(((request, response) -> {
            request.getHeaders().forEach((k, v) -> {
                System.out.println(k + ": " + v);
            });
            response.send("<html><body><h1>Greetings!</h1></body></html>");
        })).listen(8080);
    }
}

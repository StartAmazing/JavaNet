package com.ll.io.msb.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Function;

public class ServerV2 {

    ServerSocket serverSocket;

    Function<String, String> handler;

    public ServerV2(Function<String, String> handler) {
        this.handler = handler;
    }


    //Pending Queue
    public void listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        while (true) {
            this.accept();
        }
    }

    public void accept() throws IOException{
        //blocking
        // Thread --> sleep --> Other Threads
        Socket socket = serverSocket.accept();
        System.out.println("A socket created. ");
        new Thread(() -> {
            try {
                this.handler(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handler(Socket socket) throws IOException{
        try {
            DataInputStream iptStrean = new DataInputStream(socket.getInputStream());
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(iptStrean));
            StringBuilder requestBuilder = new StringBuilder();
            String line = "";
            // readLine -> line end '\n'
            while (true) {
                line = bfReader.readLine();
                if (line == null || line.isEmpty()) {
                    break;
                }
                requestBuilder.append(line).append("\n");
            }

            String request = requestBuilder.toString();
            System.out.println(request);

            BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String response = this.handler.apply(request);
            bfWriter.write(response);
            bfWriter.flush();
            socket.close();
        } catch (SocketException sce) {
            sce.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        new ServerV2(req -> {
            try {
                Thread.sleep(10);
            }catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            return "HTTP/1.1 201 ok \n\n Hello World!\n";
        }).listen(8080);
    }
}

package com.ll.io.msb.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RowHttpServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);

        // main Thread
        while (true) {
            //blocking
            // Thread --> sleep --> Other Threads
            Socket socket = serverSocket.accept();
            System.out.println("A socket created. ");

            DataInputStream iptStrean = new DataInputStream(socket.getInputStream());
            BufferedReader bfReader = new BufferedReader(new InputStreamReader(iptStrean));
            StringBuilder requestBuilder = new StringBuilder();
            String line = "";
            // readLine -> line end '\n'
            while (!(line = bfReader.readLine()).isEmpty()) {
                requestBuilder.append(line).append("\n");
            }

            String request = requestBuilder.toString();
            System.out.println(request);

            BufferedWriter bfWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bfWriter.write("HTTP/1.1 200 ok\n\nHeelo World!\n");
            bfWriter.flush();
            socket.close();
        }
    }
}

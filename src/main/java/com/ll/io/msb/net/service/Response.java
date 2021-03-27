package com.ll.io.msb.net.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;

public class Response {
    Socket socket;
    private int status;
    static HashMap<Integer, String> codeMap;

    public Response(Socket socket) {
        this.socket = socket;
        if (codeMap == null) {
            codeMap = new HashMap<>();
            codeMap.put(200, "OK");
        }
    }

    public void send(String msg) throws IOException {
        String res = "HTTP/1.1 " + this.status + " " + this.codeMap.get(this.status) + "\n";
        res += "\n";
        res += msg;
        this.sendRaw(msg);
    }

    public void sendRaw(String msg) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        bufferedWriter.write(msg);
        bufferedWriter.flush();
        socket.close();
    }
}

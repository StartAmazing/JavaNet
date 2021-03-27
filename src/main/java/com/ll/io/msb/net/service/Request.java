package com.ll.io.msb.net.service;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpParser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Request {
    String body;
    Map<String, String> headers;
    String method;
    static Pattern methodRegex = Pattern.compile("(GET|PUT|POST|DELETE|OPTIONS|TRACE|HEAD)");
    public Request(Socket socket) throws IOException {
        // DataInputStream -> primitives(char, float ...)
        // InputStream -> bytes
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        BufferedReader bfReader = new BufferedReader(new InputStreamReader(dataInputStream));

        // GET /path HTTP/1，1
        String methodLine = HttpParser.readLine(dataInputStream, "UTF-8");
        Matcher matcher = methodRegex.matcher(methodLine);
        matcher.find();
        String method = matcher.group();

        // Content-Type: xxx
        // Length: xxx
        HashMap<String, String> headMap = new HashMap<>();
        Header[] headers = HttpParser.parseHeaders(dataInputStream, "UTF-8");
        for (Header h : headers) {
            headMap.put(h.getName(), h.getValue());
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream));
        StringBuilder body = new StringBuilder();
        char[] buffer = new char[1024];
        while (dataInputStream.available() > 0) {
            bufferedReader.read(buffer);
            body.append(buffer);
        }

        this.body = body.toString();
        this.method = method;
        this.headers = headMap;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}

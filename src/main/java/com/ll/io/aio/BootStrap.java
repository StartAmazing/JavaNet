package com.ll.io.aio;

import java.io.IOException;

public class BootStrap {
    public  static void main(String[] args) throws IOException {
        new Thread(new AioServer(8080)).start();
    }
}

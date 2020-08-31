package com.ll.utils;

import java.nio.Buffer;

public class CommonPrintUtil {

    public static void printBufferMessage(Buffer buffer){
        System.out.println("buffer's mark: " + buffer.mark() +
                ", buffer's position: " + buffer.position() +
                ", buffer's limit: " + buffer.limit() +
                ", buffer's capacity: " + buffer.capacity() +
                ", buffer's remaining: " + buffer.remaining());
    }
}

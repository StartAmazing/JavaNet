package com.ll.chapter2.useapi.util;

import java.nio.channels.FileChannel;

public class PrintUtils {

    public static void channelPrintUtils(FileChannel fc) throws Exception{
        System.out.println("The Channel size is: " + fc.size() + " position = " + fc.position());
    }
}

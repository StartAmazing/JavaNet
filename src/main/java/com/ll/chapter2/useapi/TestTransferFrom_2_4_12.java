package com.ll.chapter2.useapi;

import com.ll.chapter2.useapi.util.PrintUtils;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

// long transferFrom(WritableChannel src, position, count)
// 方法不修改通道的位置 这个位置指的是【发生数据改变】的通道的位置不会变
public class TestTransferFrom_2_4_12 {
    private static RandomAccessFile raf1;
    private static RandomAccessFile raf2;
    private static FileChannel fc1;
    private static FileChannel fc2;


    private static void testTransferTo01() throws Exception{
        raf1 = new RandomAccessFile("D://abc//2_4_12_a.txt", "rw");
        raf2 = new RandomAccessFile("D://abc//2_4_12_b.txt", "rw");

        fc1 = raf1.getChannel();
        fc2 = raf2.getChannel();

        PrintUtils.channelPrintUtils(fc2);
        PrintUtils.channelPrintUtils(fc1);
        fc2.position(4);
        PrintUtils.channelPrintUtils(fc2);
        PrintUtils.channelPrintUtils(fc1);
        fc1.transferFrom(fc2, 3, 1000);
        PrintUtils.channelPrintUtils(fc2);
        PrintUtils.channelPrintUtils(fc1);

        fc1.close();
        fc2.close();

        raf1.close();
        raf2.close();
    }

    public static void main(String[] args) throws Exception{
        testTransferTo01();
    }
}

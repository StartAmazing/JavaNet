package com.ll.chapter2.useapi;

import com.ll.chapter2.useapi.util.PrintUtils;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

// long transferTo(position, count, WritableChannel dst)
// 方法不修改通道的位置
public class TestTransferTo_2_4_11 {
    private static RandomAccessFile raf1;
    private static RandomAccessFile raf2;
    private static FileChannel fc1;
    private static FileChannel fc2;

    private static void testTransferTo01() throws Exception{
        raf1 = new RandomAccessFile("D://abc//2_4_11_a.txt", "rw");
        raf2 = new RandomAccessFile("D://abc//2_4_11_b.txt", "rw");

        fc1 = raf1.getChannel();
        fc2 = raf2.getChannel();

        fc2.position(8);
        fc1.transferTo(3, 4, fc2);

        fc1.close();
        fc2.close();

        raf1.close();
        raf2.close();
    }


    private static void testTransferTo02() throws Exception{
        raf1 = new RandomAccessFile("D://abc//2_4_11_a.txt", "rw");
        raf2 = new RandomAccessFile("D://abc//2_4_11_b.txt", "rw");

        fc1 = raf1.getChannel();
        fc2 = raf2.getChannel();

        PrintUtils.channelPrintUtils(fc2);
        PrintUtils.channelPrintUtils(fc1);
        fc2.position(8);
        PrintUtils.channelPrintUtils(fc2);
        PrintUtils.channelPrintUtils(fc1);
        fc1.transferTo(3, 1000, fc2);
        PrintUtils.channelPrintUtils(fc2);
        PrintUtils.channelPrintUtils(fc1);

        fc1.close();
        fc2.close();

        raf1.close();
        raf2.close();
    }

    public static void main(String[] args) throws Exception{
//        testTransferTo01();
        testTransferTo02();
    }
}

package com.ll.chapter2.useapi;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

// FileLock(long position, long size, boolean shared), 是获取此通道的文件指定区域上的锁定，
// 执行锁操作。在可以锁定该区域之前、已关闭此通道之前或者已中断调用线程之前(以先到者为准)，将阻
// 塞此方法调用
public class TestFileLock_2_4_13_0 {

    private static RandomAccessFile raf1;
    private static RandomAccessFile raf2;
    private static FileChannel fc1;
    private static FileChannel fc2;


    private static void testLockFile01() throws Exception{
        raf1 = new RandomAccessFile("D://abc//2_4_13.txt", "rw");
        fc1 = raf1.getChannel();
        System.out.println("A Begin");
        fc1.lock(1, 2, false);
        System.out.println("A End");
        Thread.sleep(Integer.MAX_VALUE);
        fc1.close();
        raf1.close();
    }

    public static void main(String[] args) throws Exception{
        testLockFile01();
    }

}

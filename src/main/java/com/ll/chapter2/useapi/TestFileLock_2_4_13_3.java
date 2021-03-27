package com.ll.chapter2.useapi;

import java.io.*;
import java.nio.channels.FileChannel;

// FileLock(long position, long size, boolean shared), 是获取此通道的文件指定区域上的锁定，
// 执行锁操作。在可以锁定该区域之前、已关闭此通道之前或者已中断调用线程之前(以先到者为准)，将阻
// 塞此方法调用
public class TestFileLock_2_4_13_3 {

    private static RandomAccessFile raf1;
    private static RandomAccessFile raf2;
    private static FileOutputStream fos;
    private static FileInputStream fis;
    private static FileChannel fc1;
    private static FileChannel fc2;

    // 具有随机性
    private static void testFileLockInterruptionException2() throws Exception {
        raf1 = new RandomAccessFile(new File("D://abc//2_4_13.txt"), "rw");
        fc1 = raf1.getChannel();
        Thread t1 = new Thread(() -> {
            try {
                System.out.println("B Begin");
                // 如果线程在获取锁的时候感应到自己已经被中断，则会抛出FileLockInterruptException
                fc1.lock(1, 2, false);
                System.out.println("B End");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
//        fc1.close();
//        fos.close();
    }


    public static void main(String[] args) throws Exception {
        testFileLockInterruptionException2();
    }

}

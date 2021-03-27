package com.ll.chapter2.useapi;

import java.io.*;
import java.nio.channels.FileChannel;

// FileLock(long position, long size, boolean shared), 是获取此通道的文件指定区域上的锁定，
// 执行锁操作。在可以锁定该区域之前、已关闭此通道之前或者已中断调用线程之前(以先到者为准)，将阻
// 塞此方法调用
public class TestFileLock_2_4_13 {

    private static RandomAccessFile raf1;
    private static RandomAccessFile raf2;
    private static FileOutputStream fos;
    private static FileInputStream fis;
    private static FileChannel fc1;
    private static FileChannel fc2;

    // 具有随机性
    private static void testLockFileWithAsynchronousCloseException() throws Exception{
        fos = new FileOutputStream(new File("D://abc//2_4_13.txt"));
        fc1 = fos.getChannel();

        Thread t1 = new Thread(() -> {
            try {
                // 在这个函数调用期间如果执行了fc1.close()操作就会出现AsynchronousCloseException，具有一定的随机性
                fc1.lock(1, 2, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                fc1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        Thread.sleep(1);
        t2.start();

        Thread.sleep(1000);
        fc1.close();
        fos.close();
    }

    public static void testFileLockInterruptionException() throws Exception{
        fos = new FileOutputStream(new File("D://abc//2_4_13.txt"));
        fc1 = fos.getChannel();

        Thread t1 = new Thread(() -> {
           try {
               for (int i = 0; i < 1000000; i++) {
                   System.out.println("i = " + i);
               }
               // 如果在等待获取锁定的时候同时中断了调用线程，则将状态设置为中断并且抛出FileLockInterruptException异常
               // 如果调用FileLock lock(long position, long size, boolean shared)方法时已经把调用方设置为中断，则立即
               // 抛出异常；不改变该线程的中断状态
               fc1.lock(1, 2, false);
           } catch (Exception e) {
               e.printStackTrace();
           }
        });

        t1.start();
        Thread.sleep(50);
        t1.interrupt(); // 先执行中断方法
        Thread.sleep(30000);
        fc1.close();
        fos.close();
    }

    public static void main(String[] args) throws Exception{
//        testLockFileWithAsynchronousCloseException();
        testFileLockInterruptionException();
    }

}

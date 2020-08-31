package com.ll.chapter2.useapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class TestFileChannel {
    private static FileOutputStream fos;
    private static FileChannel fileChannel;

    private static FileInputStream fis;

    /**
     * int write(ByteBuffer src) 方法的作用是将remaining字节序列从给定的
     * 缓冲区写入此通道的当前位置，此方法的行为与 WriteableByteChannel 接
     * 口指定的行为完全相同：在任意给定的时刻，一个可写入通道上只能进行一个
     * 写入操作。如果某个线程在通道上发起写入操作，那么在第一个操作完成之前
     * ，将阻塞其他所有视图发起另一个写入操作的线程。其他种类的 I/O 操作是
     * 否与写入操作并发执行，取决于该通道的类型。该方法的返回值代表写入的字
     * 节数，可能为 0。
     * WriteableByteChannel 有两个特点：
     * 1、 将一个 ByteBuffer 中的 remaining 字节序列写入当前位置
     * 2.  write(ByteBuffer src) 是同步的
     * <p>
     * long position() 方法的作用是返回此通道当前的文件位置
     * public abstract FileChannel position(long newPosition) 方法的作
     * 用是设置此通道的文件位置
     */
    //1. 验证 inte write(ByteBuffer src) 是从通道的当前位置开始写入的
    public static void testWriteAndPos1() throws IOException, InterruptedException {

        fos = new FileOutputStream(new File("D:\\abc\\a.txt"));
        fileChannel = fos.getChannel();
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap("abcde".getBytes());
            System.out.println("A fileChannel.position = " + fileChannel.position());
            System.out.println("write() 1 返回值 = " + fileChannel.write(byteBuffer));
            System.out.println("B fileChannel.position = " + fileChannel.position());
            fileChannel.position(2);

            byteBuffer.rewind();  // 注意， 这里是还原 byteBuffer 的 position 为 0
            // 然后在当前 fileChannel 的 position 处再次写入
            System.out.println("write() 2 返回值 = " + fileChannel.write(byteBuffer));
            System.out.println("C fileChannel.position = " + fileChannel.position());
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileChannel.close();
        fos.close();
    }

    //2. 验证 int write(ByteBuffer src) 方法将 ByteBuffer 的 remaining 字节写入
    public static void testWriteAndPos2() throws IOException, InterruptedException {
        fos = new FileOutputStream(new File("D:\\abc\\a.txt"));
        fileChannel = fos.getChannel();
        try {
            ByteBuffer byteBuffer1 = ByteBuffer.wrap("abcde".getBytes());
            ByteBuffer byteBuffer2 = ByteBuffer.wrap("12345".getBytes());
            fileChannel.write(byteBuffer1);

            byteBuffer2.position(1);
            byteBuffer2.limit(3);
            fileChannel.position(2);
            fileChannel.write(byteBuffer2);

            System.out.println(fileChannel.position());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileChannel.close();
            fos.close();
        }
    }

    //3. 验证 int write(ByteBuffer src) 具有同步特性
    public static void testWriteAndPos3() throws IOException, InterruptedException {
        fos = new FileOutputStream(new File("D://abc//a.txt"));
        fileChannel = fos.getChannel();
        for (int i = 0; i < 10; i++) {
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.wrap("abcde\n".getBytes());
                        fileChannel.write(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    try {
                        ByteBuffer byteBuffer = ByteBuffer.wrap("我是中国人\n".getBytes());
                        fileChannel.write(byteBuffer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread1.start();
            thread2.start();
        }

        Thread.sleep(3000);
        fileChannel.close();
        fos.close();
    }

    /**
     * int read(ByteBuffer src) 方法的作用是将字节序列从此通道的当前位置
     * 读入给指定缓冲区的当前位置，此方法的行为与ReadableByteChannel接口
     * 中指定的方法完全相同： 在任意时刻一个可读通道上只能执行一个读取操作
     * 。如果某个线程在某个通道上发起读取操作，那么在第一个读取操作完成之
     * 前，将阻塞其他所有试图发起另一个读取操作的线程。其他种类的 I/O操
     * 作是否继续与读取操作并发执行，取决于该通道的类型。该方法的返回值
     * 代表读取到的字节数，可能为 0。如果该通道已经到达流的末尾，则返回
     * -1。
     * <p>
     * ReadableChannel有如下两个特点：
     * 1. 将通道当前位置的字节序列读入一个ByteBuffer缓存中的remaining
     * 空间中
     * 2. read(ByteBuffer) 是同步的
     */

    // 1. 验证int read(ByteBuffer) 返回值的意义
    // 【正数】 代表从通道的当前位置向ByteBuffer缓冲区中读的字节数
    // 【0】 代表从通道中没有读取任何数据，也就是 0 字节，有可能发生的情况
    // 就是缓存区中没有remaining剩余空间了
    //【-1】 代表到达流的末端
    public static void testReadAndPos1() throws IOException, InterruptedException {
        fis = new FileInputStream(new File("D:\\abc\\b.txt"));
        fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);

        int readLength = fileChannel.read(byteBuffer);
        System.out.println(readLength); // 取得5个字节
        //将下面代码添加注释，那么在此执行read()方法时，返回值是0，因为byteBuffer里面没有remaining空间
//        byteBuffer.clear();
        readLength = fileChannel.read(byteBuffer);
        System.out.println(readLength);  // 取得0个字节

        // 执行clear()方法使得缓冲区还原
        byteBuffer.clear();
        readLength = fileChannel.read(byteBuffer);
        System.out.println(readLength); // 到达流的末尾值为 -1
        byteBuffer.clear();

        fileChannel.close();
        fis.close();
    }

    // 2. 验证int read(ByteBuffer dst)方法是从通道的当前位置开始读取的
    public static void testReadAndPos2() throws IOException, InterruptedException {
        fis = new FileInputStream(new File("D:\\abc\\b.txt"));
        fileChannel = fis.getChannel();

        fileChannel.position(2);
        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        fileChannel.read(byteBuffer);

        byte[] byteArray = byteBuffer.array();
        for (byte ele : byteArray) {
            System.out.print((char) ele + " ");
        }
        System.out.println();
        fileChannel.close();
        fis.close();
    }

    //3. 验证int read(ByteBuffer src)方法将字节放入ByteBuffer当前位置
    public static void testReadAndPos3() throws IOException, InterruptedException {
        fis = new FileInputStream(new File("D:\\abc\\b.txt"));
        fileChannel = fis.getChannel();

        fileChannel.position(2);

        ByteBuffer byteBuffer = ByteBuffer.allocate(5);
        byteBuffer.position(3);
        fileChannel.read(byteBuffer);

        byte[] byteArray = byteBuffer.array();
        for (byte ele : byteArray) {
            System.out.print((char) ele + " ");
        }
        System.out.println();
        fileChannel.close();
        fis.close();
    }

    // 4. 验证int read(ByteBuffer dst)方法具有同步性
    public static void testReadAndPos4() throws IOException, InterruptedException {
        fis = new FileInputStream(new File("D:\\abc\\c.txt"));
        fileChannel = fis.getChannel();

        for (int i = 0; i < 1; i++) {
            Thread thread1 = new Thread(() -> {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(5);
                    int readLength = fileChannel.read(byteBuffer);
                    while (readLength != -1) {
                        byte[] getByte = byteBuffer.array();
                        System.out.println(new String(getByte, 0, readLength));
                        byteBuffer.clear();
                        readLength = fileChannel.read(byteBuffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Thread thread2 = new Thread(() -> {
                try {
                    ByteBuffer byteBuffer = ByteBuffer.allocate(5);
                    int readLength = fileChannel.read(byteBuffer);
                    while (readLength != -1) {
                        byte[] getByte = byteBuffer.array();
                        System.out.println(new String(getByte, 0, readLength));
                        byteBuffer.clear();
                        readLength = fileChannel.read(byteBuffer);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            thread1.start();
            thread2.start();
        }

        Thread.sleep(3000);
        fileChannel.close();
        fis.close();
    }

    // 5. 验证int read(ByteBuffer dst)方法从通道读取的数据大于缓冲区容量
    // 验证int read(ByteBuffer dst)方法从通道读取的字节放入缓冲区的remaining空间中
    public static void testReadAndPos5() throws IOException, InterruptedException {
        fis = new FileInputStream(new File("D:\\abc\\d.txt"));
        fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        byteBuffer.position(1);
        System.out.println("A " + fileChannel.position());
        fileChannel.read(byteBuffer);
        System.out.println("B " + fileChannel.position());

        fileChannel.close();
        fis.close();

        byteBuffer.rewind();

        for (int i = 0; i < byteBuffer.limit(); i++) {
            System.out.print((char) byteBuffer.get());
        }
        System.out.println();
    }

    /**
     * long write(ByteBuffer[] srcs)方法的作用是将每个缓冲区的remaining字节序列
     * 写入此通道的当前位置。调用的形式为 c.write(srcs),该调用与调用
     * c.write(srcs, 0, length)的形式完全相同。long write(ByteBuffer[] srcs)方
     * 法实现的是GatheringByteChannel 接口中同名方法， 接口 GatheringByteChannel
     * 的父接口是 WriteableByteChannel， 说明GatheringByteChannel具有
     * WriteableByteChannel 的两个特性：
     * <p>
     * 1. 将一个 ByteBuffer 缓冲区的 remaining 字节序列写入通道的当前位置中
     * 2. write(ByteBuffer) 方法是同步的
     * 此外， 它还具有第三个特性 —— 将多个 ByteBuffer 缓冲区中的 remaining 字节序
     * 列写入到通道的当前位置中
     */

    // 1. 验证long write(ByteBuffer[] srcs)是从通道的position位置开始写入的
    public static void testBatchWriteAndPos1() throws IOException, InterruptedException {
        fos = new FileOutputStream(new File("D:\\abc\\f.txt"));
        fileChannel = fos.getChannel();

        fileChannel.write(ByteBuffer.wrap("12345".getBytes()));
        fileChannel.position(3);

        ByteBuffer byteBuffer1 = ByteBuffer.wrap("ooooool".getBytes());
        ByteBuffer byteBuffer2 = ByteBuffer.wrap("ooooool".getBytes());

        fileChannel.write(new ByteBuffer[]{byteBuffer1, byteBuffer2});

        fileChannel.close();
        fis.close();
    }

    // 2. 验证long write(ByteBuffer[] srcs) 方法将 ByteBufferd 的 remaining
    // 写入通道
    public static void testBatchWriteAndPos2() throws IOException, InterruptedException {
        fos = new FileOutputStream(new File("D:\\abc\\e.txt"));
        fileChannel = fos.getChannel();

        fileChannel.write(ByteBuffer.wrap("12345".getBytes()));
        fileChannel.position(3);

        ByteBuffer byteBuffer1 = ByteBuffer.wrap("ooooxxl".getBytes());
        ByteBuffer byteBuffer2 = ByteBuffer.wrap("ooooxxl".getBytes());
        byteBuffer1.position(4);
        byteBuffer2.position(4);

        fileChannel.write(new ByteBuffer[]{byteBuffer1, byteBuffer2});

        fileChannel.close();
        fis.close();
    }

    //3. 验证long write(ByteBuffer[] srcs)具有同步性
    public static void testBatchWriteAndPos3() throws IOException, InterruptedException {
        fos = new FileOutputStream(new File("D:\\abc\\g.txt"));
        fileChannel = fos.getChannel();

        for (int i = 0; i < 10; i++) {
            Thread t1 = new Thread(() -> {
                try {
                    ByteBuffer byteBuffer1 = ByteBuffer.wrap("aaaaa1\n".getBytes());
                    ByteBuffer byteBuffer2 = ByteBuffer.wrap("aaaaa2\n".getBytes());

                    fileChannel.write(new ByteBuffer[]{byteBuffer1, byteBuffer2});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    ByteBuffer byteBuffer1 = ByteBuffer.wrap("zzzzz1\n".getBytes());
                    ByteBuffer byteBuffer2 = ByteBuffer.wrap("zzzzz2\n".getBytes());

                    fileChannel.write(new ByteBuffer[]{byteBuffer1, byteBuffer2});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            t1.start();
            t2.start();
        }

        Thread.sleep(3000);
        fileChannel.close();
        fis.close();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("----------------------- use write and position ---------------------");
        testWriteAndPos1();
        System.out.println();
        testWriteAndPos2();
        System.out.println();
        testWriteAndPos3();
        System.out.println("----------------------- use read and position ---------------------");
        testReadAndPos1();
        testReadAndPos2();
        testReadAndPos3();
        testReadAndPos4();
        testReadAndPos5();
        System.out.println("------------------------ batch write --------------------------");
        testBatchWriteAndPos1();
        testBatchWriteAndPos2();
        testBatchWriteAndPos3();
    }
}

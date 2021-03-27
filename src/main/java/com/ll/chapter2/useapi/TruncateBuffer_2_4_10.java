package com.ll.chapter2.useapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.ll.chapter2.useapi.util.PrintUtils.channelPrintUtils;

// 2_10 截断缓冲区
public class TruncateBuffer_2_4_10 {

    private static FileInputStream fis;
    private static FileOutputStream fos;
    private static FileChannel fc;

    // 测试正常截取文件的效果
    public static void testTruncate01() throws Exception {
        ByteBuffer byteBuffer1 = ByteBuffer.wrap("123456789".getBytes());
        fos = new FileOutputStream(new File("D://abc//2_10_1.txt"));

        fc = fos.getChannel();
        fc.write(byteBuffer1);
        channelPrintUtils(fc);
        fc.truncate(3);
        channelPrintUtils(fc);
        fc.close();
        fos.flush();
        fos.close();

    }

    public static void main(String[] args) throws Exception{
        testTruncate01();
    }
}

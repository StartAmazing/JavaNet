package com.ll.io;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class LockingExample {

    public static final boolean EXCLUSIVE = false;
    public static final boolean SHARED = true;

    public static void main(String[] args) throws IOException {

        FileLock lock = null, lock1 = null;
        try {
            RandomAccessFile raf = new RandomAccessFile("C:\\Users\\LL\\Desktop\\截图6.png", "rw");
            FileChannel channel = raf.getChannel();
            lock = channel.lock(0, raf.length() / 2, EXCLUSIVE);

            Thread.sleep(100000);

            lock.release();
            lock1 = channel.lock(raf.length() / 2 + 1, raf.length(), SHARED);

            Thread.sleep(100000);
            lock1.release();
        } catch (InterruptedException | IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (lock != null) {
                lock.release();
            }
            if (lock1 != null) {
                lock1.release();
            }
        }
    }


}

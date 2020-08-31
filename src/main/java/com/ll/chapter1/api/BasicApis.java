package com.ll.chapter1.api;

import com.ll.utils.CommonPrintUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class BasicApis {

    private static char[] charArray;
    static {
        charArray = new char[]{'a', 'b', 'c', 'd', 'e'};
    }

    //测试限制
    private static void testLimit() {
        CharBuffer buffer = CharBuffer.wrap(charArray);
        CommonPrintUtil.printBufferMessage(buffer);
//        buffer.limit(3);
        CommonPrintUtil.printBufferMessage(buffer);

        buffer.put(0, 'o');
        buffer.put(1, 'p');
        buffer.put(2, 'q');
        buffer.put(3, 'r'); // can't be write, throw IndexOutOfBoundsException
        buffer.put(4, 's');
//        buffer.put(5, 't');
    }

    //测试位置
    private static void testPos(){
        char[] chars = new char[]{'a', 'b', 'c', 'd'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        CommonPrintUtil.printBufferMessage(charBuffer);
        charBuffer.position(2);
        CommonPrintUtil.printBufferMessage(charBuffer);
        charBuffer.put('z');
        for (char ch : chars){
            System.out.print(ch + " ");
        }
        System.out.println();
    }

    private static void testRemaining(){
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        CommonPrintUtil.printBufferMessage(charBuffer);
        charBuffer.position(2);
        CommonPrintUtil.printBufferMessage(charBuffer);
    }

    private static void testMark(){
        byte[] bytes = new byte[]{ 1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.position(1);
        byteBuffer.mark(); // 在位置1设置mark

        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.position(2); // 改变位置
//        CommonPrintUtil.printBufferMessage(byteBuffer);
        System.out.println(byteBuffer.position());
        byteBuffer.reset(); // 位置重置
        // 回到位置1
        CommonPrintUtil.printBufferMessage(byteBuffer);
    }

    private static void testCapacityGTZero(){
        try{
            ByteBuffer.allocate(-1);
        }catch (IllegalArgumentException e){
            System.err.println("ByteBuffer's capacity cannot less than zero!");
        }
    }

    private static void testLimitGTZero(){
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{ 1, 2, 3});
        try{
            byteBuffer.limit(-1);
        }catch (IllegalArgumentException e){
            System.err.println("ByteBuffer's limit cannot less than zero!");
        }
    }

    private static void testPosEqLimit(){
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3, 4});
        CommonPrintUtil.printBufferMessage(byteBuffer);

        byteBuffer.position(3);
        CommonPrintUtil.printBufferMessage(byteBuffer);

        byteBuffer.limit(2);
        CommonPrintUtil.printBufferMessage(byteBuffer);
    }

    private static void testClear(){
        byte[] bytes = new byte[]{1, 2, 3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.limit(2);
        byteBuffer.position(2);
        byteBuffer.mark();
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.clear();
        CommonPrintUtil.printBufferMessage(byteBuffer);
    }

    private static void testFlip(){
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{1, 2, 3});
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.put((byte)11);
        byteBuffer.put((byte)22);
        byteBuffer.mark();
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.flip();
        CommonPrintUtil.printBufferMessage(byteBuffer);
    }

    private static void testHasArray(){
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(10);
        byteBuffer1.put((byte) 11);
        byteBuffer1.put((byte) 22);
        System.out.println(byteBuffer1.hasArray());

        ByteBuffer byteBuffer2 = ByteBuffer.allocate(10);
        byteBuffer2.put((byte) 11);
        byteBuffer2.put((byte) 22);
        System.out.println(byteBuffer2.hasArray());
    }

    private static void testHasRemaining(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        byteBuffer.put((byte) 11);
        byteBuffer.put((byte) 22);
        System.out.println(byteBuffer.hasRemaining());

        byteBuffer.put((byte) 33);
        System.out.println(byteBuffer.hasRemaining());
    }

    private static void testUseRemaining(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        byteBuffer.put((byte) 11);
        byteBuffer.put((byte) 22);
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()){
            System.out.print(byteBuffer.get() + " ");
        }
        System.out.println();
        System.out.println(byteBuffer.hasRemaining());
    }

    private static void testRewind(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        byteBuffer.put((byte) 11);
        byteBuffer.put((byte) 22);
        byteBuffer.put((byte) 33);
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.limit(2);
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.rewind();
        CommonPrintUtil.printBufferMessage(byteBuffer);
    }

    private static void testArrayOffset(){
        byte[] bytes = new byte[]{(byte)1, (byte)2, (byte)3};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println(byteBuffer.arrayOffset()); // here is always zero
    }

    // 如果List中存储ByteBuffer数据类型，则可以使用List中的toArray()方法转成ByteBuffer[]数组类型
    private static void testList2Array(){
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{'a', 'b', 'c'});
        ByteBuffer byteBuffer2 = ByteBuffer.wrap(new byte[]{'m', 'n', 'o'});
        ByteBuffer byteBuffer3 = ByteBuffer.wrap(new byte[]{'x', 'y', 'z'});

        List<ByteBuffer> list = new ArrayList<>();
        list.add(byteBuffer1);
        list.add(byteBuffer2);
        list.add(byteBuffer3);

        ByteBuffer[] byteBuffers = new ByteBuffer[list.size()];
        list.toArray(byteBuffers);
        System.out.println(list.size());
        System.out.println(byteBuffers.length);

        for (ByteBuffer bf : byteBuffers){
            while (bf.hasRemaining()){
                System.out.print((char) bf.get() + " ");
            }
            System.out.println();
        }
    }

    //

    public static void main(String[] args) {
        System.out.println("------------------ test Limit -------------------");
        testLimit();
        System.out.println("------------------ test Position -------------------");
        testPos();
        System.out.println("------------------ test remaining -------------------");
        testRemaining();
        System.out.println("------------------ test mark -------------------");
        testMark();
        System.out.println("------------------ capacity cannot less than zero ------------------");
        testCapacityGTZero();
        System.out.println("------------------ capacity cannot less than zero ------------------");
        testLimitGTZero();
        System.out.println("------------------ position is limit when limit is less than position ---------------");
        testPosEqLimit();
        System.out.println("------------------ test clear() if you want to recover default always before you want to store your data ------------------------");
        testClear();
        System.out.println("------------------ test flip() if you has written over data and want to get them ------------------------");
        testFlip();
        System.out.println("------------------ test hasArray() ------------------------");
        testHasArray();
        System.out.println("------------------ test hasRemaining() ------------------------");
        testHasRemaining();
        System.out.println("------------------ test useRemaining() ------------------------");
        testUseRemaining();
        System.out.println("------------------ test rewind() if you need rewrite or read data ------------------------");
        testRewind();
        System.out.println("------------------ test arrayOffset() ------------------------");
        testArrayOffset();
        System.out.println("------------------ test List.toArray(T[]) ------------------------");
        testList2Array();

    }
}

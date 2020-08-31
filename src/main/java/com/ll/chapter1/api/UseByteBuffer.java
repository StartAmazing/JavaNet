package com.ll.chapter1.api;

import com.ll.utils.CommonPrintUtil;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class UseByteBuffer {

    private static void testCreateDirectOrNotByteBuff(){
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
        System.out.println(byteBuffer1.isDirect());

        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(100);
        System.out.println(byteBuffer2.isDirect());
    }

    private static void testReleaseSpaceManually() throws Exception {
        System.out.println("A");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE / 2);
        System.out.println("B");
        byte[] byteArray = new byte[]{1};
        System.out.println(Integer.MAX_VALUE);
        for(int i = 0; i < Integer.MAX_VALUE / 2; i ++){
            byteBuffer.put(byteArray);
        }
        System.out.println("put end !");
        Thread.sleep(1000);

        Method cleaner = byteBuffer.getClass().getMethod("cleaner");
        cleaner.setAccessible(true);
        Object returnValue = cleaner.invoke(byteBuffer);

        Method clean = byteBuffer.getClass().getMethod("clean");
        clean.setAccessible(true);
        clean.invoke(returnValue);

        // 此程序的效果就是一秒钟以后立即回收内存的效果
        // 也就是回收直接缓冲区所占用的内存
    }

    private static void testReleaseSpaceAuto() throws Exception{
        System.out.println("A");
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE / 2);
        System.out.println("B");
        byte[] byteArray = new byte[]{1};
        System.out.println(Integer.MAX_VALUE);
        for(int i = 0; i < Integer.MAX_VALUE / 2; i ++){
            byteBuffer.put(byteArray);
        }
        System.out.println("put end !");
    }

    private static void testCompareDirectOrNot(){
        long beginTime = System.currentTimeMillis();
        ByteBuffer directByteBuffer = ByteBuffer.allocateDirect(1400000000);
        for (int i = 0; i < 1400000000; i ++){
            directByteBuffer.put((byte) 123);
        }
        long endTime = System.currentTimeMillis();
        long directByteBufferCostTime = endTime - beginTime;
        System.out.println(directByteBufferCostTime);

        beginTime = System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1400000000);
        for (int i = 0; i < 1400000000; i ++){
            byteBuffer.put((byte) 123);
        }
        endTime = System.currentTimeMillis();
        long byteBufferCostTime = endTime - beginTime;
        System.out.println(byteBufferCostTime);

        if (byteBufferCostTime > directByteBufferCostTime){
            System.out.println("not direct is slower than direct!");
        }else{
            System.out.println("direct is slower than not direct!");
        }
    }

    private static void testWrap(){
        byte[] bytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(bytes);
        // offset here is max can be set
        ByteBuffer byteBuffer2 = ByteBuffer.wrap(bytes, 2, 6);

        CommonPrintUtil.printBufferMessage(byteBuffer1);
        CommonPrintUtil.printBufferMessage(byteBuffer2);
    }

    private static void testGetAndGet(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.put((byte) 97);
        byteBuffer.put((byte) 98);
        byteBuffer.put((byte) 99);
        byteBuffer.put((byte) 100);
        byteBuffer.put((byte) 101);
        byteBuffer.put((byte) 102);
        byteBuffer.put((byte) 103);
        byteBuffer.put((byte) 104);
        byteBuffer.put((byte) 105);
        byteBuffer.put((byte) 106);

        CommonPrintUtil.printBufferMessage(byteBuffer);
        byteBuffer.flip();
        CommonPrintUtil.printBufferMessage(byteBuffer);
        while (byteBuffer.hasRemaining()){
            System.out.print((char) byteBuffer.get() + " ");
        }
        System.out.println();
        CommonPrintUtil.printBufferMessage(byteBuffer);
    }
    private static void testGetAndGetBatch(){
        int[] a = new int[12];
        for (int i = 0; i < a.length; i ++){
            a[i] = i * 10;
        }

        IntBuffer intBuffer = IntBuffer.allocate(10);
        // if offset + length > src.length, then throws ArrayIndexOfBounds
        // if length >  buffer.remaining, then throws BufferOverflowException
        intBuffer.put(a, 3,7);
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.print(intBuffer.get() + " ");
        }
        System.out.println();
        intBuffer.flip();
        int[] b = new int[3];
        // if offset + length > remaining, then throws ArrayIndexOutOfBounds\
        // if remaining < length, then throws BufferOverflowException
        intBuffer.get(b, 0, b.length);
        for (int ele : b){
            System.out.print(ele + " ");
        }
        System.out.println();
    }

    private static void testGetAndGetAll(){
        int[] a = new int[10];
        for(int i = 0; i < a.length; i ++){
            a[i] = i * 10;
        }
        IntBuffer intBuffer = IntBuffer.allocate(12);
        intBuffer.put(a); // just like intBuffer.put(a, 0, a.length);
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.print(intBuffer.get() + " ");
        }
        System.out.println();
        intBuffer.flip();

        int[] b = new int[4];
        intBuffer.get(b); // just like intBuffer.put(b, 0, b.length)
        CommonPrintUtil.printBufferMessage(intBuffer);
        for (int ele : b){
            System.out.print(ele + " ");
        }
        System.out.println();
        int[] c = new int[3];
        intBuffer.get(c);
        for (int ele : c){
            System.out.print(ele + " ");
        }
        System.out.println();
    }

    private static void testGetAndGetPosNotMove(){
        CharBuffer charBuffer = CharBuffer.wrap(new char[]{'a', 'b', 'c', 'd', 'e'});
        CommonPrintUtil.printBufferMessage(charBuffer);
        System.out.println(charBuffer.get(2));
        CommonPrintUtil.printBufferMessage(charBuffer);
        charBuffer.put(2, 'M');
        CommonPrintUtil.printBufferMessage(charBuffer);

        while (charBuffer.hasRemaining()){
            System.out.print(charBuffer.get() + " ");
        }
        System.out.println();
        CommonPrintUtil.printBufferMessage(charBuffer);
    }

    private static void testPutBatchBuffer(){
        CharBuffer cb1 = CharBuffer.wrap(new char[]{'a','b','c'});
        CharBuffer cb2 = CharBuffer.wrap(new char[]{'A', 'B', 'C', 'D', 'E'});
        cb2.position(2);
        CommonPrintUtil.printBufferMessage(cb1);
        CommonPrintUtil.printBufferMessage(cb2);
        cb2.put(cb1);
        CommonPrintUtil.printBufferMessage(cb1);
        CommonPrintUtil.printBufferMessage(cb2);
        cb2.flip();
        while (cb2.hasRemaining()){
            System.out.print(cb2.get() + " ");
        }
    }

    private static void testPutGetType(){
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(100);
        byteBuffer1.putChar('a'); // 0 - 1, char 占2个字符
        byteBuffer1.putChar(2, 'b'); // 2 - 3

        byteBuffer1.position(4);
        byteBuffer1.putDouble(1.1); // 4 - 11, double 占8个字符
        byteBuffer1.putDouble(12,1.2); // 12 - 19

        byteBuffer1.position(20);
        byteBuffer1.putFloat(2.1f); // 20 -23, float 占4个字符
        byteBuffer1.putFloat(24, 2.2f); // 24 -27

        byteBuffer1.position(28);
        byteBuffer1.putInt(31); // 28 - 31, int 占4个字符
        byteBuffer1.putInt(32, 32); // 32 -35, int 占4个字符

        byteBuffer1.position(36);
        byteBuffer1.putLong(4L); // 36 - 43, long占8个字节
        byteBuffer1.putLong(44, 42L); // 44 - 52, long占8个字节

        byteBuffer1.position(52);
        byteBuffer1.putShort((short) 51); // 52 - 53, short占2个字节
        byteBuffer1.putShort(54, (short) 52); // 54 - 55

        byteBuffer1.position(0);

        byte[] byteArrayOut = byteBuffer1.array();
        for (int i = 0; i < byteArrayOut.length; i++){
//            System.out.println(byteArrayOut[i] + " ");
        }

        System.out.println();
        System.out.println(byteBuffer1.getChar());
        System.out.println(byteBuffer1.getChar(2));
        byteBuffer1.position(4);
        System.out.println(byteBuffer1.getDouble());
        System.out.println(byteBuffer1.getDouble(12));
        byteBuffer1.position(20);
        System.out.println(byteBuffer1.getFloat());
        System.out.println(byteBuffer1.getFloat(24));
        byteBuffer1.position(28);
        System.out.println(byteBuffer1.getInt());
        System.out.println(byteBuffer1.getInt(32));
        byteBuffer1.position(36);
        System.out.println(byteBuffer1.getLong());
        System.out.println(byteBuffer1.getLong(44));
        byteBuffer1.position(52);
        System.out.println(byteBuffer1.getShort());
        System.out.println(byteBuffer1.getShort(54));
    }

    // slice()方法的作用是创建一个新的字节缓冲区，其内容是此缓冲区内容的共享子序列、
    // 新缓冲区的内容将从次缓冲区的当前位置开始。此缓冲区内容的更改在新缓冲区中是可
    // 见的，反之亦然；这两个缓冲区的位置、限制和标记都是相互独立的。新缓冲区的位置
    // 将为0，其容量和限制将为次缓冲区中所剩余的字节数量，其标记是不确定的。当且仅
    // 当次缓冲区为直接缓冲区时，新缓冲区才是直接缓冲区。当且晋档此缓冲区为只读时，
    // 新缓冲区才是只读的
    public static void testSlice(){
        byte[] byteArrayIn1 = {1, 2, 3, 4, 5, 6, 7, 8};
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(byteArrayIn1);
        byteBuffer1.position(5);
        ByteBuffer byteBuffer2 = byteBuffer1.slice();
        CommonPrintUtil.printBufferMessage(byteBuffer1);
        CommonPrintUtil.printBufferMessage(byteBuffer2);
        byteBuffer2.put(2, (byte) 111);

        System.out.println(byteBuffer1.get(7));
        System.out.println(byteBuffer2.get(2));

        System.out.println(byteBuffer2.arrayOffset());
    }

    // asCharBuffer() 方法的作用： 创建字节安魂冲区的视图， 作为char缓冲区。新缓冲区的内容
    // 将从此缓冲区的当前位置开始。此缓冲区内容的更改在新缓冲区中是可见的，反之亦然；这两个
    // 缓冲区的位置、限制和标记值都是相互独立的。新缓冲区的位置将为0， 其容量和限制将为此缓
    // 冲区剩余容量字节数的 1/2 ,其标记是不确定的，当且仅当此缓冲区为直接缓冲区时，新缓冲区
    // 才是直接缓冲区，当且仅当此缓冲区为只读缓冲区时，新缓冲区才是只读缓冲区。
    public static void testAsCharBuffer(){
//        byte[] byteArrayIn1 = "我是中国人".getBytes();
        byte[] byteArrayIn1 = "我是中国人".getBytes(StandardCharsets.UTF_16BE);
        System.out.println(Charset.defaultCharset().name());

        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayIn1);
        System.out.println("charBuffer=" + byteBuffer.getClass().getName());

        CharBuffer charBuffer = byteBuffer.asCharBuffer();
        System.out.println("charBuffer=" + charBuffer.getClass().getName());

        CommonPrintUtil.printBufferMessage(byteBuffer);
        CommonPrintUtil.printBufferMessage(charBuffer);

        charBuffer.position(0);

        for (int i = 0; i < charBuffer.capacity(); i ++){
            // get() 方法使用的是UTF-16BE
            // UTF-8 与 UTF-16BE不是一种编码
            // 所以出现了乱码
            System.out.print(charBuffer.get() + " ");
        }

        System.out.println();
    }

    // order()方法与字节数据排列的顺序有关，因为不同CPU在读取字节时的顺序不一样的
    // ,有的CPU从高位开始读，有的CPU从低位开始读，当在这两种CPU之间传递数据的时候
    // 就要同一，此时order(ByteOrder bo) 方法句有用武之地了，它的作用就是设置字
    // 节的排列顺序
    public static void testOrder(){
        int value = 123456789;
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        System.out.println(byteBuffer.order() + " ");
        byteBuffer.putInt(value);
        byte[] bytes = byteBuffer.array();
        for (int i = 0; i < bytes.length; i ++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.BIG_ENDIAN);
        System.out.println(byteBuffer.order() + " ");
        byteBuffer.putInt(value);
        bytes = byteBuffer.array();
        for (int i = 0; i < bytes.length; i ++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        byteBuffer = ByteBuffer.allocate(4);
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println(byteBuffer.order() + " ");
        byteBuffer.putInt(value);
        bytes = byteBuffer.array();
        for (int i = 0; i < bytes.length; i ++){
            System.out.print(bytes[i] + " ");
        }
        System.out.println();

        // 如果写入顺序和读取顺序不一样就会出现数据错误
        ByteBuffer byteBuffer1 = ByteBuffer.allocate(8);
        byteBuffer1.order(ByteOrder.BIG_ENDIAN);
        byteBuffer1.putInt(123);
        byteBuffer1.putInt(456);
        byteBuffer1.rewind();

        System.out.println(byteBuffer1.getInt());
        System.out.println(byteBuffer1.getInt());

        byteBuffer1.rewind();
        byteBuffer1.order(ByteOrder.LITTLE_ENDIAN);
        System.out.println(byteBuffer1.getInt());
        System.out.println(byteBuffer1.getInt());
    }

    // asReadOnlyBuffer()方法的作用：创建共享此缓冲区内容的新的只读字节缓冲区
    // 。新缓冲区的内容将为此缓冲区的内容。此缓冲区的内容的更改在新缓冲区中是可
    // 见的，但是新缓冲区将是只读的而且不能修改内容。两个缓冲区的位置、标记、限
    // 制都是相互独立的。新缓冲区的容量、限制、位置和标记值将与此缓冲区相同、
    public static void testAsReadOnly(){
        byte[] bufferArrayIn = {1, 2, 3, 4, 5};
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(bufferArrayIn);
        ByteBuffer byteBuffer2 = byteBuffer1.asReadOnlyBuffer();

        System.out.println(byteBuffer1.isReadOnly());
        System.out.println(byteBuffer2.isReadOnly());

//        byteBuffer2.putInt(1);  // throw ReadOnlyBufferException
    }

    // 压缩缓冲区，compact(); 将缓冲区的当前位置和限制之间的字节（如果有）复制到缓冲区的开始处
    // 即将索引 p = position() 处的字节复制到索引 0 处， 将索引 p + 1处的字节复制到索引 0 + 1
    // 处 ... 将索引 limit() - 1处的字节赋值到 n = limit() - 1 - p处， 然后将索引的位置置为
    // n + 1, 并将其限制设置为1其容量。如果已经定义了标记，则丢弃它。将缓冲区的位置设置为复制
    // 的字节数而不是 0 ，以便于调用此方法后可以紧接着调用另一个相对put方法
    public static void  testCompact(){
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(new byte[]{1, 2, 3, 4, 5, 6});
        CommonPrintUtil.printBufferMessage(byteBuffer1);
        System.out.println("1 getValue " + byteBuffer1.get());
        CommonPrintUtil.printBufferMessage(byteBuffer1);
        System.out.println("2 getValue " + byteBuffer1.get());
        CommonPrintUtil.printBufferMessage(byteBuffer1);

        byteBuffer1.compact();
        System.out.println("ByteBuffer compact(). ");
        CommonPrintUtil.printBufferMessage(byteBuffer1);
        byte[] getByteArray = byteBuffer1.array();
        for (int i = 0; i < getByteArray.length; i ++){
            System.out.print(getByteArray[i] + " ");
        }
    }

    // 比较缓冲区内容是否相同有两种办法： equals和 compareTo()
    public static void testCompare(){
        // equals()
        // 1. 判断是不是自身，如果是就返回true
        byte[] byteArrayIn1 = {1, 2, 3, 4, 5};
        ByteBuffer byteBuffer1 = ByteBuffer.wrap(byteArrayIn1);
        System.out.println("A = " + byteBuffer1.equals(byteBuffer1));

        //2. 判断是不是ByteBuffer类的实例，如果不是，直接返回false
        byte[] byteArrayIn2 = {1, 2, 3, 4, 5};
        int[] intArrayIn1 = {1, 2, 3, 4, 5};

        ByteBuffer byteBuffer2 = ByteBuffer.wrap(byteArrayIn2);
        IntBuffer intBuffer1 = IntBuffer.wrap(intArrayIn1);
        System.out.println("B = " + byteBuffer2.equals(intBuffer1));

        //3. 判断remaining的值是否一样，如果不一样直接返回false
        byte[] byteArray3 = {3, 4, 5};
        byte[] byteArray4 = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        ByteBuffer byteBuffer3 = ByteBuffer.wrap(byteArray3);
        ByteBuffer byteBuffer4 = ByteBuffer.wrap(byteArray4);

        byteBuffer4.position(2);
        CommonPrintUtil.printBufferMessage(byteBuffer3);
        CommonPrintUtil.printBufferMessage(byteBuffer4);

        System.out.println("C = " + byteBuffer3.equals(byteBuffer4));

        //4. 判断两个缓冲区的值是否完全一样，如果有一个不一样则返回false
        byte[] byteArrayIn5 = {1, 2, 3, 4, 5};
        byte[] byteArrayIn6 = {1, 2, 3, 11, 5};

        ByteBuffer byteBuffer5 = ByteBuffer.wrap(byteArrayIn5);
        ByteBuffer byteBuffer6 = ByteBuffer.wrap(byteArrayIn6);

        byteBuffer5.position(2);
        byteBuffer6.position(2);
        byteBuffer5.limit(4);
        byteBuffer6.limit(4);

        System.out.println("D = " + byteBuffer5.equals(byteBuffer6));


        //compareTo()
        //1. 如果开始与结束范围之间的每个字节都相同，则返回两者remaining的减数
        System.out.println("E = " + byteBuffer3.compareTo(byteBuffer4));
        //2. 如果在开始和结束范围之间，如果有多个字节不同，返回第一个不等字节的减数
        byte[] bytes1 = {1 , 2, 3, 4, 5};
        byte[] bytes2 = {1 , 2, 3, 4, 5, 6, 7, 8};

        ByteBuffer byteBuffer7 = ByteBuffer.wrap(bytes1);
        ByteBuffer byteBuffer8 = ByteBuffer.wrap(bytes2);

        byteBuffer8.position(3);

        System.out.println("F = " + byteBuffer7.compareTo(byteBuffer8));
    }

    // 创建贡献此缓冲区内容的新的字节缓冲区，新缓冲区的内容将为此缓冲区的内容
    // 此缓冲区的内容的更改在新缓冲区中是可见的，反之亦然。在创建新缓冲区的时
    // 候，容量、限制、位置、标记的值与此缓冲区相同，但是这两个缓冲区的位置、
    // 界限和标记值是相互独立的。当且仅当此缓冲区为直接缓冲区时，新缓冲区才
    // 是直接缓冲区，当且仅当此缓冲区是只读时，新缓冲区才是只读
    public static void testDuplicate(){
        // slice() VS duplicate()
        byte[] byteArrayIn1 = {1, 2, 3, 4, 5};
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArrayIn1);
        byteBuffer.position(2);

        CommonPrintUtil.printBufferMessage(byteBuffer);

        ByteBuffer byteBuffer1 = byteBuffer.slice();
        ByteBuffer byteBuffer2 = byteBuffer.duplicate();

        //byteBuffer3 和 byteBuffer指向的地址是一个
        // 所以在debug中的id是一样的
        ByteBuffer byteBuffer3 = byteBuffer;

        CommonPrintUtil.printBufferMessage(byteBuffer1);
        CommonPrintUtil.printBufferMessage(byteBuffer2);

        byteBuffer1.position(0);
        for (int i = byteBuffer1.position(); i < byteBuffer1.limit(); i ++){
            System.out.print(byteBuffer1.get(i) + " ");
        }

        System.out.println();

        byteBuffer2.position(0);
        for (int i = byteBuffer2.position(); i < byteBuffer2.limit(); i ++){
            System.out.print(byteBuffer2.get(i) + " ");
        }

        System.out.println();


    }


    public static void main(String[] args) throws Exception {
        System.out.println("----------------------- create direct or not direct byteBuffer ---------------------");
        testCreateDirectOrNotByteBuff();
        System.out.println("----------------------- release space used by DirectByteBuffer manually ---------------------");
//        testReleaseSpaceManually();
        System.out.println("----------------------- release space used by DirectByteBuffer auto ---------------------");
//        testReleaseSpaceAuto();
        System.out.println("----------------------- compare direct Buffer and not ---------------------");
//        testCompareDirectOrNot();
        System.out.println("----------------------- use wrap deal data ---------------------");
        testWrap();
        System.out.println("----------------------- use get or put deal data ---------------------");
        testGetAndGet();
        System.out.println("----------------------- use get or put batch deal data ---------------------");
        testGetAndGetBatch();
        System.out.println("----------------------- use get or put all deal data ---------------------");
        testGetAndGetAll();
        System.out.println("----------------------- use get or put with pos not move ---------------------");
        testGetAndGetPosNotMove();
        System.out.println("----------------------- use put batch with buffer ---------------------");
        testPutBatchBuffer();
        System.out.println("----------------------- use putType and getType ---------------------");
        testPutGetType();
        System.out.println("----------------------- test slice --------------------------");
        testSlice();
        System.out.println("----------------------- test asCharBuffer ---------------------");
        testAsCharBuffer();
        System.out.println("----------------------- test order ---------------------");
        testOrder();
        System.out.println("----------------------- test asReadOnly ---------------------");
        testAsReadOnly();
        System.out.println("----------------------- test compact ---------------------");
        testCompact();
        System.out.println("----------------------- test compare ---------------------");
        testCompare();
        System.out.println("----------------------- tes duplicate ---------------------");
        testDuplicate();
    }
}

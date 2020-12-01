package com.ll.chapter2.introduce;

/**
 * 通道接口层次
 */

/**
 * AutoCloseable 强调的是与 try 结合实现自动关闭，该接口针对的是任何资源，不仅仅是I/O
 * 并且此接口不是幂等的，重复调用此接口的close()方法会出现异常
 */
public class ConstructChannel {
    static class DBOperator implements AutoCloseable{

        @Override
        public void close() throws Exception {
            System.out.println("连接关闭");
        }
    }

    // 测试AutoCloseabel接口
    public static void testCloseable(){
        try(DBOperator dbOperator = new DBOperator()){
            System.out.println("使用 " + dbOperator + " 开始数据库操作 ... ");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * AsynchronousChannel接口主要支作用是使通道支持异步 IO 操作
     */
    public static void testAsynchronousChannel(){

    }

    public static void main(String[] args) {
        System.out.println("------------------ test autoCloseable ----------------");
        testCloseable();
        System.out.println("------------------ order to introduce 11 sub interface of Channel ------------------------------");
        System.out.println("------------------ test AsynchronousChannel ------------------");
        testAsynchronousChannel();
    }
}

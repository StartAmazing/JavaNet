package com.ll.io.bio;

import java.io.*;
import java.net.Socket;

public class BioClient {

    private static int SERVER_PORT = 8080;
    private static String SERVER_IP = "127.0.0.1";

    public static void main(String[] args) {
        Socket socket = null;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            String hello = "hello server";
            OutputStream os = socket.getOutputStream();
            os.write(hello.getBytes());
            os.flush();
            InputStream is = socket.getInputStream();
            byte[] recv = new byte[1024];
            is.read(recv);
            System.out.println("client receive: " + new String(recv));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                br = null;
            }
            if (pw != null) {
                try {
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pw = null;
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                socket = null;
            }
        }
    }

    public static byte[] intToByte(int value) {
        byte[] byte_src = new byte[4];
        byte_src[0] = (byte) ((value & 0xFF000000) >> 24);
        byte_src[1] = (byte) ((value & 0x00FF0000) >> 16);
        byte_src[2] = (byte) ((value & 0x0000FF00) >> 8);
        byte_src[3] = (byte) ((value & 0x000000FF));
        return byte_src;
    }


}

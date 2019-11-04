package com.srv.fileQueu;

import com.srv.util.FileUtil;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

public class FileQueu {

    private static FileQueu instance = new FileQueu();


    private ReadHandler readHandler = null;
    private LinkedBlockingDeque deque = null;

    public static FileQueu getInstance(){
        return instance;
    }

    public FileQueu(){

    }

    public void setQueu(LinkedBlockingDeque deque){
        this.deque = deque;

    }
    public void setReadHandler(ReadHandler readHandler) {
        this.readHandler = readHandler;
    }


    public void fileWrite(String fileName, boolean gubun, String prifix, String str) throws IOException, InterruptedException {
        /*
        RandomAccessFile rf = null;
        FileChannel channel = null;

        File file = new File(fileName);
        file.createNewFile();

        rf = new RandomAccessFile(file, "rw");
        channel = rf.getChannel();

        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, str.length());

        map.position(0);
        map.put(str.getBytes());

        channel.close();
        rf.close();

        //System.out.println(fileName +" Create File...");

        if(gubun) {
            deque.put(file);
        }
        */
        File file = FileUtil.fileWrite(fileName, str);


        if(gubun){
            deque.put(file);
        }


    }




    public void fileRead(File file, ReadHandler readHandler) throws IOException{
/*
        RandomAccessFile rf = null;
        FileChannel channel = null;

        byte[] data = null;
        if(file != null && file.isFile()) {
            rf = new RandomAccessFile(file, "r");
            channel = rf.getChannel();


            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0, (int) rf.length());

            data = new byte[(int) rf.length()];
            map.get(data);

            channel.close();
            rf.close();

            readHandler.setHandler(data, file);

        }
*/
        byte[] data = FileUtil.fileRead(file);
        readHandler.setHandler(data, file);
    }



    public void dataHandler(ReadHandler readHandler){

        Object result = null;
        try{
            File file = (File)deque.take();
            if(file != null && file.isFile()) {
                //System.out.println("File read file.getAbsolutePath() :" + file.getAbsolutePath());
                fileRead(file, readHandler);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }





    public int getFileCnt(){
        return deque.size();
    }


    public void fileDelete(){
        readHandler.fileDelete();
    }

}

package com.srv.fileQueu;

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

    private final String filePath = "/home/dextop/data/";

    public String getFilePath() {
        return filePath;
    }

    public void fileWrite(String filePath, String prifix, String str) throws IOException, InterruptedException {
        RandomAccessFile rf = null;

        FileChannel channel = null;


        if(filePath == null || "".equals(filePath)){
            filePath = getFilePath();
        }

        String fileName = filePath + prifix + "_srv."+getDate("yyyyMMddhhmmssSSS");

        File file = new File(fileName);
        file.createNewFile();

        rf = new RandomAccessFile(fileName, "rw");
        channel = rf.getChannel();

        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, str.length());

        map.position(0);
        map.put(str.getBytes());

        channel.close();
        rf.close();

        //System.out.println(fileName +" Create File...");

        deque.put(file);

    }

    public void fileObjectWrite(String prifix, Object object) throws IOException, InterruptedException {

        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        String fileName = filePath + prifix + "_srv."+getDate("yyyyMMddhhmmssSSS");

        File file = new File(fileName);
        file.createNewFile();

        outputStream = new FileOutputStream(file);
        bufferedOutputStream = new BufferedOutputStream(outputStream);
        objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

        objectOutputStream.writeObject(object);

        objectOutputStream.close();
        bufferedOutputStream.close();
        outputStream.close();

        deque.put(file);

    }

    public void fileObjectRead(File file, ReadHandler readHandler) throws Exception, IOException{
        InputStream inputStream = null;
        BufferedInputStream bufferedInputStream = null;
        ObjectInputStream objectInputStream = null;

        inputStream = new FileInputStream(file);
        bufferedInputStream = new BufferedInputStream(inputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);

        Object object = objectInputStream.readObject();

        readHandler.setHandler(object, file);

        objectInputStream.close();
        bufferedInputStream.close();
        inputStream.close();

        file.delete();
    }


    public void fileRead(String threadName, File file, ReadHandler readHandler) throws IOException{

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

    }


    public void fileRead(File file, ReadHandler readHandler) throws IOException{
        fileRead("", file, readHandler);
    }

    public String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS");
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }


    public String getDate(String format){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }


    public void dataHandler(ReadHandler readHandler){

        Object result = null;
        try{
            File file = (File)deque.take();
            if(file != null && file.isFile()) {
                System.out.println("File read file.getAbsolutePath() :" + file.getAbsolutePath());
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

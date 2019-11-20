package com.srv.util;

import com.srv.fileQueu.ReadHandler;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileUtil {


    public static File[] getFileList(String path){

        File file = new File(path);

        String dirName = file.getParent();
        File dirFile = new File(dirName);
        if(!dirFile.isDirectory()){
            dirFile.mkdirs();
        }

        return file.listFiles();
    }

    public static File fileWrite(String fileName, String str) throws IOException, InterruptedException {
        RandomAccessFile rf = null;
        FileChannel channel = null;

        File file = new File(fileName);


        String dirName = file.getParent();
        File dirFile = new File(dirName);
        if(!dirFile.isDirectory()){
            dirFile.mkdirs();
        }


        file.createNewFile();

        rf = new RandomAccessFile(file, "rw");
        channel = rf.getChannel();

        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, str.getBytes("utf-8").length);

        map.position(0);
        map.put(str.getBytes("utf-8"));


        channel.close();
        rf.close();

        return file;

    }


    public static byte[] fileRead(File file) throws IOException{

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
        }


        return data;

    }



    public void fileObjectWrite(String prifix, Object object) throws IOException, InterruptedException {

        OutputStream outputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        ObjectOutputStream objectOutputStream = null;


        File file = new File("");
        file.createNewFile();

        outputStream = new FileOutputStream(file);
        bufferedOutputStream = new BufferedOutputStream(outputStream);
        objectOutputStream = new ObjectOutputStream(bufferedOutputStream);

        objectOutputStream.writeObject(object);

        objectOutputStream.close();
        bufferedOutputStream.close();
        outputStream.close();


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
}

package com.srv.fileQueu;

import com.srv.util.FileUtil;

import java.io.*;
import java.util.concurrent.LinkedBlockingDeque;

public class FileQueu {

    private static FileQueu instance = new FileQueu();


    private ReadHandler readHandler = null;
    private LinkedBlockingDeque deque = null;
    private LinkedBlockingDeque resutDeque = null;


    public FileQueu(){
        deque = new LinkedBlockingDeque();
        resutDeque = new LinkedBlockingDeque();
    }

    public LinkedBlockingDeque getDeque() {
        return deque;
    }

    public void setDeque(LinkedBlockingDeque deque) {
        this.deque = deque;
    }

    public LinkedBlockingDeque getResutDeque() {
        return resutDeque;
    }

    public void setResutDeque(LinkedBlockingDeque resutDeque) {
        this.resutDeque = resutDeque;
    }

    public static FileQueu getInstance(){
        return instance;
    }


/*
    public void setQueu(LinkedBlockingDeque deque){
        this.deque = deque;

    }
*/
    public void setReadHandler(ReadHandler readHandler) {
        this.readHandler = readHandler;
    }


    public void fileWrite(String fileName, boolean gubun, String prifix, String str) throws IOException, InterruptedException {
        File file = FileUtil.fileWrite(fileName, str);
        if(gubun){
            deque.put(file);
        }else{
            resutDeque.put(file);
        }
    }


    public void sendFileWrite(String fileName, String str) throws Exception{
        fileWrite(fileName, true, "", str);
    }

    public void resultFileWrite(String fileName, String str) throws Exception{
        fileWrite(fileName, false, "", str);
    }



    public void fileRead(File file, ReadHandler readHandler) throws IOException{

        byte[] data = FileUtil.fileRead(file);
        readHandler.setHandler(data, file);
    }



    public void dataHandler(ReadHandler readHandler, String gubun){

        Object result = null;
        File file = null;
        try{

            if("send".equals(gubun)) {
                file = (File) deque.take();
            }else if("result".equals(gubun)){
                file = (File) resutDeque.take();
            }

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

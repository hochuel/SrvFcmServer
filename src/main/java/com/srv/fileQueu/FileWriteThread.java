package com.srv.fileQueu;

import java.util.concurrent.atomic.AtomicInteger;

public class FileWriteThread implements Runnable {


    private FileQueuMain fileQueuMain;

    private String name;

    private AtomicInteger atomicInteger = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileWriteThread(FileQueuMain fileQueuMain, AtomicInteger atomicInteger){

        this.fileQueuMain = fileQueuMain;

        this.atomicInteger = atomicInteger;
    }




    public void run(){

        int index = 0;
        while(true){
            try {

                String str = "";
                for(int i = 0; i < 10; i++){


                    str += this.name + "::" + fileQueuMain.getFileQueu().getDate()+ " test file data+["+atomicInteger.incrementAndGet()+"] \r\n";

                    index ++;
                }


                fileQueuMain.getFileQueu().fileWrite("", true, name, str);

                Thread.sleep(1000);


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

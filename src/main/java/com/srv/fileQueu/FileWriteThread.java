package com.srv.fileQueu;

import com.srv.util.AtomicCustom;
import com.srv.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;


public class FileWriteThread implements Runnable {


    private FileQueuMain fileQueuMain;

    private String name;

    @Autowired
    private AtomicCustom atomicCustom;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FileWriteThread(FileQueuMain fileQueuMain){

        this.fileQueuMain = fileQueuMain;

    }




    public void run(){

        boolean startThread = true;
        while(startThread){
            try {

                String str = "";
                for(int i = 0; i < 100; i++){

                    int index = atomicCustom.getIntData();
                    if(index > 10000){
                        startThread = false;
                    }

                    str += this.name + "::" + DateUtil.getDate() + " test file data+["+index+"] \r\n";

                }


                fileQueuMain.getFileQueu().fileWrite("", true, name, str);

                Thread.sleep(1000);


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

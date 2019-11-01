package com.srv.send;

import com.srv.fileQueu.FileQueuMain;
import com.srv.fileQueu.ReadHandler;

import java.io.File;

public class SendDataReadThread implements Runnable {


    private FileQueuMain fileQueuMain = null;

    public SendDataReadThread(FileQueuMain fileQueuMain){
        this.fileQueuMain = fileQueuMain;
    }

    private FcmSendProcess fcmSendProcess;

    public void setFcmSendProcess(FcmSendProcess fcmSendProcess){
        this.fcmSendProcess = fcmSendProcess;
    }


    private String name="";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while(true){
            try {

                ReadHandler readHandler = new FcmHandlerProcess(fcmSendProcess, name);

                fileQueuMain.getFileQueu().dataHandler(readHandler);

                File file = readHandler.getFile();

                String fileName = file.getName();

                fileQueuMain.getFileQueu().fileWrite("/home/dextop/data/result/"+fileName, false, name, (String)readHandler.get());

                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

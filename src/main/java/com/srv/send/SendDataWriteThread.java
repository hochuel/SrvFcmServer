package com.srv.send;

import com.srv.fileQueu.FileQueuMain;
import com.srv.util.AtomicCustom;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

public class SendDataWriteThread implements Runnable {

    private FileQueuMain fileQueuMain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    @Autowired
    private AtomicCustom atomicCustom;

    public SendDataWriteThread(FileQueuMain fileQueuMain){

        this.fileQueuMain = fileQueuMain;
    }



    @Override
    public void run() {

        boolean startThread = true;
        while(startThread){
            try {

                String jsonString = "";
                for(int i = 0; i < 100; i++) {
                    JSONObject jsonObject = new JSONObject();

                    int index = atomicCustom.getIntData();

                    if(index > 10000){
                        startThread = false;
                    }

                    jsonObject.put("title", "Test title[" + index + "]");
                    jsonObject.put("body", "Test Body[" + index + "]");
                    jsonObject.put("token", "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HO33");

                    jsonString += jsonObject.toJSONString()+"\r\n";
                }

                fileQueuMain.getFileQueu().fileWrite("", true, name, jsonString);

                Thread.sleep(1000);


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

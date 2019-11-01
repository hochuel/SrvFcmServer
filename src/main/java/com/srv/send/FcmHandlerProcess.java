package com.srv.send;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.srv.fileQueu.FileQueuMain;
import com.srv.fileQueu.ReadHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class FcmHandlerProcess implements ReadHandler {



    @Autowired
    private FileQueuMain fileQueuMain;

    private FcmSendProcess fcmSendProcess = null;
    private Object resultObj = null;

    private String prifix = "";

    public FcmHandlerProcess(FcmSendProcess fcmSendProcess, String prifix){
        this.fcmSendProcess = fcmSendProcess;
        this.prifix = prifix;
    }

    @Override
    public void setHandler(byte[] data, File file){
        ByteArrayInputStream is = null;
        BufferedReader br = null;

        List<Message> list = new ArrayList<Message>();
        List resultJsonList  = new ArrayList<Message>();
        try {
            is = new ByteArrayInputStream(data);
            br = new BufferedReader(new InputStreamReader(is));


            String line = null;
            while ((line = br.readLine()) != null) {

                //System.out.println(line);
                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
                    String title = jsonObject.get("title").toString();
                    String body = jsonObject.get("body").toString();
                    String token = jsonObject.get("token").toString();

                    Message message = Message.builder()
                            .setNotification(new Notification(title, body))
                            .setToken(token)
                            .build();

                    list.add(message);

                    resultJsonList.add(jsonObject);

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

            BatchResponse batchResponse = fcmSendProcess.sendAllMessage(list, "");
            //System.out.println(batchResponse.getSuccessCount() + " messages were sent successfully");

            List<SendResponse> resultList = batchResponse.getResponses();
            int index = 0;
            String resultStr = "";
            for(SendResponse response : resultList){
                //System.out.println(response.isSuccessful() + ":" + response.getMessageId());

                JSONObject jsonObject = (JSONObject)resultJsonList.get(index);
                jsonObject.put("result", response.isSuccessful());

                index++;

                resultStr += jsonObject.toJSONString()+"\r\n";

                //System.out.println(resultStr);
                //System.out.println(jsonObject.toJSONString());
            }

            try {
                fileQueuMain.getFileQueu().fileWrite("/home/dextop/data/result/", prifix, resultStr);
            }catch(Exception ex){
                ex.printStackTrace();
            }



            file.delete();

        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            try {
                is.close();
                br.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void setHandler(Object object, File file) {

    }

    @Override
    public void put(Object obj) {

    }

    @Override
    public Object get() {
        return resultObj;
    }

    @Override
    public void fileDelete() {

    }
}

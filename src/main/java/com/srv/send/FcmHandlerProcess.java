package com.srv.send;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.srv.fileQueu.FileQueuMain;
import com.srv.fileQueu.ReadHandler;
import com.srv.util.ErrorUtils;
import com.srv.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FcmHandlerProcess implements ReadHandler {

    private Logger logger = LogManager.getLogger("FcmHandlerProcess");

    private FcmSendProcess fcmSendProcess = null;
    private Object resultObj = null;

    private String prifix = "";

    private File file;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

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
                    String appName = jsonObject.get("appId").toString();
                    String msgId = jsonObject.get("msgId").toString();
                    String title = jsonObject.get("title").toString();
                    String body = jsonObject.get("body").toString();
                    String token = jsonObject.get("token").toString();

                    Message message = Message.builder()
                            //.putData("msgId", msgId)
                            .setNotification(new Notification(title, body))
                            .setToken(token)
                            .build();

                    list.add(message);

                    resultJsonList.add(jsonObject);

                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }


            JSONObject appInfo = (JSONObject)resultJsonList.get(0);
            BatchResponse batchResponse = fcmSendProcess.sendAllMessage(list, appInfo.get("appId").toString());

            List<SendResponse> resultList = batchResponse.getResponses();
            int index = 0;
            String resultStr = "";
            for(SendResponse response : resultList){
                JSONObject jsonObject = (JSONObject)resultJsonList.get(index);

                logger.error(ErrorUtils.getError(response.getException()));

                jsonObject.put("result", response.isSuccessful());
                jsonObject.put("resultDt", System.currentTimeMillis());
                index++;

                resultStr += jsonObject.toJSONString()+"\r\n";

                logger.info(resultStr);

            }

            resultObj = resultStr;
            setFile(file);

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

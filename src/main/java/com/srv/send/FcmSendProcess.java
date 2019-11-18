package com.srv.send;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.srv.dao.AppDAO;
import com.srv.util.ErrorUtils;
import com.srv.util.PropertyService;
import com.srv.vo.TbAppVO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FcmSendProcess {

    Logger logger = LogManager.getLogger("FcmSendProcess");

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private AppDAO appDAO;


    private Map<String, FirebaseApp> appMap = new HashMap<String, FirebaseApp>();

    private FirebaseApp app01 = null;

    public void appReset(){
        InputStream serviceAccount = null;
        //FileInputStream fileInputStream = null;
        FirebaseOptions options = null;
        try {
            //ClassPathResource resource = new ClassPathResource("json/mypush-2680e-firebase-adminsdk-dgxzc-cb58899903.json");

            serviceAccount = new FileInputStream(propertyService.getString("json.path")+"/mypush-2680e-firebase-adminsdk-dgxzc-cb58899903.json");


            //serviceAccount = resource.getInputStream();
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            app01 = FirebaseApp.initializeApp(options, "app01");
        } catch (FileNotFoundException e) {
            logger.error("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            logger.error("FirebaseOptions IOException" + e.getMessage());
        }finally {
            try {
                serviceAccount.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
    }


    public FirebaseApp appReset(String appId, String appJson){
        FirebaseApp firebaseApp = null;
        InputStream serviceAccount = null;
        FirebaseOptions options = null;
        try {
            //ClassPathResource resource = new ClassPathResource("json/mypush-2680e-firebase-adminsdk-dgxzc-cb58899903.json");

            serviceAccount = new FileInputStream(propertyService.getString("json.path")+"/"+appJson);


            //serviceAccount = resource.getInputStream();
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            firebaseApp = FirebaseApp.initializeApp(options, appId);
        } catch (FileNotFoundException e) {
            logger.error("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            logger.error("FirebaseOptions IOException" + e.getMessage());
        }finally {
            try {
                serviceAccount.close();
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return firebaseApp;
    }


    public void initApp(){

        try {
            List<TbAppVO> list = appDAO.selectTbAppList(null);

            for(TbAppVO vo :list) {

                FirebaseApp firebaseApp = appReset(vo.getAppId(), vo.getAppJson());
                if(firebaseApp != null) {
                    appMap.put(vo.getAppId(), firebaseApp);

                    logger.info(vo.getAppName() +" SUCCESS ....");

                }
            }
        }catch(Exception e){
            logger.error(ErrorUtils.getError(e));
        }
    }


    public String singleMessageSend(Message message, String appId){
        String response = "";
        System.out.println("\r\nOne message send ##################################################");
        // See documentation on defining a message payload.
        /*
        Message message01 = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken("dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HOl3")
                .build();
        */
        try {
            // Send a message to the device corresponding to the provided
            // registration token.
            FirebaseApp app = null;
            if("".equals(appId)){
                app = app01;
            }

            app = (FirebaseApp)appMap.get("appId");

            response = FirebaseMessaging.getInstance(app).send(message);
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + response);
        }catch(FirebaseMessagingException fex){
            fex.printStackTrace();
        }

        return response;
    }

    public BatchResponse multiMessageSend(MulticastMessage message, String appId){

        BatchResponse response = null;

        System.out.println("\r\nList message send ##################################################");
        /*MulticastMessage message = MulticastMessage.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .addAllTokens(registrationTokens)
                .build();
        */
        try {

            FirebaseApp app = null;
            if("".equals(appId)){
                app = app01;
            }
            app = (FirebaseApp)appMap.get("appId");

            response = FirebaseMessaging.getInstance(app).sendMulticast(message);
            List<SendResponse> list = response.getResponses();
            for(SendResponse response1 : list){
                System.out.println(response1.isSuccessful() + ":" + response1.getMessageId());
            }


            System.out.println(response.getSuccessCount() + " messages were sent successfully");
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return response;
    }

    public BatchResponse sendAllMessage(List<Message> messages, String appId){
        BatchResponse response = null;
        //System.out.println("\r\nSend All message send ##################################################");
        // Create a list containing up to 500 messages.
        /*
        List<Message> messages = Arrays.asList(
                Message.builder()
                        .setNotification(new Notification("Price drop", "5% off all electronics"))
                        .setToken("dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HO33")
                        .build(),
                // ...
                Message.builder()
                        .setNotification(new Notification("Price drop", "2% off all books"))
                        .setToken("dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HOl3")
                        .build()
        );*/

        try {
            FirebaseApp app = (FirebaseApp)appMap.get(appId);
            if(app == null){
                try {
                    TbAppVO paramVO = new TbAppVO();
                    paramVO.setAppId(appId);
                    TbAppVO appVo = appDAO.selectTbAppSingle(paramVO);
                    app = appReset(appVo.getAppId(), appVo.getAppJson());
                    if(app != null) {
                        appMap.put(appVo.getAppId(), app);
                    }
                }catch(Exception e){
                    logger.error(ErrorUtils.getError(e));
                }
            }

            response = FirebaseMessaging.getInstance(app).sendAll(messages);


        }catch (Exception ex){
            logger.error(ErrorUtils.getError(ex));
        }

        return response;
    }

}

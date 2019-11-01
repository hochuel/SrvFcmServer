package com.srv.send;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.srv.fileQueu.FileQueuMain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FcmSendProcess {

    private FirebaseApp app01 = null;

    public void appReset(){
        InputStream serviceAccount = null;
        FirebaseOptions options = null;
        try {
            ClassPathResource resource = new ClassPathResource("mypush-2680e-firebase-adminsdk-dgxzc-cb58899903.json");
            serviceAccount = resource.getInputStream();
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            app01 = FirebaseApp.initializeApp(options, "app01");
        } catch (FileNotFoundException e) {
            System.out.println("Firebase ServiceAccountKey FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            System.out.println("FirebaseOptions IOException" + e.getMessage());
        }
    }


    public String singleMessageSend(Message message, String appName){
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
            if("".equals(appName)){
                app = app01;
            }

            response = FirebaseMessaging.getInstance(app).send(message);
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + response);
        }catch(FirebaseMessagingException fex){
            fex.printStackTrace();
        }

        return response;
    }

    public BatchResponse multiMessageSend(MulticastMessage message, String appName){

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
            if("".equals(appName)){
                app = app01;
            }

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

    public BatchResponse sendAllMessage(List<Message> messages, String appName){
        BatchResponse response = null;
        System.out.println("\r\nSend All message send ##################################################");
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

            FirebaseApp app = null;
            if("".equals(appName)){
                app = app01;
            }

            response = FirebaseMessaging.getInstance(app).sendAll(messages);


        }catch (Exception ex){
            ex.printStackTrace();
        }

        return response;
    }

}

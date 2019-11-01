package com.srv;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.json.simple.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FcmSend {
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity httpEntity;

    private String fcmUrl = "https://fcm.googleapis.com/fcm/send";
    /*
    public FcmSend(){
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

    }
*/

    private FirebaseApp app01 = null;
    public FcmSend(){


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

    public void send(){
/*

        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Authorization", "key=" + authKey);

        httpEntity = new HttpEntity(jsonData, httpHeaders);
        JSONObject jsonObject = restTemplate.postForObject(fcmUrl, httpEntity, JSONObject.class);
        System.out.println("jsonData ::" + jsonObject);
*/

        // This registration token comes from the client FCM SDKs.
        //String registrationToken = "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HOl3";


        List<String> registrationTokens = Arrays.asList(
                "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HO33",
                "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HOl3"

        );


        System.out.println("\r\nOne message send ##################################################");
        // See documentation on defining a message payload.
        Message message01 = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken("dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HOl3")
                .build();
        try {
            // Send a message to the device corresponding to the provided
            // registration token.


            System.out.println(message01);

            String response = FirebaseMessaging.getInstance(app01).send(message01);
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + response);
        }catch(FirebaseMessagingException fex){
            fex.printStackTrace();
        }



        System.out.println("\r\nList message send ##################################################");
        MulticastMessage message = MulticastMessage.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .addAllTokens(registrationTokens)
                .build();
        try {
            BatchResponse response = FirebaseMessaging.getInstance(app01).sendMulticast(message);
// See the BatchResponse reference documentation
// for the contents of response.


            List<SendResponse> list = response.getResponses();

            for(SendResponse response1 : list){
                System.out.println(response1.isSuccessful() + ":" + response1.getMessageId());
            }


            System.out.println(response.getSuccessCount() + " messages were sent successfully");
        }catch(Exception ex){
            ex.printStackTrace();
        }

        System.out.println("\r\nSend All message send ##################################################");
        // Create a list containing up to 500 messages.
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
        );
        try {
            BatchResponse response = FirebaseMessaging.getInstance(app01).sendAll(messages);
        // See the BatchResponse reference documentation
        // for the contents of response.
            System.out.println(response.getSuccessCount() + " messages were sent successfully");



            List<SendResponse> list = response.getResponses();

            for(SendResponse response1 : list){
                System.out.println(response1.isSuccessful() + ":" + response1.getMessageId());
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }


/*
    public static void main(String[] args){


        JSONObject notification = new JSONObject();
        notification.put("to", "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HOl3");
        //notification.put("to", "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HO22");
        notification.put("collapse_key", "type_a");

        JSONObject msg = new JSONObject();
        msg.put("body", "This is Body");
        msg.put("title", "This is title");

        JSONObject data = new JSONObject();
        data.put("body", "This is Body");
        data.put("title", "title");


        notification.put("notification", msg);
        notification.put("data", data);

        JSONObject message = new JSONObject();
        message.putAll(notification);


        System.out.println("message ::" + message);

        FcmSend fcmSend = new FcmSend();
        fcmSend.send(message, "AAAAcAnNJGc:APA91bFMXEANTk8f4ssMhEJFP_V1A3-YSuFE3QcgQGkeWlFaV2iHRQB4DafQQllbvUXaB1ZztBNNvgbubv-yfzU0btCD3BvQLQDVOK6BkdDrrnY0rWmItpjYw1HGvu41KzHlwYesG0-X");

    }
    */



    public static void main(String[] args){
        FcmSend fcmSend = new FcmSend();
        fcmSend.send();
    }
}

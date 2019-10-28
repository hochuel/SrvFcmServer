package com.srv;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class FcmSend {
    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity httpEntity;

    private String fcmUrl = "https://fcm.googleapis.com/fcm/send";
    
    public FcmSend(){
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

    }

    public void send(JSONObject jsonData, String authKey){


        httpHeaders.set("Content-Type", "application/json");
        httpHeaders.set("Authorization", "key=" + authKey);

        httpEntity = new HttpEntity(jsonData, httpHeaders);
        JSONObject jsonObject = restTemplate.postForObject(fcmUrl, httpEntity, JSONObject.class);
        System.out.println("jsonData ::" + jsonData);
    }



    public static void main(String[] args){


        JSONObject notification = new JSONObject();
        notification.put("body", "This is Body");
        notification.put("title", "This is title");

        JSONObject message = new JSONObject();
        message.put("message", notification);


        System.out.println("message ::" + message);

        FcmSend fcmSend = new FcmSend();
        fcmSend.send(message, "asdfasdfasdf;lasd;lf ka;lsdkf;laksdf;lkasd;lfk");

    }
}

package com.srv;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.SendResponse;
import com.srv.util.DateUtil;
import com.srv.util.FileUtil;
import com.srv.util.PropertyService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MonitorApp implements Runnable{

    private int cnt = 0;
    private int time = 0;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Autowired
    private PropertyService propertyService;

    @Override
    public void run() {
        try {

            while(true) {
                String fileName = propertyService.getString("file.monitor");

                File file = new File(fileName);
                File[] isFiles = file.listFiles();

                for (int i = 0; i < isFiles.length; i++) {

                    if (isFiles[i] != null && isFiles[i].exists() && isFiles[i].getPath().indexOf("_monitor") < 0) {

                        //System.out.println(isFiles[i]);

                        byte[] data = FileUtil.fileRead(isFiles[i]);

                        monitorProcessFile(data);

                        String newFileName = DateUtil.getDate("yyyyMMdd")+"_monitor";

                        String[] temp = isFiles[i].getAbsolutePath().split("/");
                        temp[temp.length - 1] = newFileName;

                        String fName = "";
                        for(int x = 0; x < temp.length; x++){
                            fName += temp[x] + "/";
                        }

                        fileWrite(new File(fName.substring(0, fName.length() - 1)));

                        isFiles[i].delete();
                    }else{
                        isFiles[i] = null;
                    }
                }

                Thread.sleep(2000);
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void fileWrite(File file){
        JSONObject object = new JSONObject();
        object.put("cnt", this.cnt);
        object.put("time", this.time);

        int totalTime = cnt / (time / 1000);
        object.put("totalTime", totalTime);


        System.out.println(object.toJSONString());
        try {
            FileWriter f2 = new FileWriter(file, false);
            f2.write(object.toJSONString());
            f2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void monitorProcessFile(byte[] data){
        ByteArrayInputStream is = null;
        BufferedReader br = null;

        try {
            is = new ByteArrayInputStream(data);
            br = new BufferedReader(new InputStreamReader(is));


            String line = null;
            while ((line = br.readLine()) != null) {

                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);
                    cnt += Integer.parseInt(String.valueOf(jsonObject.get("cnt")));
                    time += Integer.parseInt(String.valueOf(jsonObject.get("time")));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }

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
}

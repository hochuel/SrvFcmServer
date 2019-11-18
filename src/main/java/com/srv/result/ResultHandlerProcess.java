package com.srv.result;

import com.srv.fileQueu.ReadHandler;
import com.srv.util.FileUtil;
import com.srv.vo.TbMsgVO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResultHandlerProcess implements ReadHandler {

    private List tbMsgVOList;

    private File file;


    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void setHandler(byte[] data, File file) {
        ByteArrayInputStream is = null;
        BufferedReader br = null;

        tbMsgVOList = new ArrayList();

        long endTime = 0;
        long startTime = 0;


        try {
            is = new ByteArrayInputStream(data);
            br = new BufferedReader(new InputStreamReader(is));

            int index = 0;
            String line = null;
            while ((line = br.readLine()) != null) {

                try {
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonObject = (JSONObject) jsonParser.parse(line);

                    boolean result = (Boolean) jsonObject.get("result");
                    String msgId = jsonObject.get("msgId").toString();
                    String title = jsonObject.get("title").toString();
                    String body = jsonObject.get("body").toString();
                    String token = jsonObject.get("token").toString();

//                    startTime = (Long) jsonObject.get("sendDt");
//                    endTime = (Long) jsonObject.get("resultDt");


                    TbMsgVO vo = new TbMsgVO();
                    vo.setMsgStatus(result?"02":"99");
                    vo.setMsgId(msgId);

                    tbMsgVOList.add(vo);

                    index++;
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }


            //final String str = file.getAbsolutePath() + ": cnt :" + index +": "+ (endTime - startTime) +"ms";
/*
            final JSONObject object = new JSONObject();
            object.put("cnt", index);
            object.put("time", (endTime - startTime));


            final String monitorFileName = file.getAbsolutePath().replaceAll("/data/result", "/data/monitor/");


            new Thread(){
                @Override
                public void run() {
                    try {
                        FileUtil.fileWrite(monitorFileName, object.toJSONString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }.start();
*/
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
        return tbMsgVOList;
    }

    @Override
    public void fileDelete() {

    }

    @Override
    public File getFile() {
        return file;
    }
}

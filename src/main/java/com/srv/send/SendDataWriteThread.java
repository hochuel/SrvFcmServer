package com.srv.send;

import com.srv.dao.MessageDAO;
import com.srv.fileQueu.FileQueuMain;
import com.srv.util.AtomicCustom;
import com.srv.util.DateUtil;
import com.srv.util.PropertyService;
import com.srv.vo.TbMsgVO;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SendDataWriteThread implements Runnable {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private AtomicCustom atomicCustom;

    private FileQueuMain fileQueuMain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public SendDataWriteThread(FileQueuMain fileQueuMain){

        this.fileQueuMain = fileQueuMain;
    }



    @Override
    public void run() {

        boolean startThread = true;
        while(startThread){
            try {

                String jsonString = "";
                List<TbMsgVO> resultList = messageDAO.selectTbMsg();
                if(resultList != null && resultList.size() > 0) {

                    for (TbMsgVO vo : resultList) {
                        JSONObject jsonObject = new JSONObject();

                        jsonObject.put("title", vo.getTitle());
                        jsonObject.put("body", vo.getContents());
                        jsonObject.put("token", vo.getToken());

                        jsonString += jsonObject.toJSONString() + "\r\n";
                    }



                /*
                for(int i = 0; i < 100; i++) {
                    JSONObject jsonObject = new JSONObject();

                    int index = atomicCustom.getIntData();

                    if(index > 100000){
                        startThread = false;
                    }

                    jsonObject.put("title", "Test title[" + index + "]");
                    jsonObject.put("body", "Test Body[" + index + "]");
                    jsonObject.put("token", "dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HO33");

                    jsonString += jsonObject.toJSONString()+"\r\n";
                }*/

                    String fileName = propertyService.getString("file.send") + "/" + name + "S" + DateUtil.getDate("yyyyMMddhhmmssSSS");
                    fileQueuMain.getFileQueu().fileWrite(fileName, true, name, jsonString);
                }
                Thread.sleep(1000);


            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

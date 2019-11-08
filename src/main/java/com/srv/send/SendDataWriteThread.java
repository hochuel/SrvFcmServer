package com.srv.send;

import com.srv.dao.MessageDAO;
import com.srv.fileQueu.FileQueuMain;
import com.srv.util.AtomicCustom;
import com.srv.util.DateUtil;
import com.srv.util.PropertyService;
import com.srv.vo.TbMsgVO;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

                String polKey = name + System.currentTimeMillis() + String.format("%05d", atomicCustom.getIntData());

                TbMsgVO param = new TbMsgVO();
                param.setPolKey(polKey);
                int cnt = messageDAO.updateTbMsgPolKey(param);
                //System.out.println(polKey +":" + cnt);
                //if(cnt > 0) {
                    List<TbMsgVO> resultList = messageDAO.selectTbMsg(param);
                    if (resultList != null && resultList.size() > 0) {

                        for (TbMsgVO vo : resultList) {
                            JSONObject jsonObject = new JSONObject();

                            jsonObject.put("msgId", vo.getMsgId());
                            jsonObject.put("title", vo.getTitle());
                            jsonObject.put("body", vo.getContents());
                            jsonObject.put("token", vo.getToken());
                            jsonObject.put("sendDt", System.currentTimeMillis());

                            jsonString += jsonObject.toJSONString() + "\r\n";

                            //System.out.println(name + ":" + jsonString);

                        }

                        //System.out.println(name + ":" + jsonString);

                        Map map = new HashMap();
                        map.put("list", resultList);

                      //  messageDAO.updateTbMsg(map);
                        messageDAO.insertTbMsgSend(map);
                        messageDAO.deleteTbMsg(map);


                        String fileName = propertyService.getString("file.send") + "/" + name + "S" + DateUtil.getDate("yyyyMMddhhmmssSSS");
                        fileQueuMain.getFileQueu().fileWrite(fileName, true, name, jsonString);
                    }
               // }


                Thread.sleep(1000);


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

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

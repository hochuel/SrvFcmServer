package com.srv.util;

import com.srv.dao.MessageDAO;
import com.srv.vo.TbMsgVO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataInsert {

    @Autowired
    private MessageDAO messageDAO;

    @Autowired
    private AtomicCustom atomicCustom;


    public void insertData(){

        try {
            Map map = new HashMap();

            List list = new ArrayList();
            for(int i = 0; i < 100; i++){
                TbMsgVO vo = new TbMsgVO();

                int index = atomicCustom.getIntData();

                String msgId = "MS" + System.currentTimeMillis() + String.format("%05d", index);
                vo.setMsgId(msgId);
                vo.setTitle("Test title[" + index + "]");
                vo.setContents("Test Body[" + index + "]");
                vo.setToken("dX3ZyKWUvh8:APA91bEIyzm8lg1HF4paL15knXld0wnY66dFyhlW5YKVuyUIQQzvmh1enrakV2idUDiJZdliTcwqyvlvdTBd-uGmLWO5-b4DgmcPydqjsEYorSXarksxvRmWA-SYWOp_M-m5Tnr5HO33");
                vo.setMsgStatus("00");

                list.add(vo);
            }

            map.put("list", list);

            messageDAO.insertTbMsg(map);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}

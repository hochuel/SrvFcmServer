package com.srv.result;

import com.srv.dao.MessageDAO;
import com.srv.fileQueu.FileQueuMain;
import com.srv.fileQueu.ReadHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultDataThread implements Runnable {

    @Autowired
    private MessageDAO messageDAO;

    private FileQueuMain fileQueuMain = null;

    public ResultDataThread(FileQueuMain fileQueuMain){
        this.fileQueuMain = fileQueuMain;
    }


    @Override
    public void run() {

        while(true){

            try {
                ReadHandler readHandler = new ResultHandlerProcess();

                fileQueuMain.getFileQueu().dataHandler(readHandler, "result");

                List list = (List) readHandler.get();
                if(list != null && list.size() > 0) {
                    Map map = new HashMap();
                    map.put("list", list);
                    messageDAO.updateTbMsgSend(map);
                }

                File file = readHandler.getFile();
                file.delete();
            }catch(Exception e){
                e.printStackTrace();
            }

        }

    }
}

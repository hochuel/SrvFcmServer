package com.srv.fileQueu;

import java.io.*;

public class ReadHandlerProcess implements ReadHandler{

    private File file = null;

    private Object resultObj = null;

    @Override
    public void setHandler(Object object, File file) {



        file.delete();
    }

    @Override
    public void put(Object obj) {
    }

    @Override
    public void fileDelete() {
    }

    @Override
    public Object get() {

        return resultObj;
    }

    @Override
    public void setHandler(byte[] data, File file) {

        ByteArrayInputStream is = null;
        BufferedReader br = null;

        try {
            is = new ByteArrayInputStream(data);
            br = new BufferedReader(new InputStreamReader(is));

            String str = "";
            String line = null;
            while ((line = br.readLine()) != null) {
                str += file.getAbsolutePath() +":" + line+"\r\n";
            }
            if(str != null && !"".equals(str)) {
                resultObj = str;
            }else{
                resultObj = null;
            }

            file.delete();

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

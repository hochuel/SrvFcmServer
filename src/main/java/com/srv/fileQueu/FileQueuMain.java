package com.srv.fileQueu;

import com.srv.util.FileUtil;
import com.srv.util.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingDeque;

public class FileQueuMain {


    private FileQueu fileQueu = null;

    private  int COMPARETYPE_NAME = 0;
    private  int COMPARETYPE_DATE = 1;

    private PropertyService propertyService;


    public FileQueuMain(PropertyService propertyService){


        this.propertyService = propertyService;

        fileQueu = new FileQueu();
        try {
            setFileLoad();
            setResultFileLoad();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public FileQueu getFileQueu(){
        return fileQueu;
    }



    public void setFileLoad() throws Exception{

        File[] files = FileUtil.getFileList(propertyService.getString("file.send"));

        if(files != null && files.length > 0) {
            File[] fileList = sortFileList(files, COMPARETYPE_DATE);
            for (File tempFile : fileList) {
                if (tempFile.isFile()) {
                    fileQueu.getDeque().put(tempFile);
                }
            }
        }
    }

    public void setResultFileLoad() throws Exception{
        File[] files = FileUtil.getFileList(propertyService.getString("file.result"));

        if(files != null && files.length > 0) {
            File[] fileList = sortFileList(files, COMPARETYPE_DATE);
            for (File tempFile : fileList) {
                if (tempFile.isFile()) {
                    fileQueu.getResutDeque().put(tempFile);
                }
            }
        }
    }



    public File[] sortFileList(File[] files, final int compareType) {

        Arrays.sort(files,
                new Comparator<Object>() {
                    @Override
                    public int compare(Object object1, Object object2) {

                        String s1 = "";
                        String s2 = "";

                        if (compareType == COMPARETYPE_NAME) {
                            s1 = ((File) object1).getName();
                            s2 = ((File) object2).getName();
                        } else if (compareType == COMPARETYPE_DATE) {
                            s1 = ((File) object1).lastModified() + "";
                            s2 = ((File) object2).lastModified() + "";
                        }


                        return s1.compareTo(s2);

                    }
                });

        return files;
    }

}

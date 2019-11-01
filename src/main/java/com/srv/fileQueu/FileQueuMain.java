package com.srv.fileQueu;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingDeque;

public class FileQueuMain {


    private FileQueu fileQueu = null;
    private LinkedBlockingDeque deque = null;


    private  int COMPARETYPE_NAME = 0;
    private  int COMPARETYPE_DATE = 1;



    private File file;
    private File[] files;

    public FileQueuMain(){

            deque = new LinkedBlockingDeque();


            fileQueu = new FileQueu();
            fileQueu.setQueu(deque);


            String path = "/home/dextop/data";
            file = new File(path);
            files = file.listFiles();


            try {
                setFileLoad();
                System.out.println("File " + deque.size() +" load...");

            }catch(InterruptedException ex){
                ex.printStackTrace();
            }

    }
    public LinkedBlockingDeque getDeque(){
        return deque;
    }

    public FileQueu getFileQueu(){
        return fileQueu;
    }



    public void setFileLoad() throws InterruptedException{
        File[] fileList = sortFileList(files, COMPARETYPE_DATE);


        for (File tempFile : fileList) {
            if (tempFile.isFile()) {
                deque.put(tempFile);
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

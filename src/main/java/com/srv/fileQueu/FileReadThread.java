package com.srv.fileQueu;

public class FileReadThread implements Runnable {

    private FileQueuMain fileQueuMain = null;

    public FileReadThread(FileQueuMain fileQueuMain){
        this.fileQueuMain = fileQueuMain;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void run(){

        while(true){
            try {

                ReadHandler readHandler = new ReadHandlerProcess();

                fileQueuMain.getFileQueu().dataHandler(readHandler);

                System.out.println(readHandler.get());

                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}

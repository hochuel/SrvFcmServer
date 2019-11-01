package com.srv.fileQueu;

import java.io.File;

public interface ReadHandler {

    public void setHandler(byte[] data, File file);

    public void setHandler(Object object, File file);

    public void put(Object obj);

    public Object get();

    public void fileDelete();
}

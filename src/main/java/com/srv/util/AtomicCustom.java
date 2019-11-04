package com.srv.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCustom {

    private AtomicInteger atomicInteger = null;

    public AtomicCustom(){
        atomicInteger = new AtomicInteger();

    }

    public int getIntData(){
        return atomicInteger.incrementAndGet();
    }
}

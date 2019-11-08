package com.srv.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCustom {

    private AtomicInteger atomicInteger = null;

    public AtomicCustom(){
        atomicInteger = new AtomicInteger();

    }

    public int getIntData(){

        int index = atomicInteger.incrementAndGet();

        if(index > 9000){
            atomicInteger = new AtomicInteger(0);
            index = atomicInteger.incrementAndGet();
        }

        return index;
    }
}

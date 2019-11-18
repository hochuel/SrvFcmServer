package com.srv.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtils {
    public static String getError(Exception e){
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }
}

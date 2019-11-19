package com.srv.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorUtils {
    public static String getError(Exception e){
        if(e != null) {
            StringWriter stringWriter = new StringWriter();
            e.printStackTrace(new PrintWriter(stringWriter));

            return stringWriter.toString();
        }

        return "";
    }
}

package org.jzl.android.recyclerview.util;

import android.util.Log;

public class Logger {

    private String tag;

    public static Logger logger(Class<?> type) {
        return logger(type.getCanonicalName());
    }

    public static Logger logger(String tag) {
        return new Logger(tag);
    }

    private Logger(String tag) {
        this.tag = tag;
    }

    public void d(String msg) {
        Log.d(tag, msg);
    }

    public void w(String msg){
        Log.w(tag, msg);
    }

    public void i(String msg){
        Log.i(tag, msg);
    }

    public void e(String msg){
        Log.e(tag, msg);
    }

    public void v(String msg){
        Log.v(tag, msg);
    }

}

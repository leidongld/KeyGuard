package com.example.leidong.keyguard.events;

/**
 * Created by leidong on 2017/10/15
 */

public class CryptoEvent {
    private String result, field;
    private int type;

    public CryptoEvent(String result, int type){
        this.result = result;
        this.type = type;
    }

    public CryptoEvent(String result, int type, String field){
        this.result = result;
        this.type = type;
        this.field = field;
    }

    public String getResult() {
        return result;
    }

    public int getType() {
        return type;
    }

    public String getField() {
        return field;
    }
}

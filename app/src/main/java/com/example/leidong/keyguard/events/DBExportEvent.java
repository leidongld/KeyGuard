package com.example.leidong.keyguard.events;

/**
 * Created by leidong on 2017/10/15
 */

public class DBExportEvent {
    public boolean success;
    public String filePath;

    public DBExportEvent(boolean success, String filePath) {
        this.success = success;
        this.filePath = filePath;
    }
}

package com.example.leidong.keyguard.runnable;

import android.content.Context;
import android.os.Environment;

import com.example.leidong.keyguard.events.DBExportEvent;
import com.example.leidong.keyguard.utils.AppConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 */

public class DBExportRunnable implements Runnable{
    private String dbSrcPath;
    private String dbDstPath;

    public DBExportRunnable(Context context) {
        dbSrcPath = context.getDatabasePath(AppConstants.DB_NAME).getAbsolutePath();
        dbDstPath = Environment.getExternalStorageDirectory() + "/data/data/keyguard/";
        File dstPath = new File(dbDstPath);
        if (!dstPath.exists()) {
            dstPath.mkdirs();
        }
        dbDstPath += "database.db";
    }

    @Override
    public void run() {
        try {
            File source = new File(dbSrcPath);
            File dest   = new File(dbDstPath);
            if (dest.exists()) {
                dest.delete();
            } else {
                dest.createNewFile();
            }
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(dest);
            byte[] cache = new byte[512];
            int byteRead;
            while ((byteRead = fis.read(cache)) > 0) {
                fos.write(cache, 0, byteRead);
            }
            fis.close();
            fos.close();
            EventBus.getDefault().post(new DBExportEvent(true, dest.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new DBExportEvent(false, null));
        }

    }
}

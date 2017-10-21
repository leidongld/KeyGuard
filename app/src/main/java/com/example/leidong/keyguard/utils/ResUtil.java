package com.example.leidong.keyguard.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.example.leidong.keyguard.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by leidong on 2017/10/15
 */

public class ResUtil {
    private static ResUtil ourInstance;
    private Context context;

    /**
     * 单例模式获取ResUtil的实例
     * @param context
     * @return
     */
    public static ResUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new ResUtil(context);
        }
        return ourInstance;
    }

    /**
     * 构造器
     * @param context
     */
    private ResUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取手机分辨率信息
     * @return
     */
    public float getDP(){
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 像素到dp的转换
     * @param point
     * @return
     */
    public int pointToDp(int point) {
        return (int) (point * getDP());
    }

    /**
     * 获取assets中的图片
     * @param assetName
     * @return
     * @throws IOException
     */
    public Bitmap getBmpInAssets(String assetName) throws IOException {
        return BitmapFactory.decodeStream(context.getResources().getAssets().open(assetName));
    }

    /**
     * 获取图标
     * @param fileName
     * @return
     * @throws IOException
     */
    public Bitmap getBmp(String fileName) throws IOException {
        if (fileName.startsWith("/")) {
            //File System
            return BitmapFactory.decodeStream(new FileInputStream(fileName));
        } else {
            //Assets
            return BitmapFactory.decodeStream(context.getResources().getAssets().open(fileName));
        }
    }

    /**
     * 通过Uri获取图标
     * @param fileName
     * @return
     */
    public Uri getBmpUri(String fileName) {
        if (fileName.startsWith("/")) {
            return Uri.fromFile(new File(fileName));
        } else {
            return Uri.parse("file:///android_asset/" + fileName);
        }

    }

    /**
     * 获取图片的路径
     * @param fileName
     * @return
     */
    public Object getBmpPath(String fileName) {
        if (fileName.startsWith("/")) {
            return fileName;
        } else {
            InputStream ret;
            try {
                ret =  context.getResources().getAssets().open(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
            return ret;
        }
    }

    /**
     * 展示进度条
     * @param activity
     * @param timeout
     * @param cancelable
     * @return
     */
    public AppCompatDialog showProgressbar(Activity activity, long timeout, boolean cancelable) {
        final AppCompatDialog dialog = new AppCompatDialog(activity);
        dialog.setContentView(R.layout.dialog_progress);
        dialog.setCancelable(cancelable);
        dialog.setTitle("Progressing...");
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progress);
        if (timeout > 0) {
            Handler handler = new Handler(activity.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    dialog.dismiss();
                }
            }, timeout);
            dialog.show();
        } else {
            dialog.show();
        }
        return dialog;
    }

    /**
     * 展示进度条
     * @param activity
     * @param timeout
     * @return
     */
    public AppCompatDialog showProgressbar(Activity activity, long timeout) {
        return showProgressbar(activity, timeout, true);
    }

    /**
     * 展示进度条
     * @param activity
     * @return
     */
    public AppCompatDialog showProgressbar(Activity activity) {
        return showProgressbar(activity, 0, false);
    }

    /**
     * 禁用软键盘
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

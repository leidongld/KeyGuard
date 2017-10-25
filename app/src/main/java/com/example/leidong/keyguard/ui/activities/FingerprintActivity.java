package com.example.leidong.keyguard.ui.activities;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.fingerprint.CryptoObjectHelper;
import com.example.leidong.keyguard.fingerprint.KeyGuardAuthCallback;

/**
 * Created by leidong on 2017/10/25
 */

public class FingerprintActivity extends Activity {
    private static final String TAG = "FingerPrintActivity";

    private FingerprintManagerCompat fingerprintManager = null;
    private KeyGuardAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;

    private Handler handler = null;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint);

        //获取组件
        init();


        try {
            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
            if (cancellationSignal == null) {
                cancellationSignal = new CancellationSignal();
            }
            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
                    cancellationSignal, myAuthCallback, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(FingerprintActivity.this, "指纹初始化失败，请您再试一次", Toast.LENGTH_SHORT).show();
        }

        handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d(TAG, "msg: " + msg.what + " ,arg1: " + msg.arg1);
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        cancellationSignal = null;
                        Intent welcomeIntent = new Intent(FingerprintActivity.this, MainActivity.class);
                        startActivity(welcomeIntent);
                        finish();
                        break;
                    case MSG_AUTH_FAILED:
                        //振动1s
                        Vibrator vibrator = (Vibrator)FingerprintActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                        cancellationSignal = null;
                        break;
                    case MSG_AUTH_ERROR:
                        break;
                    case MSG_AUTH_HELP:
                        break;
                }
            }
        };

        //初始化指纹认证
        fingerprintManager = FingerprintManagerCompat.from(FingerprintActivity.this);

        if (!fingerprintManager.isHardwareDetected()) {
            //没有找到指纹传感器，用通知栏通知用户
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_sensor_dialog_title);
            builder.setMessage(R.string.no_sensor_dialog_message);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else if (!fingerprintManager.hasEnrolledFingerprints()) {
            //系统中没有注册指纹
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_fingerprint_enrolled_dialog_title);
            builder.setMessage(R.string.no_fingerprint_enrolled_dialog_message);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.cancel_btn_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else {
            try {
                myAuthCallback = new KeyGuardAuthCallback(handler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取组件
     */
    private void init() {
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }
}

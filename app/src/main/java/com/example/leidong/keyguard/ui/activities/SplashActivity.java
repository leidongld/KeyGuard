package com.example.leidong.keyguard.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.UserDefault;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 */

public class SplashActivity extends AppCompatActivity{
    private boolean newInstall;
    private boolean noMainPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //得到UserDefault对象
        UserDefault userDefault = UserDefault.getInstance(this);

        //得到是否是第一次登录的标志
        newInstall = userDefault.getBoolean("newInstall");

        //得到无主密码的标志
        noMainPassword = userDefault.getBoolean("noMainPassword");

        //注册订阅者
        EventBus.getDefault().register(this);

        //如果是第一次使用
        if (newInstall) {
            //跳转到介绍界面7
            startActivity(new Intent(this, KeyGuardIntroActivity.class));
            //设置第一次使用的标志为false
            userDefault.save("newInstall", false);
            //关闭当前SplashActivity
            finish();
        }
        //如果不是第一次使用
        else {
            setContentView(R.layout.activity_splash);
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //如果没有主密码
                    if (UserDefault.getInstance(null).needPasswordWhenLaunch()) {
                        //跳转到注册界面
                        startActivity(new Intent(SplashActivity.this, AuthorizeActivity.class));
                    }
                    //如果主密码已经存在，跳转到主界面
                    else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    @Override
    public void finish() {
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(Object event) {
        if (!(event instanceof CryptoEvent)) {
            return;
        }
        switch (((CryptoEvent) event).getType()) {
            case AppConstants.TYPE_MASTERPWD:
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
                break;
            default:
                break;
        }
    }
}

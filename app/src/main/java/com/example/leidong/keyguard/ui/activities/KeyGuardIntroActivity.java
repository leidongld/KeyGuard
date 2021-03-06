package com.example.leidong.keyguard.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.leidong.keyguard.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Created by leidong on 2017/10/15
 */

public class KeyGuardIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //第一个Slide界面以及相关配置
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_one)
                .image(R.drawable.onboarding_slide_one)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        //第二个Slide界面以及相关配置
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_two)
                .image(R.drawable.onboarding_slide_two)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        //第三个Slide界面以及相关配置
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_three)
                .image(R.drawable.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        setSkipEnabled(false);
        setFullscreen(true);
        setAllowFinish(false);

        //创建新线程
        setRunWhenFinish(new Runnable() {
            @Override
            public void run() {
                //跳转到MainActivity
                startActivity(new Intent(KeyGuardIntroActivity.this, MainActivity.class));
                //关闭当前KeyGuardIntroActivity
                KeyGuardIntroActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

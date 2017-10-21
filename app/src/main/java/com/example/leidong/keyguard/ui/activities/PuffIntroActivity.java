package com.example.leidong.keyguard.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.leidong.keyguard.R;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

/**
 * Created by leidong on 2017/10/15
 */

public class PuffIntroActivity extends IntroActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_one)
                .image(R.drawable.onboarding_slide_one)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_two)
                .image(R.drawable.onboarding_slide_two)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title(R.string.title_intro_three)
                .image(R.drawable.ic_launcher)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorPrimaryDark)
                .build());

        setSkipEnabled(false);
        setFullscreen(true);
        setAllowFinish(false);
        setRunWhenFinish(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(PuffIntroActivity.this, MainActivity.class));
                PuffIntroActivity.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

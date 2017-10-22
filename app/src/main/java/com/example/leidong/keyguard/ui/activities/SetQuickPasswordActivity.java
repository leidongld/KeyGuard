package com.example.leidong.keyguard.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

import com.eftimoff.patternview.PatternView;
import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.Account;
import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.runnable.QuickPassRunnable;
import com.example.leidong.keyguard.utils.AppConstants;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 */

public class SetQuickPasswordActivity extends AppCompatActivity{
    public static final long ShowTypeSet         = 0x6001;
    public static final long ShowTypeVerify      = 0x6002;

    public static String hintStrSet;
    public static String hintStrVerify;

    private PatternView patternView;
    private AppCompatTextView hintTextView;
    private long type;
    private String masterPassword, quickCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_quick_password);
        EventBus.getDefault().register(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        hintStrSet = getString(R.string.set_gesture_password);
        hintStrVerify = getString(R.string.verify_gesture_password);

        patternView = (PatternView) findViewById(R.id.lock_view);
        hintTextView = (AppCompatTextView) findViewById(R.id.hint_view);
        type = getIntent().getLongExtra("type", ShowTypeSet);

        //手势密码监听
        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                quickCode = patternView.getPatternString();
                if (type == ShowTypeVerify) {
                    checkQuickPass();
                }
            }
        });

        //手势锁认证
        if (type == ShowTypeVerify) {
            masterPassword = getIntent().getStringExtra("masterPassword");
            fab.setVisibility(View.INVISIBLE);
            hintTextView.setText(hintStrVerify);
        }
        //注册手势锁
        else {
            fab.setVisibility(View.VISIBLE);
            hintTextView.setText(hintStrSet);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetQuickPasswordActivity.this, AuthorizeActivity.class));
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private boolean validateInput() {
        // TODO: 16/6/28 Validate
        return true;
    }

    private void saveQuickPass() {
        new Thread(new QuickPassRunnable(quickCode, masterPassword))
                .run();
    }

    private void checkQuickPass() {
        new Thread(new QuickPassRunnable(quickCode)).run();
    }

    public void onEventMainThread(Object event) {
        if (!(event instanceof CryptoEvent)) {
            return;
        }
        if (((CryptoEvent) event).getField() == null && type != ShowTypeSet) {
            patternView.setPattern(PatternView.DisplayMode.Wrong, patternView.getPattern());
            return;
        }
        switch (((CryptoEvent) event).getType()) {
            case AppConstants.TYPE_DECRYPT :
                finish();
                break;
            case AppConstants.TYPE_ENCRYPT :
                Account save = new Account();
                save.setType(AppConstants.TYPE_QUICK);
                save.setHash(((CryptoEvent) event).getResult());

                save.setSalt("");
                save.setName("");
                save.setTag("");

                AccountHelper.getInstance(null).saveAccount(save);
                setResult(RESULT_OK);
                finish();
                break;
            case AppConstants.TYPE_MASTERPWD:
                masterPassword = ((CryptoEvent) event).getResult();
                if (type == ShowTypeSet)
                    saveQuickPass();
                break;
            case AppConstants.TYPE_SHTHPPN:
                patternView.setPattern(PatternView.DisplayMode.Wrong, patternView.getPattern());
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}

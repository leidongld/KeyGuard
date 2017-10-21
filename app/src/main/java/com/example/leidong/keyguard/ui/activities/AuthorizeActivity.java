package com.example.leidong.keyguard.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.Account;
import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.runnable.PBKDFRunnable;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.ResUtil;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 * EventBus的学习？？？？？？
 */

public class AuthorizeActivity extends AppCompatActivity{
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Account master;
    private AppCompatDialog dialog;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        mPasswordView = (EditText) findViewById(R.id.password);//密码输入框
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * 按钮点击触发
     * 尝试登录或注册登录表单指定的帐户。
     * 如果存在表单错误（无效的电子邮件，缺少的字段等），则会显示错误，并且不会进行实际的登录尝试。
     */
    private void attemptLogin() {

        // 复位错误
        mPasswordView.setError(null);

        // 获取用户输入的密码
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 密码不能为空且必须大于若干位
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        if (cancel) {
            // 存在错误; 阻止登录
            focusView.requestFocus();
        }
        else {
            //判断用户输入的密码是否正确
            // TODO: 16/4/4 Check master password is correct or not.
            dialog = ResUtil.getInstance(this).showProgressbar(this);
            //获取已存在的主账户信息
            master = AccountHelper.getInstance(this).getMasterAccount();
//            new Thread(new CryptoRunnable(master.getHash(), password, AppConstants.TYPE_DECRYPT, "master"))
//                    .start();
            new Thread(new PBKDFRunnable(password, master.getHash())).start();
        }
    }

    /**
     * 密码长度哦必须大于6
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void onEventMainThread(CryptoEvent event) {
        if (event.getField() == null ||!event.getField().equalsIgnoreCase("master")) {
            return;
        }
        if (event.getType() == AppConstants.TYPE_MASTER_OK) {
            dialog.dismiss();
            CryptoEvent result = new CryptoEvent(password, AppConstants.TYPE_MASTERPWD);
            EventBus.getDefault().post(result);
            this.setResult(RESULT_OK);
            finish();
        }
        if (event.getType() == AppConstants.TYPE_MASTER_NO) {
            mPasswordView.setError(getResources().getString(R.string.master_password_invalid));
            dialog.dismiss();
        }
        if (event.getType() == AppConstants.TYPE_SHTHPPN) {
            mPasswordView.setError(getResources().getString(R.string.master_password_invalid));
            dialog.dismiss();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        CryptoEvent result = new CryptoEvent("", AppConstants.TYPE_CANCELED);
        EventBus.getDefault().post(result);
        EventBus.getDefault().unregister(this);
        this.setResult(RESULT_CANCELED);
        finish();
    }
}

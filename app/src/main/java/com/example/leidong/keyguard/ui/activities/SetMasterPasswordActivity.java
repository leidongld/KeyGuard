package com.example.leidong.keyguard.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.Account;
import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.runnable.ChangePasswordRunnable;
import com.example.leidong.keyguard.runnable.PBKDFRunnable;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.MasterPasswordFormatUtil;
import com.example.leidong.keyguard.utils.OtherUtil;
import com.example.leidong.keyguard.utils.ResUtil;

import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15、
 * 注册或者修改主密码
 */

public class SetMasterPasswordActivity extends AppCompatActivity {
    enum ShowMode {
        ShowModeAdd,
        ShowModeChange,
    }

    private AppCompatEditText passwd, confirm;
    private AppCompatImageView confirmImgView;
    private TextView helpText;
    private final String uuid = UUID.randomUUID().toString();
    private ShowMode showMode;
    private String oldPassword;
    private AppCompatDialog dialog;
    private FloatingActionButton fab;

    private static final int REQ_CODE_AUTH_MASTER   = 0x7001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_master_password);

        //获取组件
        initViews();

        //初始化动作
        initActions();

        showMode = (ShowMode) getIntent().getSerializableExtra("showMode");

        //如果是更改密码则获取传递的旧密码
        if (showMode == ShowMode.ShowModeChange) {
            oldPassword = getIntent().getStringExtra("oldPassword");
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String err = validatePassword();
                if (err != null){
                    OtherUtil.closeSoftInput(SetMasterPasswordActivity.this, passwd);
                    helpText.setText(err);
                    return;
                }
                if (showMode == ShowMode.ShowModeAdd){//如果是添加主密码
                    new Thread(new PBKDFRunnable(passwd.getText().toString())).start();
                }
                else {//如果是更改主密码
                    dialog = ResUtil.getInstance(null).showProgressbar(SetMasterPasswordActivity.this);
                    new Thread(new ChangePasswordRunnable(oldPassword, passwd.getText().toString())).run();
                }
            }
        });
    }

    /**
     * 验证密码合法性
     * @return 提示
     */
    private String validatePassword(){
        if (!passwd.getText().toString().equalsIgnoreCase(confirm.getText().toString())) {
            return getString(R.string.password_dont_match_em);
        }
        if (passwd.getText().length() < 6) {
            return getString(R.string.password_is_too_short_em);
        }
        if (!MasterPasswordFormatUtil.isStrongPassword(passwd.getText().toString())){
            return getString(R.string.password_is_not_strong);
        }
        return null;
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
    public void finish(){
        EventBus.getDefault().unregister(this);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (showMode == ShowMode.ShowModeChange) {
            finish();
        }
    }

    public void onEventMainThread(CryptoEvent event) {
        if (!event.getField().equalsIgnoreCase("master")) {
            return;
        }
        if (showMode == ShowMode.ShowModeChange && event.getType() == AppConstants.TYPE_MASTER_CHANGE) {
            dialog.dismiss();
            finish();
            return;
        }
        if (event.getType() == AppConstants.TYPE_ENCRYPT) {
            Account account = AccountHelper.getInstance(this).getMasterAccount();
            if (account == null) {
                account = new Account();
            }
            account.setHash(event.getResult());
            account.setSalt("");
            account.setName("");
            account.setType(AppConstants.TYPE_MASTER);
            account.setCategory(AppConstants.CAT_ID_PRIVATE);
            account.setTag("");
            AccountHelper.getInstance(null).saveAccount(account);
            finish();
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (reqCode != REQ_CODE_AUTH_MASTER)
            return;
        if (resultCode != RESULT_OK) {
            finish();
        }
    }

    private void initViews(){
        passwd = (AppCompatEditText) findViewById(R.id.password);
        confirm = (AppCompatEditText) findViewById(R.id.confirm);
        confirmImgView = (AppCompatImageView) findViewById(R.id.confirm_img);
        helpText = (TextView) findViewById(R.id.help_text);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    private void initActions(){
        passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(confirm.getText().toString())) {
                    confirmImgView.setImageResource(R.drawable.ic_yes);
                } else {
                    confirmImgView.setImageResource(R.drawable.ic_no);
                }
            }
        });

        passwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    helpText.setText(R.string.enter_main_password);
                }
            }
        });

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(passwd.getText().toString())) {
                    confirmImgView.setImageResource(R.drawable.ic_yes);
                } else {
                    confirmImgView.setImageResource(R.drawable.ic_no);
                }
            }
        });
        confirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    helpText.setText(R.string.confirm_main_password);
                }
            }
        });
    }
}

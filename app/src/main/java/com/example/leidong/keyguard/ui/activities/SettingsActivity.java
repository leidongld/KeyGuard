package com.example.leidong.keyguard.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.widget.Toast;
import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.events.DBExportEvent;
import com.example.leidong.keyguard.runnable.DBExportRunnable;
import com.example.leidong.keyguard.ui.views.SelectorItem;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.ResUtil;
import com.example.leidong.keyguard.utils.UserDefault;
import com.kenumir.materialsettings.MaterialSettings;
import com.kenumir.materialsettings.items.CheckboxItem;
import com.kenumir.materialsettings.items.HeaderItem;
import com.kenumir.materialsettings.items.TextItem;
import com.kenumir.materialsettings.storage.StorageInterface;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 */

public class SettingsActivity extends MaterialSettings{
    public static final int RequestCodeSetMainPassword = 0x700;
    private TextItem quickSwitcher;
    private TextItem fingerprintSwitcher;
    private SelectorItem selectorItem;
    private AppCompatDialog dialog;
    private boolean didClickedChangeMaster;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.settings);

        EventBus.getDefault().register(this);

        addItem(new HeaderItem(this).setTitle(getString(R.string.security)));
        //登录时是否需要主密码
        addItem(new CheckboxItem(this, UserDefault.kNeedPasswordWhenLaunch).setTitle(getString(R.string.need_password_when_launch)).setOnCheckedChangeListener(new CheckboxItem.OnCheckedChangeListener() {
            @Override
            public void onCheckedChange(CheckboxItem cbi, boolean isChecked) {
                UserDefault.getInstance(null).setNeedPasswordWhenLaunch(isChecked);
            }
        }));

        /**********************************************/
        //根据有没有快捷密码判断是否启用快捷密码模式
        String title1 = UserDefault.getInstance(null).hasQuickPassword() ? getString(R.string.disable_gesture_lock) : getString(R.string.enable_gesture_lock);
        quickSwitcher = new TextItem(this, "quick_pass").setTitle(title1).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                //手势密码已经存在则点击会删除掉该手势密码
                if (UserDefault.getInstance(null).hasQuickPassword()) {
                    UserDefault.getInstance(null).clearQuickPassword();
                    quickSwitcher.updateTitle(getString(R.string.enable_gesture_lock));
                }
                //手势密码不存在则点击会跳转到手势密码的设置界面
                else {
                    Intent intent = new Intent(SettingsActivity.this, SetQuickPasswordActivity.class);
                    intent.putExtra("type", SetQuickPasswordActivity.ShowTypeSet);
                    startActivityForResult(intent, RequestCodeSetMainPassword);
                }
            }
        });
        addItem(quickSwitcher);

        //根据有没有快捷密码判断是否启用快捷密码模式
        String title2 = UserDefault.getInstance(null).hasFingerprint() ? getString(R.string.disable_fingerprint_lock) : getString(R.string.enable_fingerprint_lock);

        fingerprintSwitcher = new TextItem(this, "fingerprint_pass").setTitle(title2).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                //指纹已经存在则点击会取消指纹锁
                if (UserDefault.getInstance(null).hasQuickPassword()) {
                    UserDefault.getInstance(null).clearQuickPassword();
                    quickSwitcher.updateTitle(getString(R.string.enable_gesture_lock));
                }
                //指纹不存在则点击会打开指纹锁
                else {

                }
            }
        });
        addItem(fingerprintSwitcher);
        /**********************************************/

        didClickedChangeMaster = false;
        addItem(new TextItem(this, "change_password").setTitle(getString(R.string.change_master_password)).setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                didClickedChangeMaster = true;
                startActivity(new Intent(SettingsActivity.this, AuthorizeActivity.class));
            }
        }));

        addItem(new HeaderItem(this).setTitle(getString(R.string.puff_secure_keyboard)));
        //设置自定义的输入法
        addItem(new TextItem(this, "ime").setTitle(getString(R.string.enable_puff_secure_keyboard)). setOnclick(new TextItem.OnClickListener() {
            @Override
            public void onClick(TextItem textItem) {
                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
                Toast.makeText(SettingsActivity.this, getString(R.string.please_enable_puf_ime), Toast.LENGTH_LONG).show();
            }
        }));

        addItem(new HeaderItem(this).setTitle(getString(R.string.database)));
        addItem(new TextItem(this, "database").setTitle(getString(R.string.export_database))
                .setSubtitle(getString(R.string.database_file_can_be_used))
                .setOnclick(new TextItem.OnClickListener() {
                    @Override
                    public void onClick(TextItem textItem) {

                        new AlertDialog.Builder(SettingsActivity.this).setTitle(getString(R.string.export_database))
                                .setMessage(R.string.confrim_export_database)
                                .setCancelable(false)
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface d, int which) {
                                        d.dismiss();
                                        dialog = ResUtil.getInstance(null).showProgressbar(SettingsActivity.this);
                                        DBExportRunnable runnable = new DBExportRunnable(SettingsActivity.this);
                                        new Thread(runnable).run();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                    }
                }));

    }

    @Override
    public void onResume() {
        super.onResume();
        quickSwitcher.updateTitle(UserDefault.getInstance(null).hasQuickPassword()
                ? getString(R.string.disable_gesture_lock)
                : getString(R.string.enable_gesture_lock));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    public void onEventMainThread(Object event) {
        if (event instanceof CryptoEvent && didClickedChangeMaster) {
            didClickedChangeMaster = false;
            if (((CryptoEvent) event).getType() == AppConstants.TYPE_MASTERPWD) {
                Intent intent = new Intent(SettingsActivity.this, SetMasterPasswordActivity.class);
                intent.putExtra("showMode", SetMasterPasswordActivity.ShowMode.ShowModeChange);
                intent.putExtra("oldPassword", ((CryptoEvent) event).getResult());
                UserDefault.getInstance(null).clearQuickPassword();
                AccountHelper.getInstance(null).clearQuickAccount();
                startActivity(intent);
            }
            return;
        }
        if (dialog != null) {
            dialog.dismiss();
        }
        if (!(event instanceof DBExportEvent)) {
            return;
        }
        DBExportEvent dbExportEvent = (DBExportEvent) event;
        if (dbExportEvent.success) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.success_em)
                    .setMessage(getString(R.string.database_exported_to)
                            + dbExportEvent.filePath)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle(R.string.failed_em)
                    .setMessage(R.string.please_try_again)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != RequestCodeSetMainPassword ) {
            return;
        }
        if (resultCode != RESULT_OK) {
            UserDefault.getInstance(null).clearQuickPassword();
        } else {
            UserDefault.getInstance(null).setHasQuickPassword();
        }
    }

    @Override
    public StorageInterface initStorageInterface() {
        return UserDefault.getInstance(this);
    }

}

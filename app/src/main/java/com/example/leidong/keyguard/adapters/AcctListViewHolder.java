package com.example.leidong.keyguard.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.db.Account;
import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.db.AcctType;
import com.example.leidong.keyguard.db.TypeHelper;
import com.example.leidong.keyguard.services.NotificationService;
import com.example.leidong.keyguard.ui.activities.DetailActivity;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.ClipboardUtil;
import com.example.leidong.keyguard.utils.CryptoUtil;
import com.example.leidong.keyguard.utils.ResUtil;
import com.example.leidong.keyguard.utils.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by leidong on 2017/10/13
 */

class AcctListViewHolder extends RecyclerView.ViewHolder{
    private View itemView;

    private float dp;

    private TextView name, accountName;
    private ImageView image;
    private Account account;
    private String iconStr;

    AcctListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        dp = itemView.getContext().getResources().getDisplayMetrics().density;
    }

    void configureWithAccount(final Account account) {
        this.account = account;
        ((TextView) itemView.findViewById(R.id.list_name)).setText(account.getName());
        ((TextView) itemView.findViewById(R.id.list_account_name)).setText(account.getMasked_account());
        AcctType type = TypeHelper.getInstance(null).getTypeById(account.getType());
        ((TextView) itemView.findViewById(R.id.list_account_category)).setText(type.getName());

        iconStr = account.getIcon();
        if (!StringUtil.isNullOrEmpty(iconStr)) {
            Picasso.with(this.itemView.getContext())
                    .load(ResUtil.getInstance(null).getBmpUri(account.getIcon()))
                    .fit()
                    .config(Bitmap.Config.RGB_565)
                    .into((ImageView) itemView.findViewById(R.id.list_account_image));
        } else {
            iconStr = TypeHelper.getInstance(null).getTypeById(account.getType()).getIcon();
            Uri icon = ResUtil.getInstance(null)
                    .getBmpUri(iconStr);
            Picasso.with(this.itemView.getContext())
                    .load(icon)
                    .fit()
                    .config(Bitmap.Config.RGB_565)
                    .into((ImageView) itemView.findViewById(R.id.list_account_image));
        }


        /**
         * 点击查看按钮
         */
        this.itemView.findViewById(R.id.view_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(itemView.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        //准备跳转到Account的详情界面
                        Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                        ArrayList<String> list = new ArrayList();
                        list.add(account);
                        list.add(passwd);
                        list.add(addt);
                        intent.putStringArrayListExtra("credentials", list);
                        intent.putExtra("account", AcctListViewHolder.this.account.getId());
                        //跳转到Account的详情界面
                        itemView.getContext().startActivity(intent);
                        updateAccess();
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });

        this.itemView.findViewById(R.id.pin_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(itemView.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        Intent intent = new Intent(itemView.getContext(), NotificationService.class);

                        itemView.getContext().stopService(intent);

                        intent.setAction(AppConstants.SERVICE_CMD_START);

                        intent.putExtra("name", AcctListViewHolder.this.account.getName());
                        intent.putExtra("account", account);
                        intent.putExtra("password", passwd);
                        intent.putExtra("additional", addt);
                        intent.putExtra("icon", iconStr);

                        itemView.getContext().startService(intent);
                        Snackbar.make(itemView,"Pinned!", Snackbar.LENGTH_SHORT).show();
                        updateAccess();
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });

        this.itemView.findViewById(R.id.copy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CryptoUtil(itemView.getContext(), new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        updateAccess();
                        ClipboardUtil.getInstance(itemView.getContext()).setText(passwd);
                        Snackbar.make(itemView,"Copied!", Snackbar.LENGTH_SHORT).show();
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
            }
        });
    }

    private void updateAccess() {
        account.setLast_access(Calendar.getInstance().getTimeInMillis());
        AccountHelper.getInstance(null).saveAccount(account);
    }
}

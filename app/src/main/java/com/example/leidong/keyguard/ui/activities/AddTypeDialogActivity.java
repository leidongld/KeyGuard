package com.example.leidong.keyguard.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.EditText;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.adapters.CategorySpinnerAdapter;
import com.example.leidong.keyguard.db.AcctType;
import com.example.leidong.keyguard.db.Category;
import com.example.leidong.keyguard.db.TypeHelper;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.EnvUtil;
import com.example.leidong.keyguard.utils.ResUtil;
import com.example.leidong.keyguard.utils.StringUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by leidong on 2017/10/15
 */

public class AddTypeDialogActivity extends AppCompatActivity{
    private AppCompatButton buttonOK, buttonCancel;
    private AppCompatSpinner categorySpinner;
    private AppCompatImageView imageView;
    private String imgPath;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type_dialog);
        categorySpinner = (AppCompatSpinner) findViewById(R.id.spinner_category);
        categorySpinner.setAdapter(new CategorySpinnerAdapter(this, 0));

        buttonOK = (AppCompatButton) findViewById(R.id.button_ok);
        ViewCompat.setElevation(buttonOK, 10);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (StringUtil.isNullOrEmpty(name)) {
                    return;
                }
                if (StringUtil.isNullOrEmpty(imgPath)) {
                    return;
                }
                AcctType type = new AcctType();
                type.setCategory(((Category) categorySpinner.getSelectedItem()).getId());
                type.setName(name);
                type.setIcon(imgPath);

                type.setMax_length(0);
                type.setNumbers_only(false);

                TypeHelper.getInstance(AddTypeDialogActivity.this).save(type);
                finish();
            }
        });

        buttonCancel = (AppCompatButton) findViewById(R.id.button_cancel);
        ViewCompat.setElevation(buttonCancel, 10);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(AddTypeDialogActivity.this)
                        .setCancelable(false)
                        .setTitle(R.string.cancel)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });

        imageView = (AppCompatImageView) findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");

                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("image/*");

                Intent chooserIntent = Intent.createChooser(getIntent, getResources().getString(R.string.select_image));
                chooserIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, new Intent[]{pickIntent});

                startActivityForResult(chooserIntent, AppConstants.REQUEST_CODE_IMAGE);
            }
        });

        etName = (EditText) findViewById(R.id.id_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case AppConstants.REQUEST_CODE_IMAGE:
                if (resultCode != RESULT_OK) {
                    return;
                }
                Uri imageUri = data.getData();
                Intent intent = new Intent(AddTypeDialogActivity.this,
                        ImageCropActivity.class);
                intent.setData(imageUri);
                startActivityForResult(intent, AppConstants.REQUEST_CODE_CROP);
                break;
            case AppConstants.REQUEST_CODE_CROP:
                if (resultCode != RESULT_OK) {
                    return;
                }
                String file = data.getStringExtra("cacheIcon");
                imgPath = EnvUtil.getInstance(null).getAcctIconFolder() + UUID.randomUUID().toString();
                FileOutputStream outputStream;
                FileInputStream inputStream;
                try {
                    outputStream = new FileOutputStream(imgPath);
                    inputStream = new FileInputStream(file);
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    imgPath = null;
                }
                Picasso.with(this).load(ResUtil.getInstance(null).getBmpUri(imgPath))
                        .fit()
                        .config(Bitmap.Config.RGB_565)
                        .into(imageView);
                new File(file).delete();
                break;
            case AppConstants.REQUEST_CODE_ADD_CATE:
                break;
            default:
                break;
        }
    }
}

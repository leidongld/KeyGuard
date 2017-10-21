package com.example.leidong.keyguard.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leidong.keyguard.R;
import com.example.leidong.keyguard.ui.activities.PasswordGenActivity;
import com.example.leidong.keyguard.utils.ClipboardUtil;
import com.example.leidong.keyguard.utils.PasswordGenerator;
import com.example.leidong.keyguard.utils.StringUtil;

import java.util.ArrayList;

/**
 * Created by leidong on 2017/10/15
 */

public class SecureStepDone extends SecureSlide.Fragment{
    AppCompatTextView passwordView;
    AppCompatButton reload, copy;
    ArrayList<String> words;
    PasswordGenActivity.PasswordType type;
    String password;
    int length;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);
        passwordView = (AppCompatTextView) ret.findViewById(R.id.password);
        reload = (AppCompatButton) ret.findViewById(R.id.button_reload);
        copy = (AppCompatButton) ret.findViewById(R.id.button_copy);

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePassword();
                passwordView.setText(password);
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardUtil.getInstance(getContext()).setText(passwordView.getText().toString());
                getActivity().finish();
            }
        });

        return ret;
    }

    public static SecureStepDone newInstance(@LayoutRes int layoutRes) {
        SecureStepDone fragment = new SecureStepDone();
        Bundle arguments = new Bundle();
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES", layoutRes);
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES", -1);
        fragment.setArguments(arguments);
        return fragment;
    }

    private void generatePassword() {
        password = "";
        if (words == null || words.size() == 0) {
            //No words provided.
            if (type == PasswordGenActivity.PasswordType.Number) {
                password = PasswordGenerator.getNumPassword(length, length);
            } else {
                password = PasswordGenerator.getPassword(length, length);
            }
        } else {
            for (String s : words) {
                password += StringUtil.getMaskedWord(s);
                if (password.length() > length) {
                    password = password.substring(0, length);
                    break;
                }
            }
        }
    }

    public void setPassword(String password) {
        this.password = password;
        this.length = password.length();
        this.passwordView.setText(password);
    }

    public SecureStepDone setWordsAndType(ArrayList<String> words, PasswordGenActivity.PasswordType type) {
        this.words = words;
        this.type = type;
        return this;
    }
}

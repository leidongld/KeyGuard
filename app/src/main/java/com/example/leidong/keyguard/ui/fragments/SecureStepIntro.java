package com.example.leidong.keyguard.ui.fragments;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leidong.keyguard.R;

/**
 * Created by leidong on 2017/10/15
 */

public class SecureStepIntro extends SecureSlide.Fragment{
    AppCompatEditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = super.onCreateView(inflater, container, savedInstanceState);
        editText = (AppCompatEditText) ret.findViewById(R.id.password_length);
        return ret;
    }

    public static SecureStepIntro newInstance(@LayoutRes int layoutRes) {
        SecureStepIntro fragment = new SecureStepIntro();
        Bundle arguments = new Bundle();
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_LAYOUT_RES", layoutRes);
        arguments.putInt("com.heinrichreimersoftware.materialintro.SimpleFragment.ARGUMENT_THEME_RES", -1);
        fragment.setArguments(arguments);
        return fragment;
    }

    public int getLength() {
        return Integer.valueOf(editText.getText().toString());
    }
}

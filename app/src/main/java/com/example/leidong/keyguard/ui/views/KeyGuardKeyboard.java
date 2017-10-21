package com.example.leidong.keyguard.ui.views;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

/**
 * Created by leidong on 2017/10/15
 */

public class KeyGuardKeyboard extends Keyboard{
    public KeyGuardKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    @Override
    protected Keyboard.Key createKeyFromXml(Resources res, Keyboard.Row parent, int x, int y,
                                            XmlResourceParser parser) {
        Keyboard.Key key = new KeyGuardKey(res, parent, x, y, parser);
        return key;
    }

    public void setIMEOptions(Resources res, int ImeOptions) {
        // TODO: 16/7/21 Impl
    }
}

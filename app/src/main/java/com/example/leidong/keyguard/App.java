package com.example.leidong.keyguard;

import android.app.Application;

import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.db.CategoryHelper;
import com.example.leidong.keyguard.db.TypeHelper;
import com.example.leidong.keyguard.utils.CategoryUtil;
import com.example.leidong.keyguard.utils.EnvUtil;
import com.example.leidong.keyguard.utils.ResUtil;
import com.example.leidong.keyguard.utils.UserDefault;


/**
 * Created by leidong on 2017/10/15
 */
public class App extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        AccountHelper.getInstance(getApplicationContext());
        CategoryHelper categoryHelper = CategoryHelper.getInstance(getApplicationContext());
        TypeHelper typeHelper = TypeHelper.getInstance(getApplicationContext());

        if (categoryHelper.getAllCategory() == null || categoryHelper.getAllCategory().size() == 0) {
            CategoryUtil.getInstance(getApplicationContext()).initBuiltInCategories();
        }

        if (typeHelper.getAllTypes() == null || typeHelper.getAllTypes().size() == 0) {
            CategoryUtil.getInstance(getApplicationContext()).initBuiltInTypes();
        }

        ResUtil.getInstance(getApplicationContext());
        CategoryUtil.getInstance(getApplicationContext());
        EnvUtil.getInstance(getApplicationContext());
        UserDefault.getInstance(getApplicationContext());

    }
}

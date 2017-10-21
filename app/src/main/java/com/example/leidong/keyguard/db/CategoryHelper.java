package com.example.leidong.keyguard.db;

import android.content.Context;

import com.example.leidong.keyguard.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leidong on 2017/10/15
 */

public class CategoryHelper {
    private static CategoryHelper ourInstance = null;
    private Context context;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private CategoryDao categoryDao;

    public static CategoryHelper getInstance(Context context) {
        if (ourInstance == null){
            ourInstance = new CategoryHelper(context);
        }
        return ourInstance;
    }

    private CategoryHelper(Context context) {
        this.context = context;
        getDaoMaster();
        getDaoSession();
        categoryDao = daoSession.getCategoryDao();

    }

    public DaoMaster getDaoMaster(){
        if (daoMaster == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, AppConstants.DB_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public DaoSession getDaoSession(){
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public ArrayList<Category> getAllCategory() {
        return (ArrayList<Category>) categoryDao.loadAll();
    }

    public Category getCategoryByName(String name){
        List<Category> result = categoryDao.queryBuilder()
                .where(CategoryDao.Properties.Name.eq(name))
                .list();
        return result == null ? null : result.get(0);
    }

    public Category getCategoryById(Long id) {
        List<Category> result = categoryDao.queryBuilder()
                .where(CategoryDao.Properties.Id.eq(id))
                .list();
        return result == null || result.size() == 0 ? null : result.get(0);
    }

    public long saveCategory(Category category) {
        if (category.getId() == null || getCategoryById(category.getId()) == null) {
            return categoryDao.insertOrReplace(category);
        }
        categoryDao.update(category);
        return category.getId();
    }

    public void deleteCategory(Category category) {
        categoryDao.delete(category);
    }

}

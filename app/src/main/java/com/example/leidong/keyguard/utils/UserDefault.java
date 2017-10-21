package com.example.leidong.keyguard.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kenumir.materialsettings.storage.StorageInterface;

import java.util.Map;

/**
 * Created by leidong on 2017/10/15
 * 一些密码是否存在的判断标志都是放在SharedPreference中的
 */

public class UserDefault extends StorageInterface{
    private Context context;
    private static UserDefault ourInstance;
    private SharedPreferences sharedPreferences;

    /**
     * 得到UserDefault实例
     * @param context
     * @return
     */
    public static UserDefault getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new UserDefault(context);
        return ourInstance;
    }

    /**
     * 构造器
     * @param context
     */
    private UserDefault(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserDefaultPrivate", Context.MODE_PRIVATE);
    }

    /**
     * 存入整形
     * @param key
     * @param value
     * @return
     */
    public UserDefault writeInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).commit();
        return this;
    }

    /**
     * 存入字符串
     * @param key
     * @param value
     * @return
     */
    public UserDefault writeString(String key, String value){
        sharedPreferences.edit().putString(key, value).commit();
        return this;
    }

    /**
     * 存入布尔型
     * @param key
     * @param value
     * @return
     */
    public UserDefault writeBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).commit();
        return this;
    }

    /**
     * 判断是否有快速登录密码
     * @return
     */
    public boolean hasQuickPassword() {
        return sharedPreferences.getBoolean(kSettingsHasQuickPassword, false);
    }

    /**
     * 设置有快速登录密码
     */
    public void setHasQuickPassword() {
        sharedPreferences.edit().putBoolean(kSettingsHasQuickPassword, true).commit();
    }

    /**
     * 清除快速登录密码
     */
    public void clearQuickPassword() {
        sharedPreferences.edit().putBoolean(kSettingsHasQuickPassword, false).commit();
    }

    /**
     * 通过传key得到整形
     * @param key
     * @return
     */
    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    /**
     * 通过传key得到布尔型
     * @param key
     * @return
     */
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    /**
     * 通过传key得到字符串
     * @param key
     * @return
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * 传60s
     * @return
     */
    public int getAutoClearTimeInSeconds() {
        return 60;
    }

    /**
     * SharedPreferences的数据存取
     * @param s
     * @param aBoolean
     */
    @Override
    public void save(String s, Boolean aBoolean) {
        sharedPreferences.edit().putBoolean(s, aBoolean).commit();
    }

    @Override
    public boolean load(String s, Boolean aBoolean) {
        return sharedPreferences.getBoolean(s, false);
    }

    @Override
    public void save(String s, String s1) {
        sharedPreferences.edit().putString(s, s1).commit();
    }

    @Override
    public String load(String s, String s1) {
        return sharedPreferences.getString(s, s1);
    }

    @Override
    public void save(String s, Integer integer) {
        sharedPreferences.edit().putInt(s, integer).commit();
    }

    @Override
    public Integer load(String s, Integer integer) {
        return sharedPreferences.getInt(s, integer);
    }

    @Override
    public void save(String s, Float aFloat) {
        sharedPreferences.edit().putFloat(s, aFloat).commit();
    }

    @Override
    public Float load(String s, Float aFloat) {
        return sharedPreferences.getFloat(s, aFloat);
    }

    @Override
    public void save(String s, Long aLong) {
        sharedPreferences.edit().putLong(s, aLong).commit();
    }

    @Override
    public Long load(String s, Long aLong) {
        return sharedPreferences.getLong(s, aLong);
    }

    /**
     * 得到SharedPreferences中的全部键值对
     * @return
     */
    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * 得到快速登录密码的
     * @return
     */
    public long getQuickPassByte() {
        return sharedPreferences.getLong(kSettingsQuickPassByte, v4x4);
    }

    /**
     * 提交快速登录密码的long形式
     * @param b
     */
    public void setQuickPassByte(long b) {
        sharedPreferences.edit().putLong(kSettingsQuickPassByte, b).commit();
    }

    public void setNeedPasswordWhenLaunch(boolean need) {
        sharedPreferences.edit().putBoolean(kNeedPasswordWhenLaunch, need).commit();
    }

    /**
     * 是否需要密码进行登录
     * @return
     */
    public boolean needPasswordWhenLaunch() {
        return sharedPreferences.getBoolean(kNeedPasswordWhenLaunch, false);
    }

    public static final String kSettingsHasQuickPassword    = "kSettingsHasQuickPassword";
    public static final String kSettingsQuickPassByte       = "kSettingsQuickPassByte";
    public static final String kNeedPasswordWhenLaunch      = "kNeedPasswordWhenLaunch";
    public static final long v3x3                           = 0x1033;
    public static final long v4x4                           = 0x1044;
}

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
     * @param context context
     * @return userDefault
     */
    public static UserDefault getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new UserDefault(context);
        return ourInstance;
    }

    /**
     * 构造器
     * @param context context
     */
    private UserDefault(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserDefaultPrivate", Context.MODE_PRIVATE);
    }

    /**
     * 存入整形
     * @param key key值
     * @param value value值
     * @return 当前UserDefault对象
     */
    public UserDefault writeInt(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
        return this;
    }

    /**
     * 存入字符串
     * @param key key值
     * @param value value值
     * @return 当前UserDefault对象
     */
    public UserDefault writeString(String key, String value){
        sharedPreferences.edit().putString(key, value).apply();
        return this;
    }

    /**
     * 存入布尔型
     * @param key key值
     * @param value value值
     * @return 当前UserDefault对象
     */
    public UserDefault writeBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
        return this;
    }

    /**
     * 判断是否有快速登录密码
     * @return value值
     */
    public boolean hasQuickPassword() {
        return sharedPreferences.getBoolean(kSettingsHasQuickPassword, false);
    }

    /**
     * 设置有快速登录密码
     */
    public void setHasQuickPassword() {
        sharedPreferences.edit().putBoolean(kSettingsHasQuickPassword, true).apply();
    }

    /**
     * 判断是否注册了指纹
     * @return 结果
     */
    public boolean hasFingerprint(){
        return sharedPreferences.getBoolean(kSettingsFingerprint, false);
    }

    /**
     * 设置有指纹
     */
    public void setHasFingerprint(){
        sharedPreferences.edit().putBoolean(kSettingsFingerprint, true).apply();
    }

    /**
     * 清除快速登录密码
     */
    public void clearQuickPassword() {
        sharedPreferences.edit().putBoolean(kSettingsHasQuickPassword, false).apply();
    }

    /**
     * 通过传key得到整形
     * @param key key值
     * @return value值
     */
    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    /**
     * 通过传key得到布尔型
     * @param key key值
     * @return value值
     */
    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, true);
    }

    /**
     * 通过传key得到字符串
     * @param key key值
     * @return value值
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    /**
     * 传60s
     * @return 60
     */
    public int getAutoClearTimeInSeconds() {
        return 60;
    }

    /**
     * SharedPreferences的布尔数据存
     * @param s key值
     * @param aBoolean value值
     */
    @Override
    public void save(String s, Boolean aBoolean) {
        sharedPreferences.edit().putBoolean(s, aBoolean).apply();
    }

    /**
     * SharedPreferences的布尔数据取
     * @param s key值
     * @param aBoolean 暂时无用
     * @return value值
     */
    @Override
    public boolean load(String s, Boolean aBoolean) {
        return sharedPreferences.getBoolean(s, false);
    }

    /**
     * SharedPreferences的字符串数据存
     * @param s key值
     * @param s1 value值
     */
    @Override
    public void save(String s, String s1) {
        sharedPreferences.edit().putString(s, s1).apply();
    }

    /**
     * SharedPreferences的字符串数据取
     * @param s key值
     * @param s1 默认值为null时的返回值
     * @return value值
     */
    @Override
    public String load(String s, String s1) {
        return sharedPreferences.getString(s, s1);
    }

    /**
     * SharedPreferences的字符串数据存
     * @param s key值
     * @param integer value值
     */
    @Override
    public void save(String s, Integer integer) {
        sharedPreferences.edit().putInt(s, integer).apply();
    }

    /**
     * SharedPreferences的字符串数据取
     * @param s key值
     * @param integer value值为null时的默认返回值
     * @return value值
     */
    @Override
    public Integer load(String s, Integer integer) {
        return sharedPreferences.getInt(s, integer);
    }

    /**
     * SharedPreferences的字符串数据存
     * @param s key值
     * @param aFloat value值
     */
    @Override
    public void save(String s, Float aFloat) {
        sharedPreferences.edit().putFloat(s, aFloat).apply();
    }

    /**
     * SharedPreferences的字符串数据取
     * @param s key值
     * @param aFloat value值为null时的默认返回值
     * @return value值
     */
    @Override
    public Float load(String s, Float aFloat) {
        return sharedPreferences.getFloat(s, aFloat);
    }

    /**
     * SharedPreferences的字符串数据存
     * @param s key值
     * @param aLong value值
     */
    @Override
    public void save(String s, Long aLong) {
        sharedPreferences.edit().putLong(s, aLong).apply();
    }

    /**
     * SharedPreferences的字符串数据取
     * @param s key值
     * @param aLong value值为null时的默认返回值
     * @return value值
     */
    @Override
    public Long load(String s, Long aLong) {
        return sharedPreferences.getLong(s, aLong);
    }

    /**
     * 得到SharedPreferences中的全部键值对
     * @return 当前SharedPreferences对应的全部key-value
     */
    @Override
    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    /**
     * 提交快速登录密码的long形式
     * @param b value值
     */
    public void setQuickPassByte(long b) {
        sharedPreferences.edit().putLong(kSettingsQuickPassByte, b).apply();
    }

    /**
     *设置登录时是否需要输入主密码的标志
     * @param need value值
     */
    public void setNeedPasswordWhenLaunch(boolean need) {
        sharedPreferences.edit().putBoolean(kNeedPasswordWhenLaunch, need).apply();
    }

    /**
     * 得到是否需要密码进行登录的标志
     * @return 是否需要密码进行登录的标志
     */
    public boolean needPasswordWhenLaunch() {
        return sharedPreferences.getBoolean(kNeedPasswordWhenLaunch, false);
    }


    /**
     * 一些用到的常量字符串
     */
    private static final String kSettingsHasQuickPassword    = "kSettingsHasQuickPassword";
    private static final String kSettingsQuickPassByte       = "kSettingsQuickPassByte";
    private static final String kSettingsFingerprint         = "kSettingsFingerprint";
    public static final String kNeedPasswordWhenLaunch      = "kNeedPasswordWhenLaunch";
    public static final long v3x3                           = 0x1033;
    public static final long v4x4                           = 0x1044;
}

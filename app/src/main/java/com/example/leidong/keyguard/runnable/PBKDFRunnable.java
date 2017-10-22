package com.example.leidong.keyguard.runnable;

import android.util.Base64;

import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.utils.AppConstants;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 */

public class PBKDFRunnable implements Runnable{
    private String password;
    private String hash;
    private int RUN_MODE;

    /**
     * 构造器1
     * @param password
     */
    public PBKDFRunnable(String password) {
        this.password = password;
        this.RUN_MODE = AppConstants.TYPE_ENCRYPT;
    }

    /**
     * 构造器2
     * @param password
     * @param hash
     */
    public PBKDFRunnable(String password, String hash) {
        this.password = password;
        this.hash = hash;
        this.RUN_MODE = AppConstants.TYPE_DECRYPT;
    }

    /**
     * 产生hash值
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private String hash() throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 128; //迭代次数
        char[] chars = password.toCharArray();

        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);

        //参数说明：
        //1.密码
        //2.盐
        //3.迭代次数
        //4.密钥长度
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        //拼接最终的hash结果返回
        return iterations + ":" + Base64.encodeToString(salt, Base64.DEFAULT) + ":" + Base64.encodeToString(hash, Base64.DEFAULT);
    }

    /**
     * 验证主密码是否正确
     * @return 主密码是否正确的标志
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private boolean validate() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = hash.split(":");//hash值提分段数据
        int iterations = Integer.parseInt(parts[0]);//提取迭代次数
        byte[] salt = Base64.decode(parts[1], Base64.DEFAULT);//提取盐
        byte[] hash = Base64.decode(parts[2], Base64.DEFAULT);//提取原始hash

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        //通过比较hash判断主密码是否匹配
        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    /**
     * 线程执行
     */
    @Override
    public void run() {
        CryptoEvent event = null;
        switch (RUN_MODE) {
            //解密模式，认证使用
            case AppConstants.TYPE_DECRYPT:
                try {
                    //主密码验证通过
                    if (validate()) {
                        event = new CryptoEvent("", AppConstants.TYPE_MASTER_OK, "master");
                    }
                    //主密码未验证通过
                    else {
                        event = new CryptoEvent("", AppConstants.TYPE_MASTER_NO, "master");
                    }
                }
                //异常情况下默认认证失败
                catch (Exception e) {
                    e.printStackTrace();
                    event = new CryptoEvent("", AppConstants.TYPE_MASTER_NO, "master");
                } finally {
                    EventBus.getDefault().post(event);
                }
                break;
            //加密模式，注册主密码使用
            case AppConstants.TYPE_ENCRYPT:
                try {
                    event = new CryptoEvent(hash(), AppConstants.TYPE_ENCRYPT, "master");
                } catch (Exception e) {
                    e.printStackTrace();
                    event = new CryptoEvent("", AppConstants.TYPE_SHTHPPN, "master");
                } finally {
                    EventBus.getDefault().post(event);
                }
                break;
            default:
                break;
        }
        EventBus.getDefault().unregister(this);
    }
}

package com.example.leidong.keyguard.runnable;

import android.util.Base64;

import com.example.leidong.keyguard.db.Account;
import com.example.leidong.keyguard.db.AccountHelper;
import com.example.leidong.keyguard.events.CryptoEvent;
import com.example.leidong.keyguard.utils.AppConstants;
import com.example.leidong.keyguard.utils.UserDefault;

import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import de.greenrobot.event.EventBus;

/**
 * Created by leidong on 2017/10/15
 */

public class QuickPassRunnable implements Runnable{
    private String quickCode, password, passwordHash, passwordSalt;
    private int type;


    public QuickPassRunnable(String quickCode) {
        this.quickCode = quickCode;
        getPasswordHash();
        type = AppConstants.TYPE_DECRYPT;
    }

    public QuickPassRunnable(String quickCode, String password) {
        this.quickCode = quickCode;
        this.password = password;
        type = AppConstants.TYPE_ENCRYPT;
    }

    private void getPasswordHash() {
        Account account = AccountHelper.getInstance(null).getQuickAccount();
        if (account == null) {
            UserDefault.getInstance(null).clearQuickPassword();
            AccountHelper.getInstance(null).clearQuickAccount();
            return;
        }
        this.passwordHash = account.getHash();
    }

    @Override
    public void run() {
        switch (type) {
            //验证手势密码是否正确
            case AppConstants.TYPE_DECRYPT:
                try {
                    EventBus.getDefault().post(new CryptoEvent(decrypt(), AppConstants.TYPE_MASTERPWD));
                } catch (Exception e) {
                    EventBus.getDefault().post(new CryptoEvent("", AppConstants.TYPE_SHTHPPN));
                    e.printStackTrace();
                }
                break;
            //注册手势密码
            case AppConstants.TYPE_ENCRYPT:
                try {
                    EventBus.getDefault().post(new CryptoEvent(encrypt(), AppConstants.TYPE_ENCRYPT));
                    UserDefault.getInstance(null).setHasQuickPassword();
                } catch (Exception e) {
                    EventBus.getDefault().post(new CryptoEvent("", AppConstants.TYPE_SHTHPPN));
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 加密
     * @return
     * @throws Exception
     */
    private String encrypt() throws Exception{

        int iterations = 128;

        byte[] salt = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(quickCode.toCharArray(), salt, iterations,
                256);
        SecretKey secretKey = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        AlgorithmParameters params = cipher.getParameters();

        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();

        byte[] output = cipher.doFinal(password.getBytes());

        String ret = iterations + ":"
                + Base64.encodeToString(output, Base64.DEFAULT) + ":"
                + Base64.encodeToString(salt, Base64.DEFAULT) + ":"
                + Base64.encodeToString(iv, Base64.DEFAULT);
        return ret;

    }

    /**
     * 解密
     * @return
     * @throws Exception
     */
    private String decrypt() throws Exception {

        String[] parts = passwordHash.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] hash = Base64.decode(parts[1], Base64.DEFAULT);
        byte[] salt = Base64.decode(parts[2], Base64.DEFAULT);
        byte[] iv   = Base64.decode(parts[3], Base64.DEFAULT);

        SecretKeyFactory factory = SecretKeyFactory
                .getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(quickCode.toCharArray(), salt, iterations,
                256);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));


        byte[] output = cipher.doFinal(hash);

        String ret = new String(output);

        return ret;
    }
}

package com.yxsd.mall.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtils {

    /**
     * AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
     */
    private static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS7Padding";
    /**
     * AES 加密
     */
    private static final String AES               = "AES";
    /**
     * SHA1PRNG 强随机种子算法
     */
    private static final String SHA1PRNG          = "SHA1PRNG";

    private static byte[] getRawKey(String key) throws Exception {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            byte[] passwordBytes = key.getBytes(StandardCharsets.US_ASCII);
            return InsecureSHA1PRNGKeyDerivator.deriveInsecureKey(passwordBytes, 32);
        } else {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
            SecureRandom secureRandom;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                secureRandom = SecureRandom.getInstance(SHA1PRNG, new CryptoProvider());
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                secureRandom = SecureRandom.getInstance(SHA1PRNG, "Crypto");
            } else {
                secureRandom = SecureRandom.getInstance(SHA1PRNG);
            }
            secureRandom.setSeed(key.getBytes());
            keyGenerator.init(128, secureRandom);
            // AES中128位密钥版本有10个加密循环，192比特密钥版本有12个加密循环，256比特密钥版本则有14个加密循环。
            SecretKey secretKey = keyGenerator.generateKey();
            return secretKey.getEncoded();
        }
    }

    /**
     * 加密
     */
    public static String encrypt(String key, String cleartext) {
        if (TextUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(key, cleartext.getBytes());
            return new String(Base64.encode(result, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] encrypt(String key, byte[] clear) throws Exception {
        byte[] raw = getRawKey(key);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(clear);
    }

    /**
     * 解密
     */
    public static String decrypt(String key, String encrypted) {
        if (TextUtils.isEmpty(encrypted)) {
            return encrypted;
        }
        try {
            byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] decrypt(String key, byte[] encrypted) throws Exception {
        byte[] raw = getRawKey(key);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        return cipher.doFinal(encrypted);
    }

    public static String encodeBase64(String str) {
        try {
            return Base64.encodeToString(str.getBytes("UTF-8"), Base64.DEFAULT).replace("\n", "");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encryptSha256(String str) {
        MessageDigest messageDigest;
        String encrypt;
        byte[] bytes = str.getBytes();
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(bytes);
            encrypt = bytes2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return encrypt;
    }

    private static String bytes2Hex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        String s;
        for (byte b : bytes) {
            s = (Integer.toHexString(b & 0xFF));
            if (s.length() == 1) {
                builder.append("0");
            }
            builder.append(s);
        }
        return builder.toString();
    }

    public static class CryptoProvider extends Provider {

        CryptoProvider() {
            super("Crypto", 1.0, "HARMONY (SHA1 digest; SecureRandom; SHA1withDSA signature)");
            put("SecureRandom.SHA1PRNG", "org.apache.harmony.security.provider.crypto.SHA1PRNG_SecureRandomImpl");
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }
}

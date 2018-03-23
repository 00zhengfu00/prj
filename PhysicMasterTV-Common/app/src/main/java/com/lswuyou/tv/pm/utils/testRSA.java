package com.lswuyou.tv.pm.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
/**
 *
 * @author zhiyong.xiongzy
 *
 */
public class testRSA {
    private static String prikey           = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMhDP5oVLHPJpM0QDuAGPupAkF4N3bMOD252BIYncKd8ppYJP0sA1wxnJxrVJN6UOzgK2qEReHUCzda5Hj+GaR8dmYz3v4UMozNeXhx7iqI7EK4Myp/qjCV2ROgh2LaL1ycugacTVx6dSfFvIOcczyhs8D4BUiJlSMsTWPVCSKsPAgMBAAECgYEAkL5KUgvLvUSZIL3ignkBMD7KGbDb9HbNIIGcc3o+KlAl93tni8ZnqEdGNFap91YcFz2BF7mQ3sHQFMpn2nJDtzj0tPsCKtNLHmpOi0ctAxedhzip+rOT6obNK+POL6czMWGJOaW8zLefxftgZWkT+eZactn507xc+GiwNsodAMECQQD4OjjnMhQWaSv8kHI27pH92PTv4zFb1msB0H65Im2zQ+8JrlqEH/7AqqMIxkPjIMC/vJLpkZG4TYMCYXjd4rx7AkEAzoiLGwUsXDApbDJJxxgYV6y50H/oJm6ooIOHoCwrluJhlOD81oUQyrLGfgNL95v4avvuQezZ4kmkeWDPvQ35fQJACBM8zqieZqw9NhYs4QSZ0zw/m540eNxc1s5FkRhBoVdQa2w0nZ+81d+3Ng3dH4JtQs+Lp/WIXAqJfIZXPECoCQJAW8QBgrTuu9tOXFuPul1zW2lMel6KiKD8Xa1zUnCtwXG+h8bbsHkZN+btGMpgM5libC6Z80LIoKm14ZRpWQffhQJBAJyxWEnzZA9dvqEhj6I/BTPJgvzc/boGzZ2lKQlN8j57MDkZHryoWtoKjB3Hp5dvTasweI6WHtIm313TSLuoXrg=";
    public static void main(String[] args) {
        String content = "c/BOhNHjhvCXvOenIyNqmr0FI1M3cYt/BR/WEoMgoi37ZFvKe418xOlRU4AN/QyvY2H7g2osL9oLmArsw35jk43reMFUyemTLnZ4Xw/DXT5xxPzHy4NWLt2/K0mqA2e5PiShf8RX9EdLnoIYZPXKe3Ldnw0ha3RZtP4X2tBsHqU=";
        String jsonStr;
        try {
            //解密
            jsonStr = decrypt(content, prikey, "UTF-8");
            System.out.println(jsonStr);
            //sign签名
            System.out.println(sign(prikey, content, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * RSA签名算法
     * @param privateKey
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    public static String sign(String privateKey, String content, String charset) throws Exception {
        PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey.getBytes()) );
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);

        java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(charset) );
        byte[] signed = signature.sign();
        return new String(Base64.encodeBase64(signed));
    }

    public static String decrypt(String content, String private_key, String input_charset) throws Exception {


        PrivateKey prikey = getPrivateKey(private_key);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);
        InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(content.getBytes()));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;
        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;
            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }
            writer.write(cipher.doFinal(block));
        }
        return new String(writer.toByteArray(), input_charset);
    }

    public static PrivateKey getPrivateKey(String key) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(key.getBytes());
        // 使用PKCS8
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

}
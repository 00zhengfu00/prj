package com.lswuyou.tv.pm.utils;


import com.lswuyou.tv.pm.security.Base64Decoder;
import com.lswuyou.tv.pm.security.Base64Encoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by Administrator on 2016/11/11.
 */
public class RSAUtils {
    /**
     * RSA签名算法
     *
     * @param privateKey
     * @param content
     * @param charset
     * @return
     * @throws Exception
     */
    public static String sign(String privateKey, String content, String charset) throws Exception {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64Decoder.decode(privateKey)
                .getBytes());
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);

        java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");
        signature.initSign(priKey);
        signature.update(content.getBytes(charset));
        byte[] signed = signature.sign();
        return Base64Encoder.encode(signed);
    }

    public static void main(String[] args) {
        String privateKey = "MIICXQIBAAKBgQC3ma65qDanrazrhQGum+AQQo1ZoGhC+ifKD8YviB27b+JxNEGs" +
                "v1k+8NRTJf1GbeKXBasIO3MO9nqvdrBVSmUAf5UE1w2QHAASfh7a+s0wNpC2S252" +
                "4oJCR5p2pcVyRyQW4uCSP1jo/n5yKwTfsP3ZWxTLNviJ8QP+MHzZRlS5rwIDAQAB" +
                "AoGBAIdmo2DwGT6X6srWkGMBOcFEE5JhpgKfEfEGqRYswCCXZtwtubF4fIzadvvL" +
                "LHpgV5cg6v1rOnNyexAj6+86qjjt3HpU6AnnhG9NJUDOAvYyUjNOEC3TGft1Ghw/" +
                "b3PZ+z3JUiAWAUZSEpS4pGTbyatrLstW5k/FljAt2t/RJamBAkEA8/vIIFQyG0hW" +
                "VlHFQ9tI7ZEmpdIaK4Wn2gPlwSZJBPtX0ITDqv79VYGZHlDupIYsksv5Vj7W9AV2" +
                "e8zfFWxYnwJBAMCkkjH0gX43Q7isZ4q6tfSb9BInSNhrpp2GiqfsU8XRlcv/bNKs" +
                "8ynnj79YEVYe3TX6Wg67/o/koWe4hyWLNPECQQDPO0+Lbhg1Jib3IHMuJKZ36m7q" +
                "L1+9EcQ1IFRYz1z8F0RKP0o3BmhE5VQZ9OIN6k0XzpqUj8Fh+PXfR29AMgu/AkBw" +
                "LmXEGXJfIaeaHyucqeXTsA2NlLri7K12BicwanrOM6JbzT594KEpweL/tjVCRW7k" +
                "RAm1ktxGxwkG8Xoh7tLBAkBF4XD1orSOysfDd95Y/W0Tynyy/9U8vU99MIW0gHHc" +
                "UTOVulKJAaKQWOrry8iHya8mCAJG2GE0VbgEMmIYYHwG";
        try {
            System.out.println(sign(privateKey, "hello", "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

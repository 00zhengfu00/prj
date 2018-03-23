package com.physicmaster.net.security;


import com.physicmaster.base.AppConfigure;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

/**
 * Created by wuqiang on 15-8-5.
 * <p/>
 * RSA加密解密及秘钥生成
 * 
 * @author wuqiang
 */
public final class RSAEncryption {
	
	public static RSAKeyPair rsaKeyOfficial = new RSAKeyPair(new byte[]{48,-126,1,34,48,13,6,9,42,-122,72,-122,-9,13,1,1,1,5,0,3,-126,1,15,0,48,-126,1,10,2,-126,1,1,0,-118,87,29,74,-63,13,-8,0,11,125,-49,-73,47,-1,-53,94,-76,61,18,-114,-27,6,118,26,27,39,-35,-109,5,3,-104,73,120,-69,-8,-104,89,-11,-86,-55,94,-16,10,-54,74,-109,-88,86,-96,-87,-13,66,127,-6,91,105,8,43,74,62,103,-5,-97,-4,105,53,-59,126,3,-33,-93,-86,-67,113,22,0,111,-44,-21,-10,77,-119,-72,-120,70,-34,64,95,-68,8,20,-46,36,-107,-66,123,-22,49,47,66,-116,-34,-6,-103,98,84,-37,-43,79,-79,40,44,-117,-70,-60,-82,-1,57,18,-68,113,-83,15,15,-118,64,66,45,1,0,-36,70,94,-58,-49,-61,117,108,4,-98,101,-113,108,25,31,-13,52,11,-111,-37,-33,26,-57,-53,-53,81,-53,52,-99,68,49,-104,6,-73,-113,-93,104,-37,-79,67,124,-8,-37,61,85,37,57,-97,28,-58,66,-57,-74,-51,107,-79,-109,95,66,-68,49,113,-61,-90,-15,-125,98,59,-105,-105,-10,-108,-111,-88,102,77,-4,-81,-19,87,22,-17,77,-20,126,35,-32,-49,-109,-99,59,-7,104,-126,36,65,-64,-97,23,-38,20,-17,48,-116,-127,-9,88,19,-46,72,-66,-98,-118,57,-72,91,64,17,-34,-94,11,-92,-118,-39,-30,115,2,3,1,0,1}, 2048, "#RSAPART#".getBytes());
	
	
	public static RSAKeyPair rsaKey = new RSAKeyPair(new byte[] { 48, -126, 1,
			34, 48, 13, 6,
			9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0,
			48, -126, 1, 10, 2, -126, 1, 1, 0, -91, 104, -6, -17, 32, -19, -5,
			114, -59, -72, 90, 64, -61, 98, 106, -86, 34, 34, -124, 51, -32,
			86, 43, 2, -93, 31, -98, 59, -104, 64, 31, 9, -1, 60, 7, 92, -126,
			54, 84, -48, 18, -39, -122, 112, 40, -116, -60, 28, 83, -96, 19,
			-72, -4, 67, -110, -7, -10, 8, -56, -81, -5, 88, -93, 93, 44, -48,
			-71, 117, 44, -69, 72, 92, 53, -27, -36, -118, 84, -103, 70, 81,
			100, 27, -95, 63, -46, 86, -109, -31, -81, 64, -80, -41, 102, -23,
			-66, 74, 19, 7, -26, 73, 43, 15, 25, 27, 15, -82, -128, -15, 37,
			34, 39, 23, 98, -101, 54, 1, 103, -113, -18, 42, 33, -39, 69, 35,
			25, -65, -88, -4, 102, -33, 10, 85, -16, -25, 124, -105, -94, -28,
			-8, 85, 7, -64, -112, 60, -18, 103, 13, -49, 28, -94, -70, 45,
			-126, 122, 69, 39, 105, 29, -27, -12, 94, 36, -98, -69, -53, 107,
			104, 116, -75, 30, 117, 102, 97, 47, 4, 105, 24, -73, 95, 50, 24,
			16, -119, -30, -78, 113, -80, -91, -71, -19, -84, 14, 25, 53, -55,
			-119, -16, 38, -10, -79, 69, 14, -105, -81, -122, -48, 56, -128,
			-101, -28, 126, -71, 11, 56, -3, -26, -19, 109, -48, -122, -41, 63,
			-43, 26, -30, -59, -109, -48, 106, 0, 4, 39, 40, 68, -3, 40, -71,
			-69, -30, -6, -46, 54, 75, -90, -17, -88, -91, 2, -19, -45, 74,
			-126, -46, 20, 102, 73, 2, 3, 1, 0, 1 }, 2048,
			"#RSAPART#".getBytes());
	
	public static RSAKeyPair getRSAKeyPair(){
		if (true == AppConfigure.officialVersion){
			return rsaKeyOfficial;
		}else{
			return rsaKey;
		}
	}

	private RSAEncryption() {
	}

	// 非对称加密密钥算法
	public static final String RSA_ALGORITHM = "RSA";
	// 填充方式
	public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";

	/**
	 * 用公钥对字符串进行分段加密
	 * 
	 * @param data
	 *            数据
	 * @param keyPair
	 *            密钥
	 */
	public static byte[] encryptByPublicKey(byte[] data, RSAKeyPair keyPair)
			throws Exception {
		byte[] publicKey = keyPair.getPublicKey();
		int bufferSize = keyPair.getBufferSize();
		byte[] partSplit = keyPair.getPartSplit();
		int dataLen = data.length;
		if (dataLen <= bufferSize) {
			return encryptByPublicKey(data, publicKey);
		}
		List<Byte> allBytes = new ArrayList<Byte>(2048);
		int bufIndex = 0;
		int subDataLoop = 0;
		byte[] buf = new byte[bufferSize];
		for (int i = 0; i < dataLen; i++) {
			buf[bufIndex] = data[i];
			if (++bufIndex == bufferSize || i == dataLen - 1) {
				subDataLoop++;
				if (subDataLoop != 1) {
					for (byte b : partSplit) {
						allBytes.add(b);
					}
				}
				byte[] encryptBytes = encryptByPublicKey(buf, publicKey);
				for (byte b : encryptBytes) {
					allBytes.add(b);
				}
				bufIndex = 0;
				if (i == dataLen - 1) {
					buf = null;
				} else {
					buf = new byte[Math.min(bufferSize, dataLen - i - 1)];
				}
			}
		}
		byte[] bytes = new byte[allBytes.size()];
		{
			int i = 0;
			for (Byte b : allBytes) {
				bytes[i++] = b.byteValue();
			}
		}
		return bytes;
	}

	/**
	 * 用公钥对字符串进行加密
	 * 
	 * @param data
	 *            原文
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] publicKey)
			throws Exception {
		// 得到公钥
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory kf = KeyFactory.getInstance(RSA_ALGORITHM);
		PublicKey keyPublic = kf.generatePublic(keySpec);
		// 加密数据
		Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
		cp.init(Cipher.ENCRYPT_MODE, keyPublic);
		return cp.doFinal(data);
	}

	/**
	 * 公钥解密
	 * 
	 * @param encrypted
	 *            待解密数据
	 * @param keyPair
	 *            密钥
	 */
	public static byte[] decryptByPublicKey(byte[] encrypted, RSAKeyPair keyPair)
			throws Exception {
		byte[] publicKey = keyPair.getPublicKey();
		byte[] partSplit = keyPair.getPartSplit();
		int splitLen = partSplit.length;
		if (splitLen <= 0) {
			return decryptByPublicKey(encrypted, publicKey);
		}
		int dataLen = encrypted.length;
		List<Byte> allBytes = new ArrayList<Byte>(1024);
		int latestStartIndex = 0;
		for (int i = 0; i < dataLen; i++) {
			byte bt = encrypted[i];
			boolean isMatchSplit = false;
			if (i == dataLen - 1) {
				// 到data的最后了
				byte[] part = new byte[dataLen - latestStartIndex];
				System.arraycopy(encrypted, latestStartIndex, part, 0,
						part.length);
				byte[] decryptPart = decryptByPublicKey(part, publicKey);
				for (byte b : decryptPart) {
					allBytes.add(b);
				}
				latestStartIndex = i + splitLen;
				i = latestStartIndex - 1;
			} else if (bt == partSplit[0]) {
				// 这个是以split[0]开头
				if (splitLen > 1) {
					if (i + splitLen < dataLen) {
						// 没有超出data的范围
						for (int j = 1; j < splitLen; j++) {
							if (partSplit[j] != encrypted[i + j]) {
								break;
							}
							if (j == splitLen - 1) {
								// 验证到split的最后一位，都没有break，则表明已经确认是split段
								isMatchSplit = true;
							}
						}
					}
				} else {
					// split只有一位，则已经匹配了
					isMatchSplit = true;
				}
			}
			if (isMatchSplit) {
				byte[] part = new byte[i - latestStartIndex];
				System.arraycopy(encrypted, latestStartIndex, part, 0,
						part.length);
				byte[] decryptPart = decryptByPublicKey(part, publicKey);
				for (byte b : decryptPart) {
					allBytes.add(b);
				}
				latestStartIndex = i + splitLen;
				i = latestStartIndex - 1;
			}
		}
		byte[] bytes = new byte[allBytes.size()];
		{
			int i = 0;
			for (Byte b : allBytes) {
				bytes[i++] = b.byteValue();
			}
		}
		return bytes;
	}

	/**
	 * 公钥解密
	 * 
	 * @param data
	 *            待解密数据
	 * @param publicKey
	 *            密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] publicKey)
			throws Exception {
		// 得到公钥
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		KeyFactory kf = KeyFactory.getInstance(RSA_ALGORITHM);
		PublicKey keyPublic = kf.generatePublic(keySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
		cipher.init(Cipher.DECRYPT_MODE, keyPublic);
		return cipher.doFinal(data);
	}
}

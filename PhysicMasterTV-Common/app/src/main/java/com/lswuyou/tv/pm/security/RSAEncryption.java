package com.lswuyou.tv.pm.security;


import com.lswuyou.tv.pm.AppConfigure;

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
	
	public static RSAKeyPair rsaKeyOfficial = new RSAKeyPair(new byte[]{48,-126,1,34,48,13,6,9,42,-122,72,-122,-9,13,1,1,1,5,0,3,-126,1,15,0,48,-126,1,10,2,-126,1,1,0,-99,-118,-111,117,-92,-122,106,60,-120,-70,-89,34,16,-53,104,-68,44,34,50,75,-8,108,-72,-122,58,-8,114,94,81,-96,-102,-60,32,-56,67,-36,-63,107,99,22,-115,45,26,-87,118,-121,-12,122,51,-125,45,-124,24,30,-79,-51,-17,-79,-4,-56,-111,96,-30,21,-7,-53,-108,-51,74,39,36,-41,-30,-80,-28,-91,62,-82,89,-94,-59,-54,-40,20,74,-56,-105,-9,107,-74,59,18,113,105,80,-75,-46,-76,-15,64,-89,-21,77,-62,-94,27,7,-91,74,-96,-76,16,83,-124,-120,-29,23,69,97,47,29,-51,124,-12,99,58,77,63,-94,-128,-128,93,-15,-60,108,110,27,97,84,34,-8,-20,-33,92,20,-124,-88,-115,-15,-90,52,-57,25,-81,120,-103,-74,90,-80,28,78,-10,24,-81,91,-86,-100,-34,50,103,38,39,75,-66,-44,85,-91,-83,49,-81,112,-5,-82,-116,103,72,78,110,69,17,-91,-33,25,12,71,89,-76,-61,-40,68,27,96,-97,-120,-88,44,96,-122,-93,70,20,-120,-19,-128,-87,34,-23,-126,83,-42,84,126,118,-1,-23,7,120,-33,-104,-122,-105,-93,-124,54,-1,17,10,-48,116,-15,18,-39,-10,32,97,59,70,-77,6,122,-2,-94,59,-49,-18,-39,2,3,1,0,1}, 2048, "#RSAPART#".getBytes());
	
	
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

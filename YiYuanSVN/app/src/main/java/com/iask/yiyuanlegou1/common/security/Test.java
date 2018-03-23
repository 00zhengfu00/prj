package com.iask.yiyuanlegou1.common.security;

public class Test {
	public static void testRSA(String encryptByPrivateKey) throws Exception {
		RSAKeyPair keyPair = new RSAKeyPair(new byte[] { 48, -126, 1, 34, 48,
				13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126,
				1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -91, 104, -6, -17,
				32, -19, -5, 114, -59, -72, 90, 64, -61, 98, 106, -86, 34, 34,
				-124, 51, -32, 86, 43, 2, -93, 31, -98, 59, -104, 64, 31, 9,
				-1, 60, 7, 92, -126, 54, 84, -48, 18, -39, -122, 112, 40, -116,
				-60, 28, 83, -96, 19, -72, -4, 67, -110, -7, -10, 8, -56, -81,
				-5, 88, -93, 93, 44, -48, -71, 117, 44, -69, 72, 92, 53, -27,
				-36, -118, 84, -103, 70, 81, 100, 27, -95, 63, -46, 86, -109,
				-31, -81, 64, -80, -41, 102, -23, -66, 74, 19, 7, -26, 73, 43,
				15, 25, 27, 15, -82, -128, -15, 37, 34, 39, 23, 98, -101, 54,
				1, 103, -113, -18, 42, 33, -39, 69, 35, 25, -65, -88, -4, 102,
				-33, 10, 85, -16, -25, 124, -105, -94, -28, -8, 85, 7, -64,
				-112, 60, -18, 103, 13, -49, 28, -94, -70, 45, -126, 122, 69,
				39, 105, 29, -27, -12, 94, 36, -98, -69, -53, 107, 104, 116,
				-75, 30, 117, 102, 97, 47, 4, 105, 24, -73, 95, 50, 24, 16,
				-119, -30, -78, 113, -80, -91, -71, -19, -84, 14, 25, 53, -55,
				-119, -16, 38, -10, -79, 69, 14, -105, -81, -122, -48, 56,
				-128, -101, -28, 126, -71, 11, 56, -3, -26, -19, 109, -48,
				-122, -41, 63, -43, 26, -30, -59, -109, -48, 106, 0, 4, 39, 40,
				68, -3, 40, -71, -69, -30, -6, -46, 54, 75, -90, -17, -88, -91,
				2, -19, -45, 74, -126, -46, 20, 102, 73, 2, 3, 1, 0, 1 }, 2048,
				"#RSAPART#".getBytes());
		String source = "世界你好password:123456";
		byte[] sourceBytes = source.getBytes("UTF-8");
		System.out.println("RSA - 原文：" + source);
		{
			byte[] encryptByPublicKeyBytes = RSAEncryption.encryptByPublicKey(
					sourceBytes, keyPair);
			String encryptByPublicKey = Base64Encoder.encode(
					encryptByPublicKeyBytes, false);
			System.out.println("RSA - 客户端使用公钥加密后密文（发给服务端测试） : \n"
					+ encryptByPublicKey);
		}
		{
			byte[] encryptByPrivateKeyBytes = Base64Decoder
					.decodeToBytes(encryptByPrivateKey);
			byte[] decryptByPublicKeyBytes = RSAEncryption.decryptByPublicKey(
					encryptByPrivateKeyBytes, keyPair);
			String decryptByPublicKey = new String(decryptByPublicKeyBytes);
			System.out.println("RSA - 客户端使用公钥解密后原文  : " + decryptByPublicKey);
			System.out.println("RSA - 是否与原文相同 : "
					+ source.equals(decryptByPublicKey));
		}
	}

	public static void testAES_android(String encryptedInServer)
			throws Exception {
		System.out.println("\ntestAES_android");
		String key = "123456789012345678901234567890~!";
		String source = "世界你好<name>test</name>";
		System.out.println("AES - 密钥：" + key);
		System.out.println("AES - 原文：" + source);
		{
			String encrypted = AESEncryption.encrypt(source.getBytes("UTF-8"),
					key);
			System.out.println("AES - 密文：" + encrypted);
			System.out.println("AES - 密文是否与服务端密文一致："
					+ encrypted.equals(encryptedInServer));
			String decrypted = AESEncryption.decrypt(encrypted, key);
			System.out.println("AES - 与原文是否一致：" + decrypted.equals(source));
			String decryptedServerEncrypted = AESEncryption.decrypt(
					encryptedInServer, key);
			System.out.println("AES - 解密服务端提供的密文与原文是否一致："
					+ decryptedServerEncrypted.equals(source));
		}
	}

	public static void main() throws Exception {
		testRSA("GFqgMtWaSlzUTrbX2/bdrTKWG0scUcWuyxCHT1/i3ivbjhertNHCV8BYnEcsmbGt7NVhpoHKxWlhemKR+L5v4kPA2F38DC4hbDDpFj0e/xBxXUWHimm2c4e2VC62pene4xDv94Wm34YvTKDIde4ARxWOU1IijMS3W9plffURJdgrqunjre7WPbYkFlMbJej+SQKugbw3329YNugDUKDOS2gxTc3uUvu0dk7kgk0aic/Qu4H67ibdjoINEDI0OCzgwXKXh1VqtgdisloF7leoJnR6tkYDifmE66E4V+zlmJ+cwk1lElMWs+gUskhvhBuZbs1QCGHSjBEeov1KiPtxJg==");
		testAES_android("lU1LPo4Uu9c77OZN+QZTfLLUvNqz7c6cAR37tpB5c+A=");
	}

}

package com.iask.yiyuanlegou1.common.security;

/**
 * Created by wuqiang on 15-8-5.
 * 
 * @author wuqiang
 */
public class RSAKeyPair {
	private final byte[] publicKey;
	private final int keySize;
	// 当前秘钥支持加密的最大字节数
	private final int bufferSize;
	// 当要加密的内容超过bufferSize，则采用partSplit进行分块加密
	private final byte[] partSplit;

	/**
	 * @param keySize
	 *            生成秘钥对时，秘钥长度
	 */
	public RSAKeyPair(byte[] publicKey, int keySize, byte[] partSplit) {
		super();
		this.publicKey = publicKey;
		this.keySize = keySize;
		this.bufferSize = (keySize / 8) - 11;
		this.partSplit = partSplit;
	}

	public final byte[] getPublicKey() {
		return publicKey;
	}

	public final int getBufferSize() {
		return bufferSize;
	}

	public final byte[] getPartSplit() {
		return partSplit;
	}

	public final int getKeySize() {
		return keySize;
	}

}

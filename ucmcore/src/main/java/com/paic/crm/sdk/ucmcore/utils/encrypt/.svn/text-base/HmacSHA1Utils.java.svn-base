package com.paic.crm.sdk.ucmcore.utils.encrypt;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class HmacSHA1Utils {
	

	final static String HMAC_SHA1 = "HmacSHA1";
	// �ַ�
    final static String CHARSET_NAME = "UTF-8";
	/**
	 生成签名数据
	 *
	 *
	 * @param key 加密使用的key
	 *
	 * @return 生成Base64编码的字符串
	 * 
	 */
	public static String getSignature(String toSign, String key) {
		try {
			return getSignature(toSign.getBytes(CHARSET_NAME), key.getBytes(CHARSET_NAME));
		}
		catch (UnsupportedEncodingException e) {
		}
		
		return null;
	}

	/**
	 * 生成签名数据
	 *
	 *
	 * @param key 加密使用的key
	 *
	 * @return 生成Base64编码的字符串
	 */
	public static String getSignature(byte[] toSign, byte[] key) {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
			Mac mac = Mac.getInstance(HMAC_SHA1);
			mac.init(signingKey);
			byte[] rawHmac = mac.doFinal(toSign);
			rawHmac = Base64.encode(rawHmac,Base64.DEFAULT);
			return new String(rawHmac, CHARSET_NAME);
		}
		catch (InvalidKeyException e) {
		}
		catch (NoSuchAlgorithmException e) {
		}
		catch (IllegalStateException e) {
		}
		catch (UnsupportedEncodingException e) {
		}

		return null;
	}

	public static void main(String[] args) {
		String nonce = "34697";
		String timestamp = "1437992650322";
		String key = "83519aa6d30ecdc3";
		
		String toSign = timestamp + nonce;
		System.out.println("toSign: " + toSign);
		
		String signature = getSignature(toSign, key);
		System.out.println("signature: " + signature);
	}
}

package com.paic.crm.utils;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class LoginDesUtil {
public static final String ALGORITHM_DES = "DESede/ECB/PKCS5Padding";
	
	public static final String ZZGJSPWDKEY = "1HH798GZDKUO2167W5GM5YNG";
	

	
	/**
	 * 3DES
	 */
	public static String encrypt(String value, String desKey) {
		String result = null;
		try {
			SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), 0, 24, "DESede");
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			result = new String(Base64.encode(cipher.doFinal(value.getBytes("UTF-8")),Base64.DEFAULT), "UTF-8");
		} catch (Exception e) {
		} 
		
		return result;
		
	}
	
	/**
	 * 3DES
	 */
	public static String decrypt(String value, String desKey) {
		String result = null;
		try {
			SecretKeySpec key = new SecretKeySpec(desKey.getBytes(), 0, 24, "DESede");
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = new String(cipher.doFinal(Base64.decode(value,Base64.DEFAULT)), "UTF-8");
		} catch (Exception e) {
		} 
		
		return result;
	}
	
	public static void main(String[] args) {
		String result="+=  +=//";
		result = result.replaceAll("[+]", "%2B");
		result = result.replaceAll("[/]", "%2F");
		result = result.replaceAll("[=]", "%3D");
		result = result.replaceAll("[ ]", "%20");
		System.out.println(result);
		result = result.replaceAll("%2F", "/");
		result = result.replaceAll("%3D", "=");
		result = result.replaceAll("%20", " ");
		result = result.replaceAll("%2B", "+");
		System.out.println(result);
		System.out.println(LoginDesUtil.encryptToURL("!@#+/\\$  %^&*()[]{}����|!��jin123", ZZGJSPWDKEY));
		
		System.out.println(LoginDesUtil.decryptToURL(LoginDesUtil.encryptToURL("!@#+/\\$  %^&*()[]{}����|!��jin123", ZZGJSPWDKEY), ZZGJSPWDKEY));
	}
	
	
	/**
	 * 3Des��key���ܣ��������ȿ��ǣ� ,���ڼ��ܵ�URLʹ��
	 * @param value �������Ϣ
	 * @return  3DES���ܺ���Base64������ַ�
	 */
	public static String encryptToURL(String value, String key){
		String result = encrypt(value, key);
		result = result.replaceAll("[+]", "%2B");
		result = result.replaceAll("[/]", "%2F");
		result = result.replaceAll("[=]", "%3D");
		result = result.replaceAll("[ ]", "%20");
		return result;
	}
	
	/** 
	 * 3Desʹ��Ĭ�ϵ�key����URL ���������ȿ��ǣ� 
	 * @param value 3DES���ܺ���Base64������ַ�
	 * @return ����ԭ��
	 */
	public static String decryptToURL(String value,String key){
		String result = value.replaceAll("%2B", "+");
		result = result.replaceAll("%2F", "/");
		result = result.replaceAll("%3D", "=");
		result = result.replaceAll("%20", " ");
		result = decrypt(result, key);
		return result;
	}
	
	
	/**
	 * 3Desʹ��Ĭ�ϵ�key����
	 * @param value �������Ϣ
	 * @return  3DES���ܺ���Base64������ַ�
	 */
	public static String decryptToZZGJS(String value){
		return decrypt(value, ZZGJSPWDKEY);
	}
	
	public static String sign(String text, String sign) {
		String sha1 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(text.getBytes());
			sha1 = byte2string(md.digest());
		} catch (Exception e) {
		}
		return sha1;
	}
	
	private static String byte2string(byte[] data) {
		StringBuffer sb = new StringBuffer();
		char[] hex = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'A', 'B', 'C', 'D', 'E', 'F' };
		for(int i = 0; i < data.length; i++) {
			char[] t = new char[2];
			t[0] = hex[(data[i] >>> 4) & 0x0F];
			t[1] = hex[data[i] & 0x0F];
			sb.append(t);
		}
		return sb.toString();
	}
}

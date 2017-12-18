package com.paic.crm.utils;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 双向认证HTTPS工具-安卓版(使用HttpsURLConnection)
 * 
 * @author liuzy
 * @since 2016年3月17日
 */
public class HTTPS {
	/**
	 * HTTPS工具的初始化方法，只需要调用一次，如果你信任所有服务器，trustStore传入null即可！
	 * 
	 * @param keyStore
	 * @param keyStorePwd
	 * @param trustStore
	 */
	public static void init(KeyStore keyStore, String keyStorePwd, KeyStore trustStore) {
		try {
			// 服务器信任的客户端证书
//			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//			keyManagerFactory.init(keyStore, keyStorePwd.toCharArray());
//			KeyManager[] km = keyManagerFactory.getKeyManagers();
			
			// 客户端信任的服务器证书
			TrustManager[] tm = null;
			if (trustStore == null) {
				tm = new TrustManager[] { new X509TrustManager() {
			        @Override
			        public X509Certificate[] getAcceptedIssuers() {
			            return new X509Certificate[] {};
			        }
			        @Override
			        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			        }
			        @Override
			        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			        }
			    } };
			} else {
				TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
				trustManagerFactory.init(trustStore);
				tm = trustManagerFactory.getTrustManagers();
			}
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, tm, null);
			// 设置https连接的socket工厂
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

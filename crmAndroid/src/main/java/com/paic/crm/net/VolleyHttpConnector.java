package com.paic.crm.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paic.crm.android.BuildConfig;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.HTTPS;
import com.paic.crm.utils.SpfUtil;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yueshaojun on 2017/8/22.
 */

public class VolleyHttpConnector {
	/**
	 * http通用方法
	 *
	 * @param url
	 * @param tag
	 * @param params
	 * @param volleyInterface
	 */
	public static void volleyPost(final Context context,
	                              String url, String tag,
	                              final Map<String, String> params,
	                              VolleyInterface volleyInterface) {

		//       AppContext.getRequestQueue().cancelAll(tag);
		//       FakeX509TrustManager.allowAllSSL(context);
		HTTPS.init(null, null, getTrustStoreByCrt(context, "server.cer"));
		StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
				volleyInterface.getSuccessListener(),
				volleyInterface.getErrorListener()) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return params;
			}


			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				String sysVer=android.os.Build.VERSION.RELEASE;
				String mso= (String) SpfUtil.getValue(context,SpfUtil.MSO_FLAG,"");
				headers.put("Charset", "UTF-8");
				headers.put("Content-Type", "application/x-javascript");
				headers.put("Accept-Encoding", "gzip,deflate");
				headers.put("deviceStyle","android");
				headers.put("systemVersion",sysVer);
				headers.put("networkOperators",mso);
				headers.put("versionCode", BuildConfig.VERSION_NAME);
				return headers;
			}

			@Override
			public RetryPolicy getRetryPolicy() {
				RetryPolicy retryPolicy = new DefaultRetryPolicy(5000,1,0.1f);
				return retryPolicy;
			}
		};
		Volley.newRequestQueue(context).add(stringRequest);
	}

	public static void volleyGetImage(Context context,String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config config, final VolleyInterface volleyInterface){
		ImageRequest imageRequest = new ImageRequest(url, volleyInterface.getSuccessListener(),

				maxWidth,
				maxHeight,
				scaleType,
				config,
				volleyInterface.getErrorListener());
		Volley.newRequestQueue(context).add(imageRequest);
	}
	/**
	 * @param ctx
	 * @param crtFile server.crt
	 * @return
	 */
	public static KeyStore getTrustStoreByCrt(Context ctx, String crtFile) {
		InputStream ctxIn = null;
		try {
			// 服务器证书
			ctxIn = ctx.getResources().getAssets().open(crtFile);
			CRMLog.LogInfo(Constants.LOG_TAG,"ctxIn----------->"+ctxIn);
			CertificateFactory cf = CertificateFactory.getInstance("x.509");
			Certificate cer = cf.generateCertificate(ctxIn);

			// 初始化
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			// 把服务器证书放入keystore中
			keyStore.setCertificateEntry("server", cer);

			// 打印KeyStore信息
			CRMLog.LogInfo(Constants.LOG_TAG,keyStore+"");

			return keyStore;
		} catch (Exception e) {
			CRMLog.LogInfo(Constants.LOG_TAG, "创建客户端信任的服务器证书仓库失败");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (ctxIn != null) {
					ctxIn.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}

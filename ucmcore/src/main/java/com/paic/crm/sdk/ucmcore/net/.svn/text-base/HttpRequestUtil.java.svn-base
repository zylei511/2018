package com.paic.crm.sdk.ucmcore.net;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.paic.crm.sdk.ucmcore.utils.CRMLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by yueshaojun on 16/10/28.
 * http 工具封装类，回调方法运行在主线程中
 * 同步的get和post请求需要在子线程中运行
 */
public class HttpRequestUtil {
	private static final String TAG = "HttpRequestUtil";
	private Handler requestHandler;
	private static HttpRequestUtil instance;
	private OkHttpClient mOkHttpClient;
	private final int timeout = 5 * 1000;
	private final String CONTENT_TYPE_JSON = "application/json";
	private final String METHOD_GET = "GET";
	private final String METHOD_POST = "POST";
	private Gson mGson = new Gson();

	private HashMap<String,String> mHeaders = new HashMap<>();
	private HttpRequestUtil(){
		mOkHttpClient = new OkHttpClient.Builder().build();
		requestHandler = new Handler(Looper.getMainLooper());
	}
	private HttpRequestUtil(Builder builder) {
//		mOkHttpClient = builder.clientBuilder == null ? new OkHttpClient.Builder().build() : builder.clientBuilder;
		OkHttpClient.Builder _tempBuidler = builder.clientBuilder.
				retryOnConnectionFailure(true).
				readTimeout(builder.readTimeout,TimeUnit.SECONDS).
				connectTimeout(builder.connectionTimeout,TimeUnit.SECONDS).
				writeTimeout(builder.writeTimeout, TimeUnit.SECONDS);
		if(builder.interceptor != null){
			_tempBuidler.addInterceptor(builder.interceptor);
		}
		if(builder.netInterceptor != null){
			_tempBuidler.addNetworkInterceptor(builder.netInterceptor);
		}
		mOkHttpClient = _tempBuidler.build();
		this.mHeaders = builder.headers;
		requestHandler = new Handler(Looper.getMainLooper());
	}

	public static final class Builder{
		private OkHttpClient.Builder clientBuilder;
		private HashMap<String,String> headers = new HashMap<>();
		private Interceptor interceptor;
		private Interceptor netInterceptor;
		private long readTimeout;
		private long writeTimeout;
		private long connectionTimeout;
		public Builder setHttpsCertificates(InputStream... certificates){
			this.clientBuilder = getSSLOkHttpClientBuilder(certificates);
			return this;
		}
		public Builder addHeaders(HashMap<String,String> headers){
			this.headers = headers;
			return this;
		}
		public Builder addInterceptor(Interceptor interceptor){
			this.interceptor = interceptor;
			return this;
		}
		public Builder addNetInterceptor(Interceptor interceptor){
			netInterceptor = interceptor;
			return this;
		}
		public Builder readTimeout(long timeout ){
			readTimeout = timeout;
			return this;
		}
		public Builder writeTimeout(long timeout ){
			writeTimeout = timeout;
			return this;
		}
		public Builder connectionTimeout(long timeout ){
			connectionTimeout = timeout;
			return this;
		}
		public HttpRequestUtil build(){
			return new HttpRequestUtil(this);
		}
	}

	private static OkHttpClient.Builder getSSLOkHttpClientBuilder(InputStream... certificates) {
		OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			for (InputStream certificate : certificates) {
				String certificateAlias = Integer.toString(index++);
				keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

				try {
					if (certificate != null) certificate.close();
				} catch (IOException e) {
				}
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
					.getDefaultAlgorithm());

			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			okHttpClientBuilder = new OkHttpClient.Builder().sslSocketFactory(sslContext.getSocketFactory());


		} catch (Exception e) {
			e.printStackTrace();
		}
		return okHttpClientBuilder;

	}

	/**
	 * post 表单
	 *
	 * @param actionUrl 接口地址
	 * @param paramsMap 参数
	 * @param callback  回调
	 */
	public <T> void postFile(String actionUrl, Class<T> tClass, HashMap<String, Object> paramsMap, RequestCallback
			callback) {
		//补全请求地址
		MultipartBody.Builder builder = new MultipartBody.Builder();
		//设置类型
		builder.setType(MultipartBody.FORM);
		//追加参数
		for (String key : paramsMap.keySet()) {
			Object object = paramsMap.get(key);
			if (!(object instanceof File)) {
				builder.addFormDataPart(key, object.toString());
			} else {
				File file = (File) object;
				builder.addFormDataPart(key, file.getName(), RequestBody.create(null, file));
			}
		}
		//创建RequestBody
		RequestBody body = builder.build();

		//创建Request

		final Request request = buildRequest(actionUrl, METHOD_POST, body);
		deliverAsynRequest(request, tClass, callback);
	}

	/**
	 * 异步get请求
	 *
	 * @param url
	 * @param params
	 * @param callback
	 */
	public <T> void httpAsynGet(String url, Class<T> tClass, Map<String, Object> params, final RequestCallback
			callback) {
		url = url + parseMap(params);
		CRMLog.LogInfo(TAG, url);
		Request request = buildRequest(url, METHOD_GET, null);
		deliverAsynRequest(request, tClass, callback);
	}

	/**
	 * 同步get请求，请在子线程中运行
	 *
	 * @param url
	 * @param params
	 * @return 请求结果string
	 */

	public String httpSynGet(String url, Map<String, Object> params) throws IOException {
		url = url + parseMap(params);
		Request request = buildRequest(url, METHOD_GET, null);
		return deliverSynRequest(request);
	}

	/**
	 * 异步post请求，post的是json
	 *
	 * @param url
	 * @param params
	 * @param callback
	 */

	public <T> void httpAsynPostJson(String url, Class<T> tClass, Map<String, Object> params, RequestCallback
			callback) {
		String jsonStr = "";
		if (params != null) {
			JSONObject jsonObject = new JSONObject(params);
			jsonStr = jsonObject.toString();
		}
		RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON + ";charset=utf-8"), jsonStr);
		Request request = buildRequest(url, METHOD_POST, requestBody);
		deliverAsynRequest(request, tClass, callback);
	}

	/**
	 * @param url
	 * @param params
	 * @param callback
	 */
	public <T> void httpPost(String url, Class<T> tClass, Map params, RequestCallback callback) {
		FormBody.Builder builder = new FormBody.Builder();
		Iterator it = params.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object value = params.get(key);
			builder.add((String) key, (String) value);
		}
		RequestBody requestBody = builder.build();
		Request request = buildRequest(url, METHOD_POST, requestBody);
		deliverAsynRequest(request, tClass, callback);
	}

	/**
	 * 同步post请求 请在子线程中运行
	 *
	 * @param url
	 * @param params
	 * @return string
	 */
	public String httpSynPostJson(String url, Map<String, Object> params) throws IOException {
		String jsonStr = "";
		if (params != null) {
			JSONObject jsonObject = new JSONObject(params);
			jsonStr = jsonObject.toString();
		}
		RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE_JSON + ";charset=utf-8"), jsonStr);
		Request request = buildRequest(url, METHOD_POST, requestBody);
		return deliverSynRequest(request);
	}

	private Request buildRequest(String url, String method, RequestBody requestBody) {

		Headers headers = Headers.of(mHeaders);
		CRMLog.LogInfo(TAG, headers.toString());
		Request.Builder builder = new Request.Builder().url(url).headers(headers);

		Request request = builder.method(method, requestBody).build();
		return request;
	}

	private synchronized <T> void deliverAsynRequest(final Request request, final Class<T> tClass, final
	RequestCallback callback) {

		if (callback == null) {
			throw new IllegalStateException("callback must not be null!");
		}
		final CrmErrorMessage errorMessage = new CrmErrorMessage();
		Call call = mOkHttpClient.newCall(request);
		call.enqueue(new Callback() {
			@Override
			public void onFailure(final Call call, final IOException e) {
				CRMLog.LogError(TAG, "response error:" + e.getMessage());
				errorMessage.errorCode = CrmErrorMessage.ERROR_CODE_NETWORK;
				errorMessage.errorMsg = e.getMessage();
				callback.onError(errorMessage);
			}

			@Override
			public void onResponse(Call call, final Response response) throws IOException {
				String responseStr = response.body().string();
				String resultCode, resultMsg;
				boolean isObject = false;
				T data = null;
				List<T> listData = null;
				CRMLog.LogDebug(TAG, "response:" + responseStr);
				try {
					JSONObject jsonObject = new JSONObject(responseStr);
					resultCode = jsonObject.optString("resultCode");
					resultMsg = jsonObject.optString("resultMsg");
					String dataStr = jsonObject.optString("data");
					if (!TextUtils.isEmpty(dataStr)) {
						JSONTokener jsonParser = new JSONTokener(dataStr);
						Object object = jsonParser.nextValue();
						if (object instanceof JSONObject) {
							isObject = true;
							data = mGson.fromJson(object.toString(), tClass);
						} else if (object instanceof JSONArray) {
							isObject = false;
							listData = mGson.fromJson(object.toString(), type(ArrayList.class, tClass));
						}
					}
					if ("200".equals(resultCode) || "02".equals(resultCode) || "0".equals(resultCode)) {
						callback.onSuccess(isObject ? data : listData);
					} else {
						errorMessage.errorCode = resultCode;
						errorMessage.errorMsg = resultMsg;
						callback.onError(errorMessage);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					errorMessage.errorCode = CrmErrorMessage.ERROR_CODE_DATA_FORMAT_DEFEATE;
					errorMessage.errorMsg = e.getMessage();
					callback.onError(errorMessage);
				}
			}
		});
	}


	private synchronized String deliverSynRequest(Request request) throws IOException {
		Call call = mOkHttpClient.newCall(request);
		Response response = call.execute();
		return response.body().string();
	}

	private String parseMap(Map<String, Object> params) {
		if (params == null) {
			return "";
		}
		Iterator it = params.keySet().iterator();
		String result = "";
		while (it.hasNext()) {
			Object key = it.next();
			Object value = params.get(key);
			result += (key + "=" + value);
			if (it.hasNext()) {
				result += "&";
			}
		}
		return result;
	}

	public interface RequestCallback<T> {
		void onError(CrmErrorMessage error);

		void onSuccess(T response);
	}

	public interface RequestListCallback<T> {
		void onError(String error);

		void onSuccess(List<T> response);
	}

	private static ParameterizedType type(final Class raw, final Type... args) {
		return new ParameterizedType() {
			public Type getRawType() {
				return raw;
			}

			public Type[] getActualTypeArguments() {
				return args;
			}

			public Type getOwnerType() {
				return raw;
			}
		};
	}

	public OkHttpClient getOkHttpClient() {
		return mOkHttpClient;
	}
}

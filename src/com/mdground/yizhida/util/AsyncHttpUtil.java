package com.mdground.yizhida.util;

import java.io.File;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.BuildConfig;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;

/**
 * @ClassName: AsyncHttpUtil
 * @Description: Http请求类，包括POST和GET请求及提交数据
 */
public class AsyncHttpUtil {
	private static String TAG = AsyncHttpUtil.class.getSimpleName();

	private static final int TIME_OUT = 10000;

	private static AsyncHttpClient client = new AsyncHttpClient();

	static {
		// 设置链接超时，如果不设置，默认为10s
		client.setTimeout(TIME_OUT);
		// 设置重连次数及两次重连的时间间隔
		// client.setMaxRetriesAndTimeout(3, 4000);
		// 设置header
		// client.addHeader("", "");
		client.getHttpContext();
	}

	private AsyncHttpUtil() {
	}

	/**
	 * @Description: 同步session到webview
	 * @param context
	 * @param remoteAddress
	 * @return: void
	 */
	public static void syncCookie(Context context, String remoteAddress) {
		HttpClient httpClient = client.getHttpClient();

		CookieStore cookieStore = ((AbstractHttpClient) httpClient).getCookieStore();
		Cookie cookie = null;
		List<Cookie> cookies = cookieStore.getCookies();
		if (!cookies.isEmpty()) {
			for (int i = 0; i < cookies.size(); i++) {
				cookie = cookies.get(i);
			}
		}
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();

		if (cookie != null) {
			cookieManager.removeSessionCookie();
			try {
				// webview第一次加载需要10毫秒的延时
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			String cookieString = cookie.getName() + "=" + cookie.getValue() + "; domain=" + cookie.getDomain();
			cookieManager.setCookie(remoteAddress, cookieString);
			CookieSyncManager.getInstance().sync();
		}
	}

	/******************************************************************************************
	 * ===================================以下为POST方式请求==========================
	 * =============
	 ******************************************************************************************/

	/**
	 * @Description: POST请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            TextHttpResponseHandler回调（返回String数据）
	 * @return: void
	 */
	public static void post(String url, RequestParams params, TextHttpResponseHandler handler) {
		httpPost(url, params, handler);
	}

	/**
	 * @Description: POST请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            JsonHttpResponseHandler回调（返回json数据）
	 * @return: void
	 */
	public static void post(String url, RequestParams params, JsonHttpResponseHandler handler) {
		httpPost(url, params, handler);
	}


	/**
	 * @Description: POST请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            AsyncHttpResponseHandler回调（返回String数据）
	 * @return: void
	 */
	public static void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		httpPost(url, params, handler);
	}

	/**
	 * @Description: POST请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            FileAsyncHttpResponseHandler回调（返回File数据）
	 * @return: void
	 */
	public static void post(String url, RequestParams params, FileAsyncHttpResponseHandler handler) {
		httpPost(url, params, handler);
	}

	/**
	 * @Description: POST请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            BinaryHttpResponseHandler回调（返回byte[]数据）
	 * @return: void
	 */
	public static void post(String url, RequestParams params, BinaryHttpResponseHandler handler) {
		httpPost(url, params, handler);
	}

	public static void httpPost(String url, RequestParams params, ResponseHandlerInterface handler) {
		Log.i(TAG, "request post url=" + url);
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "request post url=" + url);
		}
		if (params == null) {
			client.post(url, handler);
		} else {
			client.post(url, params, handler);
		}
	}

	/**
	 * @Description: GET请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param asyncHttpResponseHandler
	 *            TextHttpResponseHandler回调（返回String数据）
	 * @return: void
	 */
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler) {
		httpGet(url, params, asyncHttpResponseHandler);
	}

	/**
	 * @Description: GET请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            TextHttpResponseHandler回调（返回String数据）
	 * @return: void
	 */
	public static void get(String url, RequestParams params, TextHttpResponseHandler handler) {
		httpGet(url, params, handler);
	}

	/**
	 * @Description: GET请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            JsonHttpResponseHandler回调（返回json数据）
	 * @return: void
	 */
	public static void get(String url, RequestParams params, JsonHttpResponseHandler handler) {
		httpGet(url, params, handler);
	}

	/**
	 * @Description: GET请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            FileAsyncHttpResponseHandler回调（返回File数据）
	 * @return: void
	 */
	public static void get(String url, RequestParams params, FileAsyncHttpResponseHandler handler) {
		httpGet(url, params, handler);
	}

	/**
	 * @Description: GET请求
	 * @param url
	 *            url地址
	 * @param params
	 *            参数
	 * @param handler
	 *            BinaryHttpResponseHandler回调（返回byte[]数据）
	 * @return: void
	 */
	public static void get(String url, RequestParams params, BinaryHttpResponseHandler handler) {
		httpGet(url, params, handler);
	}

	public static void httpGet(String url, RequestParams params, ResponseHandlerInterface handler) {
		if (BuildConfig.DEBUG) {
			Log.i(TAG, "request get url=" + url);
		}

		if (params == null) {
			client.get(url, handler);
		} else {
			Log.i(TAG, "request get url=" + url + "?" + params.toString());
			client.get(url, params, handler);
		}
	}

}

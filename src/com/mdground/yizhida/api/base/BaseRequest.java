package com.mdground.yizhida.api.base;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mdground.yizhida.BuildConfig;
import com.mdground.yizhida.activity.appointment.PatientAppointmentPresenterImpl;
import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.server.global.LoginEmployee;
import com.mdground.yizhida.api.utils.DeviceUtils;
import com.mdground.yizhida.api.utils.Encrypt;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.MD5Util;
import com.mdground.yizhida.util.MdgConfig;
import com.mdground.yizhida.util.PreferenceUtils;
import com.mdground.yizhida.util.ToolNetwork;

public abstract class BaseRequest {
	protected static final String TAG = "API";

	private final static int TIME_OUT = 10 * 1000;

	private AsyncHttpClient httpClient;
	private HttpClient httpsClient;

	protected Context mContext;
	// 请求参数
	private RequestData data;
	private RequestCallBack mCallBack;

	protected abstract String getFunctionName();

	protected abstract String getUrl();

	protected abstract int getBusinessCode();

	private BaseTextHttpResponseHandler mTextHttpResponseHandler;

	public BaseRequest(Context context) {
		this.data = new RequestData();
		this.mContext = context;
		this.mTextHttpResponseHandler = new BaseTextHttpResponseHandler();
	}

	// 加密调用
	protected void pocess() {
		pocess(true);
	}

	protected void pocess(boolean hasEncrypt) {
		if (!ToolNetwork.isNetworkConnected(mContext)) {
			Toast.makeText(mContext, "当前网络不可用，请先检查网络设置", Toast.LENGTH_SHORT).show();
			return;
		}
		mTextHttpResponseHandler.setEncrypt(hasEncrypt);
		StringEntity entity = createRequestEntity(hasEncrypt);
		if (entity == null) {
			Log.e(TAG, "new StringEntity failed !");
			return;
		}
		if (BuildConfig.DEBUG) {
			Log.i(TAG, entity.toString());
		}

		AsyncHttpClient client = getHttpClient();
		client.post(mContext, getUrl(), entity, "application/x-www-form-urlencoded", mTextHttpResponseHandler);
	}

	// 同步请求
	protected ResponseData pocessSync(boolean hasEncrypt) {
		if (!ToolNetwork.isNetworkConnected(mContext)) {
			Toast.makeText(mContext, "请连接网络", Toast.LENGTH_SHORT).show();
			return null;
		}

		ResponseData responseData = null;

		StringEntity entity = createRequestEntity(hasEncrypt);
		if (entity == null) {
			Log.e(TAG, "new StringEntity failed !");
			return null;
		}
		if (BuildConfig.DEBUG) {
			Log.i(TAG, entity.toString());
		}

		// AsyncHttpClient client = getHttpClient();
		HttpClient httpClient = getHttpsClient();
		HttpPost httpost = new HttpPost(getUrl());
		httpost.setEntity(entity);
		httpost.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		httpost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63");

		try {
			HttpResponse response = httpClient.execute(httpost);
			HttpEntity responseEntity = response.getEntity();
			String charset = EntityUtils.getContentCharSet(responseEntity);
			Log.i(TAG, "charset：" + charset);
			Log.i(TAG, "response status: " + response.getStatusLine());
			String body = EntityUtils.toString(responseEntity);
			if (hasEncrypt) {
				body = URLDecoder.decode(body, "UTF-8");
				body = Encrypt.decrypt(body);
				Log.i(TAG, body);
			}

			Gson gson = new GsonBuilder().create();
			responseData = gson.fromJson(body, ResponseData.class);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseData;
	}

	private StringEntity createRequestEntity(boolean hasEncrypt) {
		if (data == null) {
			data = getData();
		}
		data.setCulture(DeviceUtils.getLanguage(mContext));
		data.setBusinessCode(getBusinessCode());
		data.setFunctionName(getFunctionName());
		data.setActionTimeSpan(System.currentTimeMillis() / 1000);
		data.setPlatform(getPlatform());

		String serviceToken = "";
		if (mContext.getApplicationContext() instanceof MdgAppliction) {
			MdgAppliction appliction = (MdgAppliction) mContext.getApplicationContext();
			data.setDeviceID(appliction.getDeviceId());

			Employee employee = appliction.getLoginEmployee();
			if (employee != null) {
				serviceToken = employee.getServiceToken();
				data.setEmployeeID(employee.getEmployeeID());
				data.setClinicID(employee.getClinicID());
			}
		}
		data.setServiceToken(serviceToken);
		data.setSign(appSign(data));

		// 加密RequestData
		String postString = new Gson().toJson(data);
		
		L.e(this, "请求接口 : " + getFunctionName() + " 的postString : " + postString);
		if (hasEncrypt) {
			try {
				postString = Encrypt.encrypt(postString);
				postString = URLEncoder.encode(postString, HTTP.UTF_8);
				
//				L.e(this, "加密后的postString : " + postString);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i(TAG, "encrypt exception", e);
			}
		}

		StringEntity entity = null;
		try {
			entity = new StringEntity(postString, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return entity;
	}

	private int getPlatform() {
		boolean isPad = DeviceUtils.isPad(mContext);
		if (isPad) {
			return PlatformType.ANDROID_PAD.value();
		} else {
			return PlatformType.ANDROID_PHONE.value();
		}
	}

	protected String appSign(RequestData data) {
		Map<String, String> params = new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String lhs, String rhs) {
				return lhs.compareToIgnoreCase(rhs);
			};
		});

		params.put("Version", String.valueOf(data.getVersion()));
		params.put("Culture", data.getCulture());
		params.put("Platform", String.valueOf(data.getPlatform()));
		params.put("BusinessCode", String.valueOf(data.getBusinessCode()));
		params.put("FunctionName", data.getFunctionName());
		params.put("DeviceID", String.valueOf(data.getDeviceID()));
		params.put("ClinicID", String.valueOf(data.getClinicID()));
		params.put("EmployeeID", String.valueOf(data.getEmployeeID()));
		params.put("ServiceToken", data.getServiceToken());
		params.put("ActionTimeSpan", String.valueOf(data.getActionTimeSpan()));

		Set<String> keySet = params.keySet();
		Iterator<String> iter = keySet.iterator();

		StringBuffer sb = new StringBuffer();
		while (iter.hasNext()) {
			String key = iter.next();
			sb.append(key);
			sb.append(params.get(key));
		}
		sb.append("@2O!5");

		if (BuildConfig.DEBUG) {
			Log.i(TAG, sb.toString());
		}

		return MD5Util.MD5(sb.toString());
	}

	public RequestData getData() {
		return data;
	}

	public void setData(RequestData data) {
		this.data = data;
	}

	protected AsyncHttpClient getHttpClient() {
		if (httpClient == null) {
			httpClient = new AsyncHttpClient();
			httpClient.setTimeout(TIME_OUT);
		}
		return httpClient;
	}

	protected HttpClient getHttpsClient() {
		if (httpsClient == null) {
			try {
				KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
				trustStore.load(null, null);
				SSLSocketFactory sf = new BaseSSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); // 允许所有主机的验证

				HttpParams params = new BasicHttpParams();

				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
				HttpProtocolParams.setUseExpectContinue(params, true);

				// 设置连接管理器的超时
				ConnManagerParams.setTimeout(params, 10000);
				// 设置连接超时
				HttpConnectionParams.setConnectionTimeout(params, 10000);
				// 设置socket超时
				HttpConnectionParams.setSoTimeout(params, 10000);

				// 设置http https支持
				SchemeRegistry schReg = new SchemeRegistry();
				schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
				schReg.register(new Scheme("https", sf, 443));

				ClientConnectionManager conManager = new ThreadSafeClientConnManager(params, schReg);

				httpsClient = new DefaultHttpClient(conManager, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return httpsClient;
	}

	public RequestCallBack getRequestCallBack() {
		return mCallBack;
	}

	public void setRequestCallBack(RequestCallBack mCallBack) {
		this.mCallBack = mCallBack;
	}

	private class BaseTextHttpResponseHandler extends TextHttpResponseHandler {

		private boolean encrypt;

		public void onStart() {
			RequestCallBack callback = getRequestCallBack();
			if (callback == null) {
				return;
			}
			callback.onStart();
		};

		@Override
		public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			RequestCallBack callback = getRequestCallBack();
			if (callback == null) {
				return;
			}
			callback.onFailure(statusCode, headers, responseString, throwable);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, String responseString) {
			RequestCallBack callback = getRequestCallBack();
			
			if (callback == null) {
				return;
			}

			try {
				if (encrypt) {
					responseString = URLDecoder.decode(responseString, "UTF-8");
					responseString = Encrypt.decrypt(responseString);
				}

				Gson gson = new GsonBuilder().create();
				ResponseData mResponseData = gson.fromJson(responseString, ResponseData.class);
				
				L.e(this, "请求 " + getFunctionName() + " 返回的code : " + mResponseData.getCode());
				L.e(this, "请求 " + getFunctionName() + " 返回的message : " + mResponseData.getMessage());
				L.e(this, "请求 " + getFunctionName() + " 返回的content : " + mResponseData.getContent());
				
				// 处理token失效问题
				if (mResponseData.getCode() == ResponseCode.InvalidToken.getValue()) {
					login(PreferenceUtils.getPrefString(mContext, MemberConstant.USERNAME, ""),
							PreferenceUtils.getPrefString(mContext, MemberConstant.PASSWORD, ""));
					return;
				}
				callback.onSuccess(mResponseData);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFinish() {
			RequestCallBack callback = getRequestCallBack();
			if (callback == null) {
				return;
			}
			callback.onFinish();
		}

		public void setEncrypt(boolean encrypt) {
			this.encrypt = encrypt;
		};
	};

	protected static class QueryDataBuilder {
		JsonObject jsonObj;

		public QueryDataBuilder() {
			jsonObj = new JsonObject();
		}

		public QueryDataBuilder putData(String property, String value) {
			jsonObj.addProperty(property, value);
			return this;
		}

		public String create() {
			return jsonObj.toString();
		}
	}

	private void login(String loginId, String pwd) {
		LoginEmployee loginEmployee = new LoginEmployee(mContext);
		loginEmployee.loginEmployee(loginId, pwd, DeviceUtils.getDeviceInfo(mContext), new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {
				if (response.getCode() == ResponseCode.Normal.getValue()) {
					Employee employee = response.getContent(Employee.class);
					if ((employee.getEmployeeRole() & Employee.SCREEN) != 0
							|| ((employee.getEmployeeRole() & Employee.DOCTOR) != 0 && (employee.getEmployeeRole() & Employee.NURSE) != 0)) {
						return;
					}
					// 保存数据库
					PreferenceUtils.setPrefLong(mContext, MemberConstant.LOGIN_EMPLOYEE, employee.getEmployeeID());
					PreferenceUtils.setPrefInt(mContext, MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_IN);
					PreferenceUtils.setPrefString(mContext, MemberConstant.USERNAME, employee.getLoginID());
					PreferenceUtils.setPrefString(mContext, MemberConstant.PASSWORD, employee.getLoginPwd());
					// PreferenceUtils.setPrefInt(BaseActivity.this,
					// MemberConstant.DEVICE_ID, employee.getDeviceID());
					MdgConfig.setDeviceId(employee.getDeviceID());
					if (mContext.getApplicationContext() instanceof MdgAppliction) {
						MdgAppliction appliction = (MdgAppliction) mContext.getApplicationContext();
						appliction.setLoginEmployee(employee);
					}
					pocess();
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
			}
		});
	}

}

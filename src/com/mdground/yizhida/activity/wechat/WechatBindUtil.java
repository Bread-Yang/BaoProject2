package com.mdground.yizhida.activity.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.R;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseCode;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.global.SaveEmployee;
import com.mdground.yizhida.api.server.global.UpdateEmployeeWeChat;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.api.utils.PxUtil;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WechatBindUtil {

	public interface WechatBindSuccessCallBack {
		public void bindSuccess(String wechat_nickName);

		public void unBindSuccess();

		public void bindFail();
	}

	WechatBindSuccessCallBack callBack;

	Context mContext;

	// 获取第一步的code后，请求以下链接获取access_token
	public static final String ACCESS_TOKEN_REQUEST_TEMPLATE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	public static final String GET_USER_INFO_TEMPLATE_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

	private String accessTokenRequestURL;

	private String openid = "";
	private String unionid = "";

	public Runnable DownloadRun = new Runnable() {

		@Override
		public void run() {
			WXGetAccessToken();
		}
	};

	public WechatBindUtil(Context context, WechatBindSuccessCallBack callBack) {
		this.mContext = context;
		this.callBack = callBack;
	}

	public void bindWechat() {
		if (!MedicalAppliction.api.isWXAppInstalled()) {
			Toast.makeText(mContext, R.string.please_install_wechat, Toast.LENGTH_LONG).show();
		} else {
			WXEntryActivity.wechat_code = null;

			L.e(this, "绑定之前,wechat_code : " + WXEntryActivity.wechat_code);

			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "carjob_wx_login";
			MedicalAppliction.api.sendReq(req);
		}
	}

	public void unBindWechat() {
		new UpdateEmployeeWeChat(mContext).updateEmployeeWeChat("", "", "", new RequestCallBack() {

			@Override
			public void onSuccess(ResponseData response) {

				if (response.getCode() == ResponseCode.Normal.getValue()) {
					if (callBack != null) {
						callBack.unBindSuccess();
					}

					Employee employee = ((MedicalAppliction) mContext.getApplicationContext()).getLoginEmployee();
					employee.setOpenID(null);
					employee.setUnionID(null);
					employee.setWeChatName(null);
				} else if (response.getCode() == ResponseCode.AppCustom0.getValue()) {
					Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(mContext, R.string.unbind_fail, Toast.LENGTH_LONG).show();
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

	public void getWechatOpenIdAndUnionIdAndNickname() {
		accessTokenRequestURL = getAccessTokenRequestURL(WXEntryActivity.wechat_code);

		Thread thread = new Thread(DownloadRun);
		thread.start();
	}

	/**
	 * 获取access_token的URL（微信）
	 * 
	 * @param code
	 *            授权时，微信回调给的
	 * @return URL
	 */
	public static String getAccessTokenRequestURL(String code) {
		String url = ACCESS_TOKEN_REQUEST_TEMPLATE_URL;
		url = url.replace("APPID", urlEnodeUTF8(MedicalConstant.WECHAT_MOBILE_APP_ID));
		url = url.replace("SECRET", urlEnodeUTF8(MedicalConstant.WECHAT_MOBILE_APP_SECRET));
		L.e(WechatBindUtil.class, "urlEnodeUTF8(code)前的code : " + code);
		url = url.replace("CODE", urlEnodeUTF8(code));
		return url;
	}

	/**
	 * 获取用户个人信息的URL（微信）
	 * 
	 * @param access_token
	 *            获取access_token时给的
	 * @param openid
	 *            获取access_token时给的
	 * @return URL
	 */
	private String getUserInfo(String access_token, String openid) {
		String url = GET_USER_INFO_TEMPLATE_URL;
		url = url.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
		url = url.replace("OPENID", urlEnodeUTF8(openid));
		return url;
	}

	public static String urlEnodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 获取access_token等等的信息(微信)
	 * 
	 * 正确返回 :
	 * 
	 * { "access_token":"ACCESS_TOKEN", "expires_in":7200,
	 * "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE",
	 * "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL" }
	 */
	private void WXGetAccessToken() {
		L.e(WechatBindUtil.class, "WXGetAccessToken()");
		HttpClient get_access_token_httpClient = new DefaultHttpClient();
		String access_token = "";
		try {
			L.e(this, "accessTokenRequestURL : " + accessTokenRequestURL);
			HttpPost postMethod = new HttpPost(accessTokenRequestURL);
			HttpResponse response = get_access_token_httpClient.execute(postMethod); // 执行POST方法
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String str = "";
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				is.close();
				String jsonString = sb.toString();

				L.e(this, "请求access_token返回的json是 : " + jsonString);

				JSONObject json = new JSONObject(jsonString);
				access_token = (String) json.get("access_token");
				openid = (String) json.get("openid");
				unionid = json.getString("unionid");

				String get_user_info_url = getUserInfo(access_token, openid);
				WXGetUserInfo(get_user_info_url);
			} else {

			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();

			((Activity) mContext).runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(mContext, "请求微信access_token出错", Toast.LENGTH_LONG).show();
				}
			});

			openid = null;
			unionid = null;
		}
	}

	/**
	 * 获取微信用户个人信息
	 * 
	 * @param get_user_info_url
	 *            调用URL
	 */
	private void WXGetUserInfo(String get_user_info_url) {
		HttpClient get_access_token_httpClient = new DefaultHttpClient();
		try {
			HttpGet getMethod = new HttpGet(get_user_info_url);
			HttpResponse response = get_access_token_httpClient.execute(getMethod); // 执行GET方法
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String str = "";
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				is.close();

				String jsonString = sb.toString();
				L.e(this, "请求user_info返回的json是 : " + jsonString);

				JSONObject json = new JSONObject(jsonString);

				final String nickname = (String) json.get("nickname");
				openid = (String) json.get("openid");
				unionid = json.getString("unionid");
				// final String headimgurl = (String) json.get("headimgurl");

				((Activity) mContext).runOnUiThread(new Runnable() {
					public void run() {
						new UpdateEmployeeWeChat(mContext).updateEmployeeWeChat(openid, unionid, nickname,
								new RequestCallBack() {

							@Override
							public void onSuccess(ResponseData response) {

								if (response.getCode() == ResponseCode.Normal.getValue()) {
									Employee employee = ((MedicalAppliction) mContext.getApplicationContext())
											.getLoginEmployee();
									employee.setOpenID(openid);
									employee.setUnionID(unionid);
									employee.setWeChatName(nickname);

									if (callBack != null) {
										callBack.bindSuccess(employee.getWeChatName());
									}
								} else if (response.getCode() == ResponseCode.AppCustom0.getValue()) {
									Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(mContext, R.string.bind_fail, Toast.LENGTH_LONG).show();
								}
								WXEntryActivity.wechat_code = null;
							}

							@Override
							public void onStart() {

							}

							@Override
							public void onFinish() {

							}

							@Override
							public void onFailure(int statusCode, Header[] headers, String responseString,
									Throwable throwable) {
								if (callBack != null) {
									callBack.bindFail();
								}
								WXEntryActivity.wechat_code = null;
							}
						});
					}
				});
			} else {
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

package com.mdground.yizhida;

import android.graphics.Typeface;

/**
 * 网络请求的常量类
 * 
 * @author Administrator
 * 
 */
public class MedicalConstant {
	/**
	 * 全局服务器地址
	 */
	public static final String GLOBAL_HOST = "https://g.yideguan.com";
	// public static final String GLOBAL_HOST = "https://guat.yideguan.com";
	public static final String API_ACTION = "/Api/ClinicService.aspx";
	public static final String GLOBAL_SERVER = GLOBAL_HOST + API_ACTION;

	// public static final String IMAGE_SERVER = "fuat.yideguan.com";
	public static final String IMAGE_SERVER = "file.yideguan.com";
	public static final String WEB_SERVICE_NAMESPACE = "http://tempuri.org/";

	// 短信接口参数
	public static final String APP_KEY = "8332dd660d6b";
	public static final String APP_SECRECT = "7c1476ec13475006dcf4756abd406eab";
	
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
	public static final String WECHAT_APP_ID = "wx89ed82042044ca4a";
    public static final String WECHAT_APP_SECRET = "14234cb71fc29c657be5cb8db93a6fb4";

	// 医直达在sdcar的目录
	public static final String APP_PATH = "/yideguan";
	// 保存数据目录
	public static final String APP_DATA = "/data";
	// 保存二维码目录
	public static final String APP_QRCODE = "/qrcode";
	//保存crash打印
	public static final String APP_CRASH = "/crash";

	// 信鸽推送
	public static final long XG_V2_ACCESS_ID = 2100142473;
	public static final String XG_V2_ACCESS_KEY = "AP6YZ6M68H1M";
	
	// 字体
	public static Typeface NotoSans_Regular;

}

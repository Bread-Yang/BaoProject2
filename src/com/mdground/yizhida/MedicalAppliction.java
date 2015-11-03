package com.mdground.yizhida;

import java.io.File;
import java.util.List;

import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.bean.Employee;
import com.mdground.yizhida.db.Basedao;
import com.mdground.yizhida.util.MdgConfig;
import com.mdground.yizhida.util.MedicalImageDownload;
import com.mdground.yizhida.util.TencentXgPush;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.Typeface;

public class MedicalAppliction extends MdgAppliction {

	private Employee employee;// 登陆用户
	
	public static IWXAPI api;
	
	public MedicalAppliction() {
	}

	public Employee getLoginEmployee() {
		return employee;
	}

	public void setLoginEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initImageLoader();
		copyCitDatabase();
		// 初始化信鸽推送
		initTencentXgPush();

		// 初始化异常crash
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext());

		// CalligraphyConfig.initDefault("fonts/hwxh.ttf", R.attr.fontPath);
		// CalligraphyConfig.initDefault("fonts/boby.ttf", R.attr.fontPath);

		// 注册微信
		api = WXAPIFactory.createWXAPI(this, MedicalConstant.WECHAT_MOBILE_APP_ID, false);
		api.registerApp(MedicalConstant.WECHAT_MOBILE_APP_ID);

		MedicalConstant.NotoSans_Regular = Typeface.createFromAsset(getAssets(), "fonts/NotoSans-Regular.ttf");
	}

	public boolean isMainProcess() {
		ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		List<RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		String mainProcessName = getPackageName();
		int myPid = android.os.Process.myPid();
		for (RunningAppProcessInfo info : processInfos) {
			if (info.pid == myPid && mainProcessName.equals(info.processName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 初始化信鸽推送
	 */
	private void initTencentXgPush() {
		// TencentXgPush.config(this, MedicalConstant.XG_V2_ACCESS_ID,
		// MedicalConstant.XG_V2_ACCESS_KEY);
		TencentXgPush.registerPush(this);
		TencentXgPush.customPushNotifyLayout(this);
	}

	@Override
	public int getDeviceId() {
		int deviceId = MdgConfig.getDeviceId();
		return deviceId;
	}

	/**
	 * 初始化Image缓存
	 */
	public void initImageLoader() {
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.imageDownloader(new MedicalImageDownload(getApplicationContext())).build();
		ImageLoader.getInstance().init(configuration);
	}

	/**
	 * 拷贝地址信息数据库到，sdcard yideguan/data目录下
	 */
	private void copyCitDatabase() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Basedao baseDao = new Basedao(getApplicationContext());
				File mWorkingPath = new File(baseDao.getSavePath() + "/city");
				if (mWorkingPath.exists()) {
					return;
				} else {
					new Basedao(getApplicationContext()).CopyAssets("city");
				}
			}
		}).start();
	}

	@Override
	public String getDeviceToken() {
		return XGPushConfig.getToken(this);
	}

}

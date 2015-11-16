package com.mdground.yizhida.activity.warehousing;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.http.Header;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.client.android.camera.AmbientLightManager;
import com.google.zxing.client.android.camera.BeepManager;
import com.google.zxing.client.android.camera.CameraManager;
import com.google.zxing.client.android.camera.InactivityTimer;
import com.google.zxing.client.android.decode.CaptureActivityHandler;
import com.google.zxing.client.android.view.ViewfinderView;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.api.server.clinic.GetDurgInfoByBarcode;
import com.mdground.yizhida.api.utils.L;
import com.mdground.yizhida.view.TitleBar;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BarcodeScanActivity extends TitleBarActivity implements SurfaceHolder.Callback {

	private static final String TAG = "WareHousingActivity";

	private TextView tv_badge;

	private CameraManager mCameraManager;
	private CaptureActivityHandler mHandler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;

	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> mDecodeHints;
	private String characterSet;

	private boolean hasSurface;

	private InactivityTimer inactivityTimer;
	private BeepManager beepManager;
	private AmbientLightManager ambientLightManager;

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return mHandler;
	}

	public CameraManager getCameraManager() {
		return mCameraManager;
	}

	@Override
	public int getContentLayout() {
		return R.layout.activity_barcode_scan;
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);

		titleBar.setTitle(getResources().getString(R.string.warehousing));
		titleBar.setBackgroundResource(R.drawable.top_bg1);

		RelativeLayout rlt_layout = titleBar.inflateView(TitleBar.RIGHT, RelativeLayout.class);
		tv_badge = (TextView) rlt_layout.findViewById(R.id.tv_badge);
	}

	@Override
	public void findView() {

	}

	@Override
	public void initView() {

	}

	@Override
	public void initMemberData() {
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		ambientLightManager = new AmbientLightManager(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mCameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(mCameraManager);

		inactivityTimer.onResume();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();

		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
		}

		beepManager.updatePrefs();
		ambientLightManager.start(mCameraManager);
	}

	@Override
	protected void onPause() {
		if (mHandler != null) {
			mHandler.quitSynchronously();
			mHandler = null;
		}
		inactivityTimer.onPause();
		ambientLightManager.stop();
		mCameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void setListener() {

	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (mCameraManager.isOpen()) {
			Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			mCameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (mHandler == null) {
				mHandler = new CaptureActivityHandler(this, decodeFormats, mDecodeHints, characterSet, mCameraManager);
			}
			// decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			// displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			// displayFrameworkBugMessageAndExit();
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();
		
		String barCode = rawResult.getText();
		
		new GetDurgInfoByBarcode(getApplicationContext()).getDurgInfoByBarcode(barCode, new RequestCallBack() {
			
			@Override
			public void onSuccess(ResponseData response) {
				
			}
			
			@Override
			public void onStart() {
				showProgress();
			}
			
			@Override
			public void onFinish() {
				hidProgress();
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				hidProgress();
			}
		});

//		mHandler.sendEmptyMessageDelayed(R.id.restart_preview, 0);
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}

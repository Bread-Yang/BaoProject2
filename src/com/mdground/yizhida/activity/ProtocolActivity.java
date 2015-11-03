package com.mdground.yizhida.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;

/**
 * 医直达软件服务协议
 * 
 * @author Vincent
 * 
 */
public class ProtocolActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_protocol);

		WebView wv = (WebView) findViewById(R.id.wv_protocol);
		WebSettings settings = wv.getSettings();
		
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		wv.loadUrl("file:///android_asset/protocol.html");
	}

	@Override
	public void findView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initMemberData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub
		
	}

}

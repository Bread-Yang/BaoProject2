package com.mdground.yizhida.activity.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.mdground.yizhida.R;
import com.mdground.yizhida.view.TitleBar;
import com.mdground.yizhida.view.TitleBar.TitleBarCallBack;

/**
 * 带有titleBar的activity
 * 
 * @author Vincent
 * 
 */
public abstract class TitleBarActivity extends BaseActivity implements TitleBarCallBack, BaseView {

	private TitleBar mTitleBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_titlebar);
		ViewStub contentStub = (ViewStub) findViewById(R.id.stub_content);
		if (getContentLayout() != 0) {
			contentStub.setLayoutResource(getContentLayout());
			contentStub.inflate();
		}
		findView();
		initMemberData();
		initView();
		setListener();
		mTitleBar = (TitleBar) findViewById(R.id.titleBar);
		mTitleBar.setTitleBarCallBack(this);
		initTitleBar(mTitleBar);

	}

	/**
	 * 获取content部分资源文件
	 * 
	 * @return
	 */
	public abstract int getContentLayout();

	public abstract void initTitleBar(TitleBar titleBar);

	@Override
	public void onLeftClick(View v) {
		onBackPressed();
	}

	@Override
	public void onTitleClick(View v) {

	}

	@Override
	public void onRightClick(View v) {

	}
}

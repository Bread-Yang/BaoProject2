package com.mdground.yizhida.activity.income;

import android.view.View;
import android.widget.ImageView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.view.TitleBar;

/**
 * 收益详情
 * 
 * @author Vincent
 * 
 */
public class TurnOutActivity extends TitleBarActivity {

	@Override
	public void findView() {
	}

	@Override
	public void initView() {

	}

	@Override
	public void initMemberData() {

	}

	@Override
	public void setListener() {
	}

	@Override
	public void onLeftClick(View v) {
		onBackPressed();
	}

	@Override
	public int getContentLayout() {
		return R.layout.activity_turn_out;
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		titleBar.setTitle("转出");
		titleBar.setBackgroundResource(R.drawable.top_bg4);
	}

}

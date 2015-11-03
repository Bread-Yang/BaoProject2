package com.mdground.yizhida.activity.income;

import java.util.Arrays;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.TitleBarActivity;
import com.mdground.yizhida.adapter.IncomeCategoryAdapter;
import com.mdground.yizhida.adapter.IncomeInfoAdapter;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.view.TitleBar;

/**
 * 收益分类查看
 * 
 * @author Vincent
 * 
 */
public class IncomeInfoActivity extends TitleBarActivity {

	private int currentType = -1;
	private List<String> categorys;
	private TextView mTitleView;
	private Drawable closeDrawable;
	private Drawable openDrawable;
	private IncomeCategoryAdapter mCategoryAdapter;
	
	private ListView mListView;
	private IncomeInfoAdapter mInfoAdapter;

	@Override
	public void findView() {
		mListView = (ListView) findViewById(R.id.lv_income);
	}

	@Override
	public void initView() {
		mListView.setAdapter(mInfoAdapter);
	}

	@Override
	public void initMemberData() {
		currentType = getIntent().getIntExtra(MemberConstant.INCOME_TYPE, -1);
		String[] strCategorys = getResources().getStringArray(R.array.income_category);
		categorys = Arrays.asList(strCategorys);
		closeDrawable = getResources().getDrawable(R.drawable.down);
		closeDrawable.setBounds(0, 0, closeDrawable.getMinimumWidth(), closeDrawable.getMinimumHeight());
		openDrawable = getResources().getDrawable(R.drawable.up);
		closeDrawable.setBounds(0, 0, openDrawable.getMinimumWidth(), openDrawable.getMinimumHeight());
		mInfoAdapter = new IncomeInfoAdapter(this);
	}


	@Override
	public void setListener() {
	}

	@Override
	public void onTitleClick(View v) {
	}

	@Override
	public int getContentLayout() {
		return R.layout.activity_income_info;
	}

	@Override
	public void initTitleBar(TitleBar titleBar) {
		ImageView imageVeiw = titleBar.inflateView(TitleBar.LEFT, ImageView.class);
		imageVeiw.setImageResource(R.drawable.back);
		if (currentType != -1) {
			mTitleView = titleBar.getTitleView();
			mTitleView.setText(categorys.get(currentType));
			mTitleView.setCompoundDrawables(null, null, closeDrawable, null);
		}
		titleBar.setBackgroundResource(R.drawable.top_bg4);
	}

}

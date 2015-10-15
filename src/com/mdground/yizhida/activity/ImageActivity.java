package com.mdground.yizhida.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.mdground.yizhida.R;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.view.PhotoView;
import com.mdground.yizhida.view.PhotoView.ImageClick;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageActivity extends Activity implements ImageClick {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		PhotoView view = new PhotoView(this, 300, 300);
		Intent intent = getIntent();
		if (intent != null) {
			int gender = intent.getIntExtra(MemberConstant.EMPLOYEE_GENDER, 1);
			if (gender == 2) {
				view.setImageDrawable(getResources().getDrawable(R.drawable.head_lady));
			} else {
				view.setImageDrawable(getResources().getDrawable(R.drawable.head_man));
			}
			String photoUrl = intent.getStringExtra(MemberConstant.EMPLOYEE_PHOTO_URL);
			if (photoUrl != null && !photoUrl.equals("")) {
				ImageLoader.getInstance().displayImage(photoUrl, view);
			}
		}
		view.setImgeClick(this);
		setContentView(view);
	}

	@Override
	public void onImageClick() {
		finish();
	}

}

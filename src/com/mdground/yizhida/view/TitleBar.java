package com.mdground.yizhida.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mdground.yizhida.R;

/**
 * 顶部， 左侧
 * 
 * @author Vincent
 * 
 */
public class TitleBar extends FrameLayout {
	public static final int LEFT = 1;
	public static final int RIGHT = 2;

	private ViewStub mLeftView;
	private TextView mTitleView;
	private ViewStub mRightView;

	private View contentView;
	private View mLeft;
	private View mRight;
	private TitleBarCallBack mCallBack;

	public static interface TitleBarCallBack {
		public void onLeftClick(View v);

		public void onTitleClick(View v);

		public void onRightClick(View v);
	}

	public TitleBar(Context context) {
		this(context, null, 0);
	}

	public TitleBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		contentView = LayoutInflater.from(getContext()).inflate(R.layout.title_bar, null);
		if (!isInEditMode()) {
			mLeftView = (ViewStub) contentView.findViewById(R.id.stub_left);
			mRightView = (ViewStub) contentView.findViewById(R.id.stub_right);
			mTitleView = (TextView) contentView.findViewById(R.id.title);
			addView(contentView);
		}
	}

	@Override
	protected void onFinishInflate() {
		if (mTitleView != null) {
			mTitleView.setOnClickListener(mOnClickListener);
		}
	}

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mCallBack == null) {
				return;
			}

			if (mLeft != null && v.getId() == mLeft.getId()) {
				mCallBack.onLeftClick(v);
			} else if (mRight != null && v.getId() == mRight.getId()) {
				mCallBack.onRightClick(v);
			} else if (mTitleView != null && v.getId() == mTitleView.getId()) {
				mCallBack.onTitleClick(v);
			}
		}
	};

	/**
	 * 设置控件,只支持 textView和imageView
	 * 
	 * @param witchView
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T inflateView(int witchView, Class<T> clazz) {
		T view = null;
		switch (witchView) {
		case LEFT:
			view = (T) (mLeft = inflateImageView(mLeftView, clazz));
			break;
		case RIGHT:
			view = (T) (mRight = inflateImageView(mRightView, clazz));
			break;

		default:
			break;
		}
		if (view != null) {
			view.setOnClickListener(mOnClickListener);
		}
		return view;
	}

	private View inflateImageView(ViewStub viewStub, Class<? extends View> clazz) {
		View view = null;
		if (viewStub != null) {
			if (clazz.equals(ImageView.class)) {
				viewStub.setLayoutResource(R.layout.title_bar_imge);
				viewStub.inflate();
				view = contentView.findViewById(R.id.imgView);
			} else if (clazz.equals(TextView.class)) {
				viewStub.setLayoutResource(R.layout.title_bar_text);
				viewStub.inflate();
				view = contentView.findViewById(R.id.text);
			} else if (clazz.equals(RelativeLayout.class)) {
				viewStub.setLayoutResource(R.layout.title_bar_relativelayout);
				viewStub.inflate();
				view = contentView.findViewById(R.id.rlt_layout);
			}
		}
		return view;
	}

	public void setTitle(String title) {
		if (mTitleView != null) {
			mTitleView.setText(title);
		}
	}

	public TextView getTitleView() {
		return this.mTitleView;
	}

	public void setTitleBarCallBack(TitleBarCallBack callBack) {
		this.mCallBack = callBack;
	}

	@Override
	public void setBackgroundResource(int resid) {
		if (contentView != null) {
			contentView.setBackgroundResource(resid);
		} else {
			super.setBackgroundResource(resid);
		}
	}

	@Override
	public void setBackgroundColor(int color) {
		if (contentView != null) {
			contentView.setBackgroundColor(color);
		} else {
			super.setBackgroundColor(color);
		}
	}

}

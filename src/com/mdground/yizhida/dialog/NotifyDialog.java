package com.mdground.yizhida.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.mdground.yizhida.R;

/**
 * 提示消息
 * 
 * @author Vincent
 * 
 */
public class NotifyDialog extends Dialog implements android.view.View.OnClickListener {
	private TextView tvTitle;
	private TextView tvContent;
	private TextView mSure;
	private TextView mCancle;
	private OnSureClickListener listener;

	public static interface OnSureClickListener {
		public void onSureClick();
	}
	

	public NotifyDialog(Context context) {
		this(context, R.style.appointmentDialog);
	}

	public NotifyDialog(Context context, int theme) {
		super(context, theme);
	}

	protected NotifyDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = LinearLayout.inflate(getContext(), R.layout.dialog_notify, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvContent = (TextView) view.findViewById(R.id.tv_message);
		mSure = (TextView) view.findViewById(R.id.btn_sure);
		mCancle = (TextView) view.findViewById(R.id.btn_cancle);
		mSure.setOnClickListener(this);
		mCancle.setOnClickListener(this);
		setContentView(view);

		getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			if (this.listener != null) {
				this.listener.onSureClick();
			} else {
				dismiss();
			}
			break;
		case R.id.btn_cancle:
			dismiss();
			break;

		default:
			break;
		}

	}

	public void setOnSureClickListener(OnSureClickListener listener) {
		this.listener = listener;
	}

	@Override
	public void setTitle(CharSequence title) {
		if (this.tvTitle == null) {
			return;
		}
		this.tvTitle.setText(title);
	}

	public void setMessage(CharSequence message) {
		if (tvContent == null) {
			return;
		}
		this.tvContent.setText(message);
	}

	public void setSureMessage(CharSequence string) {
		if (mSure == null) {
			return;
		}
		this.mSure.setText(string);
	}
}

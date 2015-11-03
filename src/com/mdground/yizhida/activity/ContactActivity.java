package com.mdground.yizhida.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.bean.Employee;

public class ContactActivity extends BaseActivity {

	private LinearLayout mobilphoneLayout;
	private Employee employe;
	TextView spportPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		employe = ((MedicalAppliction) getApplication()).getLoginEmployee();
		if ((employe.getSupportName() == null || employe.getSupportName().equals(""))
				&& (employe.getSupportPhone() == null || employe.getSupportPhone().equals(""))) {
			findViewById(R.id.layout_mobilphone).setVisibility(View.GONE);
		} else {
			TextView spportName = (TextView) findViewById(R.id.tv_supportName);
			spportPhone = (TextView) findViewById(R.id.tv_mobil_phone_number);
			spportName.setText(employe.getSupportName());
			String supportPhone = employe.getSupportPhone();
			StringBuffer sbuffer = new StringBuffer();
			sbuffer.append(supportPhone.subSequence(0, 3));
			sbuffer.append("-");
			sbuffer.append(supportPhone.subSequence(3, 7));
			sbuffer.append("-");
			sbuffer.append(supportPhone.subSequence(7, 11));
			spportPhone.setText(sbuffer.toString());
			spportPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		}

		findViewById(R.id.back).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();

			}
		});

		findViewById(R.id.layout_mobilphone).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (spportPhone != null && spportPhone.getText().length() > 0) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + spportPhone.getText().toString()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}

			}
		});
		TextView tv_email = (TextView) findViewById(R.id.tv_email);
		tv_email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		findViewById(R.id.layout_email).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				String[] recipients = new String[] { "support@yideguan.com", "", };
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Test");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is email's message");
				emailIntent.setType("text/plain");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});

		TextView tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
		tv_phone_number.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		findViewById(R.id.layout_phone).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:020-22091461"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
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

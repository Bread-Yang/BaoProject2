package com.mdground.yizhida.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.R;
import com.mdground.yizhida.activity.base.BaseActivity;
import com.mdground.yizhida.constant.MemberConstant;
import com.mdground.yizhida.util.ToolPicture;
import com.mdground.yizhida.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

public class QrcodeActivity extends BaseActivity implements OnClickListener {
	private static final int QR_HEIGHT = 700;
	private static final int QR_WIDTH = 700;

	private String qrcodeUrl;
	private String photoUrl;
	private String name;
	private String clinc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrcode);
		findViewById(R.id.back).setOnClickListener(this);
		findViewById(R.id.save).setOnClickListener(this);
		Intent intent = getIntent();
		if (intent != null) {
			qrcodeUrl = intent.getStringExtra(MemberConstant.EMPLOYEE_QRCODE);
			photoUrl = intent.getStringExtra(MemberConstant.EMPLOYEE_PHOTO_URL);
			name = intent.getStringExtra(MemberConstant.EMPLOYEE_NAME);
			clinc = intent.getStringExtra(MemberConstant.EMPLOYEE_CLINIC_NAME);

			if (photoUrl != null) {
				ImageLoader.getInstance().displayImage(photoUrl, (ImageView) findViewById(R.id.img_head));
			}

			TextView nameTv = (TextView) findViewById(R.id.tv_name);
			nameTv.setText(name);
			TextView clincName = (TextView) findViewById(R.id.tv_clinic_name);
			clincName.setText(clinc);
			// displayQrCode(qrcodeUrl, (ImageView)
			if (qrcodeUrl != null && !qrcodeUrl.equals("")) {
				ImageView imageView = (ImageView) findViewById(R.id.img_qrcode);
				try {
					Bitmap logBitmap = ImageLoader.getInstance().loadImageSync(photoUrl);
					Bitmap bitmap = ToolPicture.makeQRImageWithLogo(qrcodeUrl, QR_WIDTH, QR_HEIGHT, logBitmap);
					imageView.setImageBitmap(bitmap);
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			onBackPressed();
			break;
		case R.id.save:
			saveImage();
			break;
		default:
			break;
		}
	}

	private void saveImage() {
		String sdCardPath = Tools.getAppPath();
		if (sdCardPath == null || sdCardPath.equals("")) {
			Toast.makeText(this, "请插入sdcard", Toast.LENGTH_SHORT).show();
			return;
		}

		File file = new File(sdCardPath + File.separator + MedicalConstant.APP_QRCODE);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		File qrFile = new File(file.getAbsolutePath() + File.separator + name + "_" + clinc + ".png");
		if (qrFile.exists()) {
			qrFile.delete();
		}

		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(qrFile);
			Bitmap bitmap = ToolPicture.loadBitmapFromView(findViewById(R.id.layout_qrcode));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			Toast.makeText(this, "保存成功:" + qrFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
			bitmap.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fOut != null) {
					fOut.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

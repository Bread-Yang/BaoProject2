package com.mdground.yizhida.util;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mdground.yizhida.MedicalConstant;
import com.mdground.yizhida.R;

/**
 * custom menu
 * 
 * @author eilin
 * 
 */
public class GetPhotoPop extends PopupWindow implements OnTouchListener, OnClickListener {

	public static final int PHOTO_REQUEST_GALLERY = 1;// 相册
	public static final int PHOTO_REQUEST_CAREMA = 2;// 拍照
	public static final int PHOTO_REQUEST_CUT = 3;// 剪切

	private Activity activity;

	Uri imageUri;

	public GetPhotoPop(Activity activity) {
		this.activity = activity;

		View view = View.inflate(activity, R.layout.layout_editicon_pop, null);
		setContentView(view);
		setWidth(ViewGroup.LayoutParams.FILL_PARENT);
		setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		setOutsideTouchable(false);
		setTouchInterceptor(this);
		// view.setOnKeyListener(this);
		view.findViewById(R.id.takephoto).setOnClickListener(this);
		view.findViewById(R.id.getlocal).setOnClickListener(this);
		view.findViewById(R.id.modify_note).setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		dismiss();
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.takephoto:
			camera();
			break;
		case R.id.getlocal:
			gallery();
			break;
		case R.id.modify_note:
			dismiss();
			break;

		default:
			break;
		}
	}

	/*
	 * 从相册获取
	 */
	private void gallery() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/*
	 * 从相机获取
	 */
	private void camera() {
		if (isSdCardMounted()) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			/** ----------------- */
			activity.startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
		} else {
			Toast.makeText(activity, "内存卡不存在", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 截图
	 * 
	 * @param uri
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 */
	public void cropImageUri(Uri uri) {
		if (uri == null) {
			return;
		}

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	public File getTempFile() {
		if (isSdCardMounted()) {

			File f = new File(Environment.getExternalStorageDirectory() + File.separator + MedicalConstant.APP_PATH, "tmp.jpg");
			// try {
			// f.createNewFile();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			return f;
		} else {
			return null;
		}
	}

	public Uri getTempUri() {
		return Uri.fromFile(getTempFile());
	}

	private boolean isSdCardMounted() {
		String status = Environment.getExternalStorageState();

		if (status.equals(Environment.MEDIA_MOUNTED))
			return true;
		return false;
	}

	public Uri getImageUri() {
		return this.imageUri;
	}

	public void setImageUri(Uri uri) {
		this.imageUri = uri;
	}

	/**
	 * Try to return the absolute file path from the given Uri
	 * 
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri, new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}
}

package com.mdground.yizhida.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * 上传图片时的一些处理图片方法
 * 
 * @author win
 * 
 */
public class GetPhotoTool {

	public static final int PHOTO_REQUEST_GALLERY = 1;// 相册
	public static final int PHOTO_REQUEST_CAREMA = 2;// 拍照
	public static final int PHOTO_REQUEST_CUT = 3;// 剪切

	private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";// temp
																				// file
	Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);// The Uri to store the big
													// bitmap

	private GetPhotoTool(Context context) {
		// this.context = context;

	}

	/*
	 * 从相册获取
	 */
	public void gallery(Context context) {
		// // 激活系统图库，选择一张图片
		// Intent intent = new Intent(Intent.ACTION_PICK);
		// intent.setType("image/*");
		// // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
		// ((Activity) context).startActivityForResult(intent,
		// PHOTO_REQUEST_GALLERY);

		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 100);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}

	/*
	 * 从相机获取
	 */
	public void camera(Context context) {
		if (hasSdcard()) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			/***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 */
			ContentValues values = new ContentValues();
			imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
			/** ----------------- */
			((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
		} else {
			Toast.makeText(context, "内存卡不存在", Toast.LENGTH_LONG).show();
		}

	}

	private boolean hasSdcard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/*
	 * 剪切图片
	 */
	public void crop(Context context, Uri uri, int width, int height) {
		// 裁剪图片意图
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// 裁剪框的比例，1：1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// 裁剪后输出图片的尺寸大小
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);

		intent.putExtra("outputFormat", "JPEG");// 图片格式
		intent.putExtra("noFaceDetection", true);// 取消人脸识别
		intent.putExtra("return-data", true);
		// 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
		((Activity) context).startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

}

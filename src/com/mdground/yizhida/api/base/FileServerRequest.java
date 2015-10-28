package com.mdground.yizhida.api.base;

import android.content.Context;


public abstract class FileServerRequest extends BaseRequest {

	// 文件服务器地址
	private static final String FILE_SERVER_URL = "http://fuat.yideguan.com/Api/RpcService.ashx";
//	private static final String FILE_SERVER_URL = "http://file.yideguan.com/api/RpcService.ashx";
//	private static final String FILE_SERVER_URL = "http://192.168.0.107:8888/api/RpcService.ashx";

	public FileServerRequest(Context context) {
		super(context);
	}

	@Override
	protected String getUrl() {
		return FILE_SERVER_URL;
	}

	@Override
	protected int getBusinessCode() {
		return BusinessType.FILE.getType();
	}

}

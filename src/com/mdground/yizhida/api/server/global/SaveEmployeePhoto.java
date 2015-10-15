package com.mdground.yizhida.api.server.global;

import android.content.Context;

import com.google.gson.Gson;
import com.mdground.yizhida.api.base.GlobalRequest;
import com.mdground.yizhida.api.base.RequestCallBack;

public class SaveEmployeePhoto extends GlobalRequest {
	private static final String FUNCTION_NAME = "SaveEmployeePhoto";

	public SaveEmployeePhoto(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	public void saveEmployeePhoto(int photoID, int photoSID, RequestCallBack requestCallBack) {
		setRequestCallBack(requestCallBack);

		PhotoQuery query = new PhotoQuery();
		query.PhotoID = photoID;
		query.PhotoSID = photoSID;

		Gson gson = new Gson();
		String queryStr = gson.toJson(query);
		getData().setQueryData(queryStr);

		pocess();
	}

	protected class PhotoQuery {
		int PhotoID;
		int PhotoSID;

		public int getPhotoID() {
			return PhotoID;
		}

		public void setPhotoID(int photoID) {
			PhotoID = photoID;
		}

		public int getPhotoSID() {
			return PhotoSID;
		}

		public void setPhotoSID(int photoSID) {
			PhotoSID = photoSID;
		}

	}
}

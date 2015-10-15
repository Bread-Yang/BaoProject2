package com.mdground.yizhida.api.server.fileserver;

import android.content.Context;

import com.google.gson.Gson;
import com.mdground.yizhida.api.MdgAppliction;
import com.mdground.yizhida.api.base.FileServerRequest;
import com.mdground.yizhida.api.base.RequestCallBack;
import com.mdground.yizhida.api.base.RequestData;
import com.mdground.yizhida.api.base.ResponseData;
import com.mdground.yizhida.bean.Employee;

public class GetFile extends FileServerRequest {
	public static final String FUNCTION_NAME = "GetFile";

	public GetFile(Context context) {
		super(context);
	}

	@Override
	protected String getFunctionName() {
		return FUNCTION_NAME;
	}

	// 同步调用
	public ResponseData getFile(int clinicID, long fileID) {

		if (clinicID == 0) {
			MdgAppliction appliction = null;
			if (mContext != null && mContext.getApplicationContext() instanceof MdgAppliction) {
				appliction = (MdgAppliction) mContext.getApplicationContext();
			} else {
				throw new RuntimeException("please create MdgAppliction !!!");
			}

			if (appliction == null) {
				return null;
			}

			Employee loginEmployee = appliction.getLoginEmployee();
			if (loginEmployee == null) {
				throw new RuntimeException("please login ");
			}
			clinicID = loginEmployee.getClinicID();
		}

		GetFileQuery query = new GetFileQuery();
		query.setClinicID(clinicID);
		query.setFileID(fileID);

		Gson gons = new Gson();
		String queryJson = gons.toJson(query);

		RequestData data = getData();
		data.setQueryData(queryJson);

		return pocessSync(false);
	}

	// 异步调用
	public void getFile(int clinicID, long fileID, RequestCallBack callBack) {

		setRequestCallBack(callBack);

		GetFileQuery query = new GetFileQuery();
		query.setClinicID(clinicID);
		query.setFileID(fileID);

		Gson gons = new Gson();
		String queryJson = gons.toJson(query);

		RequestData data = getData();
		data.setQueryData(queryJson);

		pocess(false);
	}

	class GetFileQuery {
		private int ClinicID;
		private long FileID;

		public int getClinicID() {
			return ClinicID;
		}

		public void setClinicID(int clinicID) {
			ClinicID = clinicID;
		}

		public long getFileID() {
			return FileID;
		}

		public void setFileID(long fileID) {
			FileID = fileID;
		}

	}

}

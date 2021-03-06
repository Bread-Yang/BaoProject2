package com.mdground.yizhida.wxapi;

import com.mdground.yizhida.MedicalAppliction;
import com.mdground.yizhida.api.utils.L;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private Context context = WXEntryActivity.this;
	
	public static String wechat_code = null;

	private void handleIntent(Intent paramIntent) {
		MedicalAppliction.api.handleIntent(paramIntent, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handleIntent(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub
		L.e(this, "onReq()");
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		L.e(this, "onResp()");
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType()) {
				break;
			}
			wechat_code = ((SendAuth.Resp) resp).code;
			
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			break;
		}
		finish();
	}
}

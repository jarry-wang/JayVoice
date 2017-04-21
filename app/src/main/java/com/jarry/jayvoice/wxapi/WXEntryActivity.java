package com.jarry.jayvoice.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.activity.BaseActivity;
import com.jarry.jayvoice.core.Config;
import com.jarry.jayvoice.util.Logger;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{

	private IWXAPI api;
	private final int WX_LOGIN = 0X01;
	public final int GET_COM_INFO = 0x03;
	final int RESULT_GO_SELECTCOM = 0x04;
	public final int SHARE_POINTS = 0x05;
	String fromAc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Config.wx_APP_ID, false);
		api.handleIntent(getIntent(), this);
	}
	
	@Override
	public void onReq(BaseReq req) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		Logger.d("WXEntryActivity--resp="+resp);
		if(resp instanceof SendAuth.Resp){
			SendAuth.Resp r = (SendAuth.Resp)resp;//这里做一下转型就是
		    String code = r.code;//这样就拿到了，就可以进行下一步了。
		    Logger.d("WXEntryActivity--onResp--code="+code);
//		    fromAc = application.loginFromReq;
		    doWxLogin(code);
		    return;
		}else{
			Logger.d("WXEntryActivity--resp.errCode="+resp.errCode);
			int result = 0;
			String resultStr = "";
			switch (resp.errCode){		
		    case BaseResp.ErrCode.ERR_OK:
		      result = R.string.errcode_success;
		      break;
		    case BaseResp.ErrCode.ERR_USER_CANCEL:
		      result = R.string.errcode_cancel;
		      break;
		    case BaseResp.ErrCode.ERR_AUTH_DENIED:
		      result = R.string.errcode_deny;
		      break;
		    default:
		      result = R.string.errcode_unknown;
		      break;
		    }
			showToast(getString(result));
		}
		this.finish();
		
	}
	
	private void doWxLogin(String code) {
//		WxLoginReq loginReq = new WxLoginReq();
//		loginReq.registration_id = application.getRegistration_id();
//		loginReq.code = code;
//		mDataBusiness.setStyleText("正在登录...");
//		mDataBusiness.doWxLogin(handler, WX_LOGIN, loginReq);
	}
	
//	@Override
//	public void handleMsg(Message msg) {
//		// TODO Auto-generated method stub
//		super.handleMsg(msg);
//		switch (msg.what) {
//		case WX_LOGIN:
//			
//			break;
//		case GET_COM_INFO:
//			
//			break;
//		case SHARE_POINTS:
//			
//			break;
//		}
//	}
	
	
	private void goFinish(int resultCode) {
		// TODO Auto-generated method stub
		if(resultCode==RESULT_GO_SELECTCOM){
			
			finish();
		}else{			
			if(fromAc!=null&&fromAc.equals("MainActivity")){
				
				finish();
			}else{
				finish();
			}
		}				
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return 0;
	}
	

}

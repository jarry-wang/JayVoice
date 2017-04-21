package com.jarry.jayvoice.activity;


import com.jarry.jayvoice.R;
import com.jarry.jayvoice.core.GetDataBusiness.SaveHandler;
import com.jarry.jayvoice.util.StringUtils;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class FeedBackActivity extends BaseActivity {
	private TextView commitView,ifdoConfirm;
	EditText contentView,contactView;
	String content,contact;
	private final int DO_FEED_BACK = 0x01;
	Dialog successDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		setTitle(getString(R.string.feedback_title));
		contentView = (EditText) findViewById(R.id.feedback_content_edittext);
		contactView = (EditText) findViewById(R.id.feedback_contact_edittext);
		commitView = (TextView) findViewById(R.id.feedback_commit_textview);
	}

	@Override
	public void getData() {}

	@Override
	public void showData() {}
	
	
	public void doCommit(View v) {
		// TODO Auto-generated method stub
		if(checkFeedbackInfo()){
			getDataBusiness.addFeedBack(contact, content, new SaveHandler() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					showDoSuccessDialog();
				}
				
				@Override
				public void onError(int errorCode, String msg) {
					// TODO Auto-generated method stub
					showToast(msg);
				}
			});
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		super.onClick(arg0);
		switch (arg0.getId()) {
		case R.id.input_feedback_conform:
			if (successDialog.isShowing()) {
				successDialog.dismiss();
			}
			this.finish();
			break;

		default:
			break;
		}
	}
	
//	private void checkFeedbackResult(CommonResult result) {
//		// TODO Auto-generated method stub
//		if(result.isSuccess()){
//			showDoSuccessDialog();
//		}else{
//			showToast(this, result.getMsg());
//		}
//	}
	
	private void showDoSuccessDialog() {
		// TODO Auto-generated method stub
		successDialog = new Dialog(this, R.style.MyDialog);
		successDialog.setContentView(R.layout.feedback_dialog);
		TextView doInfoView = (TextView) successDialog.findViewById(R.id.textView_do_info);
		doInfoView.setText("提交成功，感谢您的反馈");
		ifdoConfirm = (TextView) successDialog.findViewById(R.id.input_feedback_conform);
		ifdoConfirm.setOnClickListener(this);
		successDialog.show();
	}
	
	private boolean checkFeedbackInfo() {
		// TODO Auto-generated method stub
		content = contentView.getText().toString().trim();
		contact = contactView.getText().toString().trim();
		if(StringUtils.isNull(content)){
			showToast("请输入反馈内容");
			return false;
		}
		if(StringUtils.isNull(contact)){
			showToast("请输入联系方式");
			return false;
		}
		if(StringUtils.isMobileNO(contact)||StringUtils.isEmail(contact)){			
		}else{
			showToast("请输入正确的手机或邮箱");
			return false;
		}
		return true;
	}


	@Override
	public int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_feedback_add;
	}
}

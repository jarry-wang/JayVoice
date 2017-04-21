package com.jarry.jayvoice.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import com.jarry.jayvoice.R;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Used to select a local photo
 * 
 * @author wangfj
 * 
 */
public class PhotoSelUtil implements OnClickListener{
	public final int IMAGE_REQUEST_CODE = 0;
	public final int CAMERA_REQUEST_CODE = 1;
	public final int RESIZE_REQUEST_CODE = 2;
	private final String IMAGE_FILE_NAME = "header.jpg";
	private String pzPath;//图片的base64String
	private String imgString;//图片的base64String
	private Bitmap photo;
	private Activity mContext;
	private int getType;//0 需裁剪 1不需裁剪
	private Dialog dialog;
	private int resizeHeight;
	
	public String getImgString() {
		String temp =  imgString;
		imgString = null;
		return temp;
	}
	
	public Bitmap getBitmap(){
		return photo;
	}		

	public void setGetType(int getType) {
		this.getType = getType;
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.textView_pic:
//			Intent galleryIntent = new Intent(
//					Intent.ACTION_GET_CONTENT);
//			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//			galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
//			galleryIntent.setType("image/*");
//			galleryIntent.putExtra("scaleUpIfNeeded", true);	
//			mContext.startActivityForResult(galleryIntent,
//					IMAGE_REQUEST_CODE);
			Intent intentPick = new Intent(Intent.ACTION_PICK, null);
            intentPick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            mContext.startActivityForResult(intentPick,
					IMAGE_REQUEST_CODE);
            dialog.dismiss();
			break;
		case R.id.textView_pz:
			if (isSdcardExisting()) {
				Intent cameraIntent = new Intent(
						"android.media.action.IMAGE_CAPTURE");
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						getImageUri());
				cameraIntent.putExtra(
						MediaStore.EXTRA_VIDEO_QUALITY, 0);
				mContext.startActivityForResult(cameraIntent,
						CAMERA_REQUEST_CODE);
			} else {
				Toast.makeText(mContext, "请插入sd卡",
						Toast.LENGTH_LONG).show();
			}
			dialog.dismiss();
			break;
		case R.id.textView_cancel:
			dialog.dismiss();
			break;
		}
	}

	public void showPhoto(final Activity mActivity,String title) {
		mContext = mActivity;
		dialog = new Dialog(mContext, R.style.ShowPicDialog);
		dialog.setContentView(R.layout.set_pic_dialog);
		TextView titleView = (TextView) dialog.findViewById(R.id.textView_title);
		TextView picView = (TextView) dialog.findViewById(R.id.textView_pic);
		TextView pzView = (TextView) dialog.findViewById(R.id.textView_pz);
		TextView caecelView = (TextView) dialog.findViewById(R.id.textView_cancel);
		titleView.setText(title);
		picView.setOnClickListener(this);
		pzView.setOnClickListener(this);
		caecelView.setOnClickListener(this);
		dialog.show();
	}

	

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		} else {
			switch (requestCode) {
			case IMAGE_REQUEST_CODE:
				if(getType==0){
					resizeImage(data.getData());
				}else{
					if(getType==2){
						resizeHeight = 800;
					}else{
						resizeHeight = 400;
					}
					getBitMap(data.getData());
				}										
				break;
			case CAMERA_REQUEST_CODE:
				if (isSdcardExisting()) {
					if(getType==0){
						resizeImage(getImageUri());
					}else{
						if(getType==2){
							resizeHeight = 800;
						}else{
							resizeHeight = 400;
						}
						getPzBitMap(getImagePath());
					}				
				} else {
					Toast.makeText(mContext, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG)
							.show();
				}
				break;

			case RESIZE_REQUEST_CODE:
				if (data != null) {
					showResizeImage(data);
				}
				break;
			}
		} 

	}

	private boolean isSdcardExisting() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	private void resizeImage(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		mContext.startActivityForResult(intent, RESIZE_REQUEST_CODE);
	}

	public void showResizeImage(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			photo = extras.getParcelable("data");
			bitmapToBase64(photo);
		}
	}
	
	/**
	 * bitmap转为base64
	 * @param bitmap
	 * @return
	 */
	public void bitmapToBase64(Bitmap bitmap) {

		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				imgString = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	

	public Uri getImageUri() {
		return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
				IMAGE_FILE_NAME));
	}
	
	public String getImagePath() {
		return Environment.getExternalStorageDirectory()+"/"+IMAGE_FILE_NAME;

	}
	
	private void getBitMap(Uri uri) {
		// TODO Auto-generated method stub
		String fPath = uri2filePath(uri); // 转化为路径
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;  
		Bitmap bitmap = BitmapFactory.decodeFile(fPath,options);	 
		int be = (int)(options.outHeight / (float)resizeHeight);  
		if (be <= 0)  
			be = 1; 
		options.inJustDecodeBounds = false; 
		options.inSampleSize = be;  
		photo=BitmapFactory.decodeFile(fPath,options);  
		bitmapToBase64(photo);
	}
	
	private void getPzBitMap(String path) {
		// TODO Auto-generated method stub
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inJustDecodeBounds = true;  
		Bitmap bitmap = BitmapFactory.decodeFile(path,options);	 
		int be = (int)(options.outHeight / (float)resizeHeight);  
		if (be <= 0)  
			be = 1; 
		options.inJustDecodeBounds = false; 
		options.inSampleSize = be;  
		photo=BitmapFactory.decodeFile(path,options);  
		bitmapToBase64(photo);
	}
	
	private String uri2filePath(Uri uri){
		String[] proj = { MediaStore.Images.Media.DATA };		
		Cursor cursor = mContext.getContentResolver().query(uri,proj,null,null,null);
		if(cursor==null)return "";
		int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(index);
		cursor.close();
		return path;
	}

	
}

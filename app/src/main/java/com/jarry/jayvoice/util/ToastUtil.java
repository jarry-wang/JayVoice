package com.jarry.jayvoice.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.widget.HandyTextView;

public class ToastUtil {

	public static synchronized void showToast(Context context, String text) {
		// TODO Auto-generated method stub
		View toastRoot = LayoutInflater.from(context).inflate(
				R.layout.common_toast, null);
		((HandyTextView) toastRoot.findViewById(R.id.toast_text)).setText(text);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(toastRoot);
		toast.show();
	}
}

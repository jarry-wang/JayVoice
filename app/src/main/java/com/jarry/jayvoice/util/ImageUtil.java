package com.jarry.jayvoice.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jarry.jayvoice.activity.CommonImageActivity;

import cn.join.android.ui.glide.GlideCircleTransform;
import cn.join.android.ui.glide.GlideRoundTransform;

public class ImageUtil {
    private static final String TAG = "ImageUtil";
    public interface ImageDownloadCallBack {
        void downSuccess(Bitmap bitmap);
    }
    public static void setImg(Context context,ImageView imageView, String imageUrl, int nullImgId) {
        if (StringUtils.isNotNull(imageUrl)) {
            if (imageUrl.endsWith(".gif")){
                Glide.with(context).load(imageUrl).asGif().into(imageView);
            }else {
                Glide.with(context).load(imageUrl).centerCrop().into(imageView);
            }
        } else {
            imageView.setImageResource(nullImgId);
        }
    }

    public static void setImg(Context context,ImageView imageView, String imageUrl, int nullImgId,final int[] scaleSize) {
        if (scaleSize.length != 2) return;
        if (StringUtils.isNotNull(imageUrl)) {
            if (imageUrl.endsWith(".gif")){
                Glide.with(context).load(imageUrl).asGif().override(scaleSize[0],scaleSize[1]).into(imageView);
            }else {
                Glide.with(context).load(imageUrl).thumbnail(0.1f).override(scaleSize[0],scaleSize[1]).centerCrop().into(imageView);
            }
        } else {
            imageView.setImageResource(nullImgId);
        }
    }

    public static void downloadImg(final Context context,final String imageUrl,final int[] scaleSize,final ImageDownloadCallBack callBack) {

        Glide.with(context).load(imageUrl).asBitmap().into(new SimpleTarget<Bitmap>() {

            @Override
            public void onLoadStarted(Drawable placeholder) {
                Log.d(TAG,"downloadImg--onLoadStarted");
                super.onLoadStarted(placeholder);
            }

            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Log.d(TAG,"downloadImg--onResourceReady--resource="+resource);
                callBack.downSuccess(resource);
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                Log.d(TAG,"downloadImg--onLoadFailed--"+e.getMessage());
                super.onLoadFailed(e, errorDrawable);
            }
        });

    }

    public static void setCircleImg(Context context,ImageView imageView, String imageUrl, int nullImgId) {
        Glide.with(context).load(imageUrl).placeholder(nullImgId).error(nullImgId).transform(new GlideCircleTransform(context)).into(imageView);
    }

    public static void setRoundImg(Context context,ImageView imageView, String imageUrl, int nullImgId, String imgScareSize,int dp) {
        Glide.with(context).load(imageUrl).placeholder(nullImgId).transform(new GlideRoundTransform(context,dp)).into(imageView);
    }

	public static void gotoImage(Context context,String url,ImageView imageView) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context,CommonImageActivity.class);
		intent.putExtra("url", url);
		int[] location = new int[2];
		imageView.getLocationOnScreen(location);
		intent.putExtra("locationX", location[0]);
		intent.putExtra("locationY", location[1]);

		intent.putExtra("width", imageView.getWidth());
		intent.putExtra("height", imageView.getHeight());
		context.startActivity(intent);
		((Activity) context).overridePendingTransition(0, 0);
	}
	
	public static Bitmap Create2DCode(String str,Context context,int size) throws WriterException {            
        BitMatrix matrix = new MultiFormatWriter().encode(str,BarcodeFormat.QR_CODE, DisplayUtil.getDpSize(size, context), DisplayUtil.getDpSize(size, context));       
        int width = matrix.getWidth();       
        int height = matrix.getHeight();            
        int[] pixels = new int[width * height];       
        for (int y = 0; y < height; y++) {       
            for (int x = 0; x < width; x++) {       
                if(matrix.get(x, y)){       
                    pixels[y * width + x] = 0xff000000;       
                }                            
            }       
        }       
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);   
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);       
        return bitmap;       
    }   
	
	//把一个url的网络图片变成一个本地的BitMap
	public static Bitmap returnBitMap(String url) {
         URL myFileUrl = null;
         Bitmap bitmap = null;
         try {
             myFileUrl = new URL(url);
         } catch (MalformedURLException e) {
             e.printStackTrace();
         }
         try {
             HttpURLConnection conn = (HttpURLConnection) myFileUrl
                     .openConnection();
             conn.setDoInput(true);
             conn.connect();
             InputStream is = conn.getInputStream();
             bitmap = BitmapFactory.decodeStream(is);
             is.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return bitmap;
	 }
}

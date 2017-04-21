package com.jarry.jayvoice.widget;

import java.io.BufferedReader;  
import java.io.ByteArrayOutputStream;
import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;
import java.io.InputStreamReader;  
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;  
import java.util.TreeMap;  
import java.util.regex.Matcher;  
import java.util.regex.Pattern;  

import com.jarry.jayvoice.R;
import com.jarry.jayvoice.bean.LyricObject;
import com.jarry.jayvoice.util.DisplayUtil;
import com.jarry.jayvoice.util.Logger;
import com.jarry.jayvoice.util.StringUtils;
  
import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.util.Log;  
import android.view.MotionEvent;  
import android.view.View;  
  
public class LyricView extends View{  
      
    private static TreeMap<Integer, LyricObject> lrc_map;  
    private float mX;       //屏幕X轴的中点，此值固定，保持歌词在X中间显示  
    private float offsetY;      //歌词在Y轴上的偏移量，此值会根据歌词的滚动变小  
    private static boolean blLrc=false;  
    private float touchY;   //当触摸歌词View时，保存为当前触点的Y轴坐标  
    private float touchX;  
    private boolean blScrollView=false;  
    private int lrcIndex=0; //保存歌词TreeMap的下标  
    private  int SIZEWORD=0;//显示歌词文字的大小值  
    private  int INTERVAL=45;//歌词每行的间隔  
    Paint paint=new Paint();//画笔，用于画不是高亮的歌词  
    Paint paintHL=new Paint();  //画笔，用于画高亮的歌词，即当前唱到这句歌词  
    static Context context;
    int mWidth;
    int mHeight;
      
    public LyricView(Context context){  
        super(context);  
        this.context = context;
        init();  
    }  
      
    public LyricView(Context context, AttributeSet attrs) {  
        super(context, attrs); 
        this.context = context;
        init();  
    }  
      
    /* (non-Javadoc) 
     * @see android.view.View#onDraw(android.graphics.Canvas) 
     */  
    @Override  
    protected void onDraw(Canvas canvas) { 
        if(blLrc){  
            paintHL.setTextSize(SIZEWORD);  
            paint.setTextSize(SIZEWORD);  
            LyricObject temp=lrc_map.get(lrcIndex);  
            if(temp==null||temp.lrc==null)return;
            canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*lrcIndex, paintHL);  
            // 画当前歌词之前的歌词  
            for(int i=lrcIndex-1;i>=0;i--){  
                temp=lrc_map.get(i);  
                if(offsetY+(SIZEWORD+INTERVAL)*i<0){  
                    break;  
                }  
                canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);  
            }  
            // 画当前歌词之后的歌词  
            for(int i=lrcIndex+1;i<lrc_map.size();i++){  
                temp=lrc_map.get(i);  
                if(offsetY+(SIZEWORD+INTERVAL)*i>mHeight-SIZEWORD){  
                    break;  
                }  
                canvas.drawText(temp.lrc, mX, offsetY+(SIZEWORD+INTERVAL)*i, paint);  
            }  
        }else{  
            paint.setTextSize(25);  
            canvas.drawText("找不到歌词", mX, 310, paint);  
        }  
        super.onDraw(canvas);  
    }  
  
    /* (non-Javadoc) 
     * @see android.view.View#onTouchEvent(android.view.MotionEvent) 
     */  
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        // TODO Auto-generated method stub  
        System.out.println("bllll==="+blScrollView);  
        float tt=event.getY();  
        if(!blLrc){  
            //return super.onTouchEvent(event);  
  
            return super.onTouchEvent(event);  
        }  
        switch(event.getAction()){  
        case MotionEvent.ACTION_DOWN:  
            touchX=event.getX();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            touchY=tt-touchY;             
            offsetY=offsetY+touchY;  
            break;  
        case MotionEvent.ACTION_UP:  
            blScrollView=false;  
            break;        
        }  
        touchY=tt;  
        return true;  
    }  
  
    public void init(){  
        lrc_map = new TreeMap<Integer, LyricObject>();  
        offsetY=0;      
        INTERVAL = DisplayUtil.getDpSize(10, context);
        paint=new Paint();  
        paint.setTextAlign(Paint.Align.LEFT);  
        paint.setColor(Color.GRAY);  
        paint.setAntiAlias(true);  
        paint.setDither(true);  
        paint.setAlpha(180);  
          
          
        paintHL=new Paint();  
        paintHL.setTextAlign(Paint.Align.LEFT);  
          
        paintHL.setColor(getResources().getColor(R.color.bluetop));  
        paintHL.setAntiAlias(true);  
        paintHL.setAlpha(255);  
    }  
      
    /** 
     * 根据歌词里面最长的那句来确定歌词字体的大小 
     */  
      
    public void SetTextSize(){  
        if(!blLrc){  
            return;  
        }  
        if(lrc_map.size()<=0){
        	return;
        }
        int max=lrc_map.get(0).lrc.length();  
        for(int i=1;i<lrc_map.size();i++){  
            LyricObject lrcStrLength=lrc_map.get(i);  
            if(lrcStrLength!=null&&lrcStrLength.lrc!=null){
            	if(max<lrcStrLength.lrc.length()){  
                    max=lrcStrLength.lrc.length();  
                }  
            }         
        }  
        Logger.d("SetTextSize-mWidth="+mWidth);
        SIZEWORD=mWidth/max; 
        offsetY=SIZEWORD;
        Logger.d("SetTextSize-SIZEWORD="+SIZEWORD);
      
    }  
      
    protected void onSizeChanged(int w, int h, int oldw, int oldh) { 
        mX = 0;  
        mWidth = w;
        mHeight = h;
        Logger.d("onSizeChanged-mWidth="+mWidth+";mHeight="+mHeight);
        SetTextSize();
        
        super.onSizeChanged(w, h, oldw, oldh);  
    }  
      
    /** 
     *  歌词滚动的速度 
     *  
     * @return 返回歌词滚动的速度 
     */  
    public Float SpeedLrc(){  
        float speed=0;  
        if(offsetY+(SIZEWORD+INTERVAL)*lrcIndex>220){  
            speed=((offsetY+(SIZEWORD+INTERVAL)*lrcIndex-220)/20);  
  
        } else if(offsetY+(SIZEWORD+INTERVAL)*lrcIndex < 120){  
            Log.i("speed", "speed is too fast!!!");  
//            speed = 0;  
            speed=((offsetY+(SIZEWORD+INTERVAL)*lrcIndex-120)/5);  
        }  
//      if(speed<0.2){  
//          speed=0.2f;  
//      }  
        return speed;  
    }  
      
    /** 
     * 按当前的歌曲的播放时间，从歌词里面获得那一句 
     * @param time 当前歌曲的播放时间 
     * @return 返回当前歌词的索引值 
     */  
    public int SelectIndex(int time){  
        if(!blLrc){  
            return 0;  
        }  
        int index=0;  
        for(int i=0;i<lrc_map.size();i++){  
            LyricObject temp=lrc_map.get(i); 
            if(temp.begintime<time){  
                ++index;  
            }else{
            	break;
            }
        }      
        lrcIndex=index-1; 
        if(lrcIndex<0){  
            lrcIndex=0;  
        }  
        return lrcIndex;  
      
    }  
    static TreeMap<Integer, LyricObject> lrc_read =new TreeMap<Integer, LyricObject>();
    /** 
     * 读取歌词文件 
     * @param file 歌词的路径 
     *  
     */  
    public static void read(String file) {  
    	lrc_read.clear();
        String data = "";       
        try {  
          InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open("a.txt"),"utf-8"); 
          blLrc=true;  
          BufferedReader br = new BufferedReader(inputReader);
          int i = 0;
          Pattern pattern = Pattern.compile("\\d{2}");  
          while ((data = br.readLine()) != null) {      
        	  data = data.replace("[","");//将前面的替换成后面的  
              data = data.replace("]","@");  
              String splitdata[] =data.split("@");//分隔  
              if(data.endsWith("@")){  
                  for(int k=0;k<splitdata.length;k++){  
                      String str=splitdata[k];  
                        
                      str = str.replace(":",".");  
                      str = str.replace(".","@");  
                      String timedata[] =str.split("@");  
                      Matcher matcher = pattern.matcher(timedata[0]);  
                      if(timedata.length==3 && matcher.matches()){  
                          int m = Integer.parseInt(timedata[0]);  //分  
                          int s = Integer.parseInt(timedata[1]);  //秒  
                          int ms = Integer.parseInt(timedata[2]); //毫秒  
                          int currTime = (m*60+s)*1000+ms*10;  
                          LyricObject item1= new LyricObject();  
                          item1.begintime = currTime;  
                          item1.lrc       = "";  
                          lrc_read.put(currTime,item1);  
                      }  
                  }  
                    
              } else{  
                  String lrcContenet = splitdata[splitdata.length-1];   
            
                  for (int j=0;j<splitdata.length-1;j++)  
                  {  
                      String tmpstr = splitdata[j];  
                        
                      tmpstr = tmpstr.replace(":",".");  
                      tmpstr = tmpstr.replace(".","@");  
                      String timedata[] =tmpstr.split("@");  
                      Matcher matcher = pattern.matcher(timedata[0]);  
                      if(timedata.length==3 && matcher.matches()){  
                          int m = Integer.parseInt(timedata[0]);  //分  
                          int s = Integer.parseInt(timedata[1]);  //秒  
                          int ms = Integer.parseInt(timedata[2]); //毫秒  
                          int currTime = (m*60+s)*1000+ms*10;  
                          LyricObject item1= new LyricObject();  
                          item1.begintime = currTime;  
                          item1.lrc       = lrcContenet;  
                          lrc_read.put(currTime,item1);// 将currTime当标签  item1当数据 插入TreeMap里  
                          i++;  
                      }  
                  }  
              }  
          }    
        }  
        catch (FileNotFoundException e) {  
        }  
        catch (IOException e) {  
        }              
        data ="";
        doLyric();        
    }   
    
    
    
    public static void readParse(String urlPath)  {
    	lrc_read.clear();
        String data="";
        if(StringUtils.isNull(urlPath)){
        	blLrc=false;
        	return;
        }else{
        	blLrc=true;
        }        
		HttpURLConnection conn = null;
        try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			int len = 0;
			URL url = new URL(urlPath);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10 * 1000); 
			conn.setReadTimeout(10 * 1000); 
			conn.connect();
			InputStream inStream = conn.getInputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(inStream,"GB2312"));    //GB2312 									
			int i = 0;
			Pattern pattern = Pattern.compile("\\d{2}");  
			while((data=br.readLine())!=null){
				data = data.replace("[","");//将前面的替换成后面的  
	              data = data.replace("]","@");  
	              String splitdata[] =data.split("@");//分隔  
	              if(data.endsWith("@")){  
	                  for(int k=0;k<splitdata.length;k++){  
	                      String str=splitdata[k];  
	                        
	                      str = str.replace(":",".");  
	                      str = str.replace(".","@");  
	                      String timedata[] =str.split("@");  
	                      Matcher matcher = pattern.matcher(timedata[0]);  
	                      if(timedata.length==3 && matcher.matches()){  
	                          int m = Integer.parseInt(timedata[0]);  //分  
	                          int s = Integer.parseInt(timedata[1]);  //秒  
	                          int ms = Integer.parseInt(timedata[2]); //毫秒  
	                          int currTime = (m*60+s)*1000+ms*10;  
	                          LyricObject item1= new LyricObject();  
	                          item1.begintime = currTime;  
	                          item1.lrc       = "";  
	                          lrc_read.put(currTime,item1);  
	                      }  
	                  }  
	                    
	              } else{  
	                  String lrcContenet = splitdata[splitdata.length-1];   
	            
	                  for (int j=0;j<splitdata.length-1;j++)  
	                  {  
	                      String tmpstr = splitdata[j];  
	                        
	                      tmpstr = tmpstr.replace(":",".");  
	                      tmpstr = tmpstr.replace(".","@");  
	                      String timedata[] =tmpstr.split("@");  
	                      Matcher matcher = pattern.matcher(timedata[0]);  
	                      if(timedata.length==3 && matcher.matches()){  
	                          int m = Integer.parseInt(timedata[0]);  //分  
	                          int s = Integer.parseInt(timedata[1]);  //秒  
	                          int ms = Integer.parseInt(timedata[2]); //毫秒  
	                          int currTime = (m*60+s)*1000+ms*10;  
	                          LyricObject item1= new LyricObject();  
	                          item1.begintime = currTime;  
	                          item1.lrc       = lrcContenet;  
	                          lrc_read.put(currTime,item1);// 将currTime当标签  item1当数据 插入TreeMap里  
	                          i++;  
	                      }  
	                  }  
	              } 
			}
			br.close();			
		    inStream.close();		    
		    outStream.close();		   	
		    conn.disconnect();		    			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.d("readParse IOException");
			conn.disconnect();
			conn = null;
		}
        data ="";
        doLyric();
	}
    
    private static void doLyric() {
    	/* 
         * 遍历hashmap 计算每句歌词所需要的时间 
        */  
        lrc_map.clear();   
        Logger.d("doLyric-lrc_read.size="+lrc_read.size());
        Iterator<Integer> iterator = lrc_read.keySet().iterator();  
        LyricObject oldval  = null;  
        int i =0;  
        while(iterator.hasNext()) {  
            Object ob =iterator.next();  
              
            LyricObject val = (LyricObject)lrc_read.get(ob);  
              
            if (oldval==null)  
                oldval = val;  
            else  
            {  
                LyricObject item1= new LyricObject();  
                item1  = oldval;  
                item1.timeline = val.begintime-oldval.begintime;  
                lrc_map.put(new Integer(i), item1);  
                i++;  
                oldval = val;  
            }  
            if (!iterator.hasNext()) {  
                lrc_map.put(new Integer(i), val);  
            }               
        }   
	}
      
    /** 
     * @return the blLrc 
     */  
    public static boolean isBlLrc() {  
        return blLrc;  
    }  
  
    /** 
     * @return the offsetY 
     */  
    public float getOffsetY() {  
        return offsetY;  
    }  
  
    /** 
     * @param offsetY the offsetY to set 
     */  
    public void setOffsetY(float offsetY) {  
        this.offsetY = offsetY;  
    }  
  
    /** 
     * @return 返回歌词文字的大小 
     */  
    public int getSIZEWORD() {  
        return SIZEWORD;  
    }  
  
    /** 
     * 设置歌词文字的大小 
     * @param sIZEWORD the sIZEWORD to set 
     */  
    public void setSIZEWORD(int sIZEWORD) {  
        SIZEWORD = sIZEWORD;  
    }  
}  

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\android-tools\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn a.b.c.**
-keep class a.b.c.** {
    *;
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

-keep class com.tencent.mm.sdk.** { *; }
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-dontwarn com.sina.**
-keep class com.sina.** { *; }
-dontwarn com.umeng.**
-keep class com.umeng*.** {*; }
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *; }
-dontwarn com.google.gson.**
-keep class com.google.gson.** { *; }
-dontwarn android.support.v4.**
-keep class android.support.v4.** {*;}

-keepattributes Signature

# Gson specific classes
-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

-ignorewarnings
# 这里根据具体的SDK版本修改
-libraryjars libs/BmobSDK_V3.2.8_0105.jar

-keepattributes Signature
-keep class cn.bmob.v3.** {*;}

# 保证继承自BmobObject、BmobUser类的JavaBean不被混淆
-keep class com.jarry.jayvoice.bean.** {*;}

-keep class com.umeng.message.* {
        public <fields>;
        public <methods>;
}

-keep class com.umeng.message.protobuffer.* {
        public <fields>;
        public <methods>;
}

-keep class com.squareup.wire.* {
        public <fields>;
        public <methods>;
}

-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class com.jarry.jayvoice.R$*{
    public static final int *;
}
-keep class com.baidu.cyberplayer.** {*;}

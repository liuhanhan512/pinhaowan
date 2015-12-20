# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\android-sdk/tools/proguard/proguard-android.txt
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

-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage { *;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class com.hwand.pinhaowanr.utils.NonProguard
-keep class * implements com.hwand.pinhaowanr.utils.NonProguard { *; }


-keep class com.android.volley.** { *; }

-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keepclassmembers class ** {
    public void onEvent*(**);
}



#gson
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.** { *; }


#友盟
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class com.umeng.**
-keep public class com.idea.fifaalarmclock.app.R$*{
    public static final int *;
}
-keep public class com.umeng.fb.ui.ThreadView {
}
-dontwarn com.umeng.**
-dontwarn org.apache.commons.**
-keep public class * extends com.umeng.**
-keep public class * extends com.nostra13.universalimageloader.**
-keep public class * extends com.view.pagerindicator.**
-keep class com.umeng.** {*; }
-keep class com.github.mikephil.charting.**{*; }
#图片加载
#-libraryjars libs/universal-image-loader-1.9.2.jar  #imageLoader的jar包不要混淆
-keep class com.nostra13.universalimageloader.** { *; }              #imageLoader包下所有类及类里面的内容不要混淆
-keep class com.view.pagerindicator.**{ *; }
-keep class com.android.pwel.pwel.model.**{ *; }
-keep public class * extends android.app.Activity{ *; }
-keep public class * extends android.app.Application{ *; }
 -keep public class * extends android.app.Service{ *; }
 -keep public class * extends android.content.BroadcastReceiver{ *; }
 -keep public class * extends android.content.ContentProvider{ *; }
 -keep public class * extends android.app.backup.BackupAgentHelper{ *; }
 -keep public class * extends android.preference.Preference{ *; }
 -keep public class com.android.vending.licensing.ILicensingService{ *; }
 -keep public class * extends com.android.pwel.pwel.util.**{ *; }
 -keep public class * extends com.android.pwel.pwel.volley.toolbox.**{ *; }
 -keep public abstract interface com.asqw.android.Listener{
 public protected <methods>;
}
-keep public class * extends com.github.mikephil.charting.**{ *; }



-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.facebook.**
-dontwarn com.tencent.**


-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}
-keep public class javax.**
-keep public class android.webkit.**

-keep class com.facebook.**
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**


-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

-keep public class [your_pkg].R$*{
    public static final int *;
}

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

-keep class com.umeng.message.local.* {
        public <fields>;
        public <methods>;
}
-keep class org.android.agoo.impl.*{
        public <fields>;
        public <methods>;
}

-keep class org.android.agoo.service.* {*;}

-keep class org.android.spdy.**{*;}

-keep public class com.android.pwel.pwel.R$*{
    public static final int *;
}

#高德相关混淆文件
-dontwarn com.amap.api.**
-dontwarn com.aps.**

#Location
-keep   class com.amap.api.location.**{*;}
-keep   class com.aps.**{*;}

#讯飞语音
-keep class com.iflytek.**{*;}



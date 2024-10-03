package com.pasc.lib.safe.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class SignatureUtils {
    /*********/
//    private static final String md5SignStr = "C2D4EB8ADB0E45FE658C032497EE43C5";
    private final static Handler mHandler = new Handler (Looper.getMainLooper ()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage (msg);
            android.os.Process.killProcess (android.os.Process.myPid ());
            killAllProcess ((Context) msg.obj);
        }
    };

    /**
     *  * MD5加密
     *  * @param byteStr 需要加密的内容
     *  * @return 返回 byteStr的md5值
     *  
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest;
        StringBuffer md5StrBuff = new StringBuffer ();
        try {
            messageDigest = MessageDigest.getInstance ("MD5");
            messageDigest.reset ();
            messageDigest.update (byteStr);
            byte[] byteArray = messageDigest.digest ();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString (0xFF & byteArray[i]).length () == 1) {
                    md5StrBuff.append ("0").append (Integer.toHexString (0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append (Integer.toHexString (0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace ();
        }
        return md5StrBuff.toString ().toUpperCase ();
    }

    /**
     *  * 获取app签名md5值
     *  
     */
    public static String getSignMd5Str(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager ().getPackageInfo (context.getPackageName (), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5 (sign.toByteArray ());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace ();
        }
        return "";
    }

//    public static void checkSign(Context context) {
//        if (!getSignMd5Str(context).equals(md5SignStr) || isEmulator (context)) {
//            Message message = Message.obtain();
//            message.obj = context;
//            mHandler.sendMessageDelayed(message, 1000);
//        }
//    }


    private static void killAllProcess(Context context) {
        final ActivityManager am = (ActivityManager) context.getSystemService (Context.ACTIVITY_SERVICE);
        if (am == null) {
            return;
        }
        List<ActivityManager.RunningAppProcessInfo> appProcessList = am
                .getRunningAppProcesses ();

        if (appProcessList == null) {
            return;
        }
        // NOTE: getRunningAppProcess() ONLY GIVE YOU THE PROCESS OF YOUR OWN PACKAGE IN ANDROID M
        // BUT THAT'S ENOUGH HERE
        for (ActivityManager.RunningAppProcessInfo ai : appProcessList) {
            // KILL OTHER PROCESS OF MINE
            if (ai.uid == android.os.Process.myUid () && ai.pid != android.os.Process.myPid ()) {
                android.os.Process.killProcess (ai.pid);
            }
        }

    }


    public static void checkEmulator(Context context) {
        if (isEmulator (context)) {
            Message message = Message.obtain ();
            message.obj = context;
            mHandler.sendMessageDelayed (message, 1000);
        }
    }

    public static boolean isEmulator(Context mContext) {
        String url = "tel:" + "123456";
        Intent intent = new Intent ();
        intent.setData (Uri.parse (url));
        intent.setAction (Intent.ACTION_DIAL);
        boolean canResolveIntent = intent.resolveActivity (mContext.getPackageManager ()) != null;
        return Build.FINGERPRINT.startsWith ("generic")
                || Build.FINGERPRINT.toLowerCase ().contains ("vbox")
                || Build.FINGERPRINT.toLowerCase ().contains ("test-keys")
                || Build.MODEL.contains ("google_sdk")
                || Build.MODEL.contains ("Emulator")
                || Build.SERIAL.equalsIgnoreCase ("unknown")
                || Build.SERIAL.equalsIgnoreCase ("android")
                || Build.MODEL.contains ("Android SDK built for x86")
                || Build.MANUFACTURER.contains ("Genymotion")
                || (Build.BRAND.startsWith ("generic") && Build.DEVICE.startsWith ("generic"))
                || "google_sdk".equals (Build.PRODUCT)
                || ((TelephonyManager) mContext.getSystemService (Context.TELEPHONY_SERVICE))
                .getNetworkOperatorName ().toLowerCase ().equals ("android")
                || !canResolveIntent;
    }


}

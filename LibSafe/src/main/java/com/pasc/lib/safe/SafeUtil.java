package com.pasc.lib.safe;

import android.app.Application;
import android.content.Context;

import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.pasc.lib.safe.util.SignatureUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yangzijian
 * @date 2018/10/30
 * @des
 * @modify
 **/
public class SafeUtil {

    private static Context sContext;
    public static Context getContext() {
        return sContext;
    }

    public static native boolean checkEnv(Context context);

    public static native void checkDebug();


    public static native String getAppKey(Context context);

    /*** use by native***/
    public static String getMd5() {
        return SignatureUtils.getSignMd5Str (getContext ());
    }
    /*** use by native***/
    public static void checkEm(){
        SignatureUtils.checkEmulator (getContext ());
    }

    private volatile static String key;

    public static synchronized String getPassword() {
        if (key == null) {
            key = getAppKey (getContext ());
        }
        return key;
    }
    public static void init(final Context context) {
        sContext=context;
        System.loadLibrary("pascSafe");
        new AsyncTask<Void, Void, Boolean>() {
            @Override protected Boolean doInBackground(Void... params) {
                checkEnv(context);
                return isDeviceRooted() && !isHooked();
            }

            @Override protected void onPostExecute(Boolean isRooted) {
                if (isRooted) {
                    Toast.makeText(context, R.string.tip_device_rooted, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
    public static boolean isHooked() {
        return detectHook() || detectHook2();
    }

    private static boolean detectHook() {
        boolean hooked = false;

        try {
            Set<String> libraries = new HashSet<>();
            String mapsFilename = "/proc/" + android.os.Process.myPid() + "/maps";
            BufferedReader reader = new BufferedReader(new FileReader(mapsFilename));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith(".so") || line.endsWith(".jar")) {
                    int n = line.lastIndexOf(" ");
                    libraries.add(line.substring(n + 1));
                }
            }
            for (String library : libraries) {
                if (library.contains("com.saurik.substrate")) {
                    hooked = true;
                    Log.wtf("HookDetection", "Substrate shared object found: " + library);
                    break;
                } else if (library.contains("XposedBridge.jar")) {
                    hooked = true;
                    Log.wtf("HookDetection", "Xposed JAR found: " + library);
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            Log.wtf("HookDetection", e.toString());
        }
        return hooked;
    }

    private static boolean detectHook2() {
        try {
            throw new Exception("blah");
        } catch (Exception e) {
            int zygoteInitCallCount = 0;
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                if (stackTraceElement.getClassName().equals("com.android.internal.os.ZygoteInit")) {
                    zygoteInitCallCount++;
                    if (zygoteInitCallCount == 2) {
                        Log.wtf("HookDetection", "Substrate is active on the device.");
                        return true;
                    }
                }
                if (stackTraceElement.getClassName().equals("com.saurik.substrate.MS$2")
                    && stackTraceElement.getMethodName().equals("invoked")) {
                    Log.wtf("HookDetection", "A method on the stack trace has been hooked using Substrate.");
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                    && stackTraceElement.getMethodName().equals("main")) {
                    Log.wtf("HookDetection", "Xposed is active on the device.");
                    return true;
                }
                if (stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")
                    && stackTraceElement.getMethodName().equals("handleHookedMethod")) {
                    Log.wtf("HookDetection", "A method on the stack trace has been hooked using Xposed.");
                    return true;
                }
            }
        }
        return false;
    }
  public static boolean isDeviceRooted() {
    return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
  }

  private static boolean checkRootMethod1() {
    String buildTags = android.os.Build.TAGS;
    return buildTags != null && buildTags.contains("test-keys");
  }

  private static boolean checkRootMethod2() {
    String[] paths = {
        "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su",
        "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
        "/system/bin/failsafe/su", "/data/local/su"
    };
    for (String path : paths) {
      if (new File(path).exists()) {
          return true;
      }
    }
    return false;
  }

  private static boolean checkRootMethod3() {
    java.lang.Process process = null;
    try {
      process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
      BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
      return in.readLine() != null;
    } catch (Throwable t) {
      return false;
    } finally {
      if (process != null) {
          process.destroy();
      }
    }
  }
    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}

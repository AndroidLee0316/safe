#include <jni.h>
#include <string>
#include "pascUtil.h"
#include "Constant.h"
#include "pascSafe.h"
#include "common.h"
#include "jniLoad.h"
//static bool should_check(JNIEnv *env, const char *pn)
//{
//    char buf[100];
//    char *real_pn;
//
//    if (ends_with(pn, ".test"))
//    {
//        substring(buf, pn, 0, strlen(pn) - 3);
//    }
//    else
//    {
//        substring(buf, pn, 0, strlen(pn));
//    }
//
//    real_pn = buf;
//    replace(real_pn, ".", "/");
//    char *clz_name = str_stitching(real_pn, "/BuildConfig");
//
//    jclass bulid_config_clz = env->FindClass(clz_name);
//    jfieldID debug_fid = env->GetStaticFieldID(bulid_config_clz, "DEBUG", "Z");
//    jboolean debug = env->GetStaticBooleanField(bulid_config_clz, debug_fid);
//    env->DeleteLocalRef(bulid_config_clz);
//    return !debug;
//}

extern "C" JNIEXPORT jboolean JNICALL checkEnv(
        JNIEnv *env, jclass type, jobject context) {
    const char *pn;
    jclass context_clz = env->GetObjectClass(context);
    jmethodID pn_mid = env->GetMethodID(context_clz, "getPackageName",
                                        "()Ljava/lang/String;");
    jstring package_name = (jstring) env->CallObjectMethod(context, pn_mid);

    pn = env->GetStringUTFChars(package_name, NULL);
//    if (!should_check(env, pn))
//    {
//        return true;
//    }

//    jstring appSignature = loadSignature(env, context); // 当前 App 的签名
    jstring appSignature = getAppKey(env, type, context); // 当前 App 的签名

    jstring releaseSignature = env->NewStringUTF(APP_SIGNATURE); // 发布时候的签名
    const char *charAppSignature = env->GetStringUTFChars(appSignature, NULL);
    const char *charReleaseSignature = env->GetStringUTFChars(releaseSignature, NULL);
    LOGI("  start cmp  getSignature");
    LOGI(charAppSignature);
    LOGI("  start cmp  getReleaseSignature");
    LOGI(charReleaseSignature);
    jboolean result = JNI_FALSE;
    // 比较是否相等
    if (charAppSignature != NULL && charReleaseSignature != NULL) {
        if (strcmp(charAppSignature, charReleaseSignature) == 0) {
            result = JNI_TRUE;
        }
    }
    env->ReleaseStringUTFChars(appSignature, charAppSignature);
    env->ReleaseStringUTFChars(releaseSignature, charReleaseSignature);
    return result;
}

/**
 * 检查加载该so的应用的签名，与预置的签名是否一致
 */
static jboolean checkSignature(JNIEnv *env) {
    // 调用 getContext 方法得到 Context 对象
    jobject appContext = getContext(env);
    if (appContext != NULL) {
        jboolean signatureValid = checkEnv(
                env, NULL, appContext);
        return signatureValid;
    }
    return JNI_FALSE;
}


extern "C"
JNIEXPORT jstring JNICALL
getAppKey(JNIEnv *env, jclass type, jobject context) {

    jclass jcls = env->FindClass(CLASS_NAME_NATIVE);
    if (jcls != NULL) {
        jmethodID methodID = env->GetStaticMethodID(jcls,
                                                    METHOD_NAME_GETMD5,
                                                    METHOD_SIGNATURE_GETMD5);
        if (methodID != NULL) {
            jobject obj = env->CallStaticObjectMethod(jcls, methodID);
            return static_cast<jstring>(obj);
        }
    }

    jstring appSignature = loadSignature(env, context); // 当前 App 的签名
    return appSignature;

}

void checkDebug(JNIEnv *env, jclass type) {
    if (isAppDebug(env)) {
        LOGI("isDebug true");

    } else {
        LOGI("isDebug false");

    }

}

jint RegisterSafeNativeMethods(JNIEnv *env) {
    const int p_c = 3;
    const JNINativeMethod safeUtilMethods[p_c] =
            {
                    {"checkEnv",   "(Landroid/content/Context;)Z",                  (void *) checkEnv},
                    {"getAppKey",  "(Landroid/content/Context;)Ljava/lang/String;", (void *) getAppKey},
                    {"checkDebug", "()V",                                           (void *) checkDebug}

            };
    int ret = RegisterNativeMethods(env, CLASS_NAME_NATIVE,
                                    safeUtilMethods, p_c);
    LOGI("RegisterNativeMethods ret : %d", ret);
    return 0;
}
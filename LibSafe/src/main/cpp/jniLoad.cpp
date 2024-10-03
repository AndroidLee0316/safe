//
// Created by 杨自鉴 on 2018/11/14.
//
#include "jniLoad.h"
#include "pascUtil.h"
#include "Constant.h"

__attribute__((visibility ("default")))
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void * /*reserved*/) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        RegisterSafeNativeMethods(env);
    }
    //    if (checkSignature(env) != JNI_TRUE) {
//        LOGI("  checkSignature = false ");
    // 检测不通过，返回 -1 就会使 App crash
//        std::abort();
//        std::exit(1);
//        return -1;

//    }
    jclass jcls = env->FindClass(CLASS_NAME_NATIVE);
    if (jcls != NULL) {
        jmethodID methodID = env->GetStaticMethodID(jcls,
                                                    METHOD_NAME_CHECKEM,
                                                    METHOD_SIGNATURE_CHECKEM);
        if (methodID != NULL) {
            env->CallStaticVoidMethod(jcls, methodID);
        }
    }
    if (isAppDebug(env)) {
    } else {
        be_attached_check();
    }

    return JNI_VERSION_1_6;
}

///////////////////////////////////////////////////////////////////////////////
// The VM calls JNI_OnUnload when the native library is unloaded.
//
//extern "C"

/**
 * 加载 so 文件的时候，会触发 OnLoad
 * 检测失败，返回 -1，App 就会 Crash
 */
__attribute__((visibility ("default")))
JNIEXPORT void JNICALL JNI_OnUnload(JavaVM *vm, void * /*reserved*/) {
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) == JNI_OK) {
        UnregisterNativeMethods(env, CLASS_NAME_NATIVE);
    }




}

jint RegisterNativeMethods(JNIEnv *env, const char *className, const JNINativeMethod *methods,
                           int numMethods) {
    jint result = JNI_ERR;
    if (jclass clazz = env->FindClass(className)) {
        result = env->RegisterNatives(clazz, methods, numMethods);
        env->DeleteLocalRef(clazz);
    }
    return result;
}

jint UnregisterNativeMethods(JNIEnv *env, const char *className) {
    jint result = JNI_ERR;
    if (jclass clazz = env->FindClass(className)) {
        result = env->UnregisterNatives(clazz);
        env->DeleteLocalRef(clazz);
    }
    return result;
}
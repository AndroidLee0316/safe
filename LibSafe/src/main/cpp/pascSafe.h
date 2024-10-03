
#ifndef ANDROID_PASCSAFE_HEARDERS_H
#define ANDROID_PASCSAFE_HEARDERS_H

#include <jni.h>

extern "C" JNIEXPORT jboolean JNICALL
checkEnv(
        JNIEnv *env, jclass type, jobject context);
extern "C"
JNIEXPORT jstring JNICALL
getAppKey(JNIEnv *env, jclass type, jobject context);
#endif
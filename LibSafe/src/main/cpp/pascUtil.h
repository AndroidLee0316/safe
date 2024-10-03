
#ifndef ANDROIDSAFEDEMO_PASCUTIL_H
#define ANDROIDSAFEDEMO_PASCUTIL_H

#include <android/log.h>
#include <jni.h>
#include <string>
#include <unistd.h>
#define  LOG_TAG    "safeTag"
#define  LOGFLAG 1

#ifdef LOGFLAG
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#else
#define  LOGI(...)
#endif

void ByteToHexStr(const char *source, char *dest, int sourceLen);

jstring ToMd5(JNIEnv *env, jbyteArray source);

jstring loadSignature(JNIEnv *env, jobject context);

jobject getContext(JNIEnv *env);

bool isAppDebug(JNIEnv *env);

void be_attached_check();

#endif //ANDROIDSAFEDEMO_PASCUTIL_H

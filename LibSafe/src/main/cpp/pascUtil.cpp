#include "pascUtil.h"
#include "Constant.h"


void ByteToHexStr(const char *source, char *dest, int sourceLen) {
    short i;
    char highByte, lowByte;

    for (i = 0; i < sourceLen; i++) {
        highByte = source[i] >> 4;
        lowByte = source[i] & 0x0f;
        highByte += 0x30;

        if (highByte > 0x39) {
            dest[i * 2] = highByte + 0x07;
        } else {
            dest[i * 2] = highByte;
        }

        lowByte += 0x30;
        if (lowByte > 0x39) {
            dest[i * 2 + 1] = lowByte + 0x07;
        } else {
            dest[i * 2 + 1] = lowByte;
        }
    }
}

jstring ToMd5(JNIEnv *env, jbyteArray source) {
    // MessageDigest类
    jclass classMessageDigest = env->FindClass("java/security/MessageDigest");
    // MessageDigest.getInstance()静态方法
    jmethodID midGetInstance = env->GetStaticMethodID(classMessageDigest, "getInstance",
                                                      "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    // MessageDigest object
    jobject objMessageDigest = env->CallStaticObjectMethod(classMessageDigest, midGetInstance,
                                                           env->NewStringUTF("md5"));

    // update方法，这个函数的返回值是void，写V
    jmethodID midUpdate = env->GetMethodID(classMessageDigest, "update", "([B)V");
    env->CallVoidMethod(objMessageDigest, midUpdate, source);

    // digest方法
    jmethodID midDigest = env->GetMethodID(classMessageDigest, "digest", "()[B");
    jbyteArray objArraySign = (jbyteArray) env->CallObjectMethod(objMessageDigest, midDigest);

    jsize intArrayLength = env->GetArrayLength(objArraySign);
    jbyte *byte_array_elements = env->GetByteArrayElements(objArraySign, NULL);
    size_t length = (size_t) intArrayLength * 2 + 1;
    char *char_result = (char *) malloc(length);
    memset(char_result, 0, length);

    // 将byte数组转换成16进制字符串，发现这里不用强转，jbyte和unsigned char应该字节数是一样的
    ByteToHexStr((const char *) byte_array_elements, char_result, intArrayLength);
    // 在末尾补\0
    *(char_result + intArrayLength * 2) = '\0';

    jstring stringResult = env->NewStringUTF(char_result);
    // release
    env->ReleaseByteArrayElements(objArraySign, byte_array_elements, JNI_ABORT);
    // 释放指针使用free
    free(char_result);

    return stringResult;
}

jstring loadSignature(JNIEnv *env, jobject context) {
    // 获得Context类
    jclass cls = env->GetObjectClass(context);
    // 得到getPackageManager方法的ID
    jmethodID mid = env->GetMethodID(cls, "getPackageManager",
                                     "()Landroid/content/pm/PackageManager;");

    // 获得应用包的管理器
    jobject pm = env->CallObjectMethod(context, mid);

    // 得到getPackageName方法的ID
    mid = env->GetMethodID(cls, "getPackageName", "()Ljava/lang/String;");
    // 获得当前应用包名
    jstring packageName = (jstring) env->CallObjectMethod(context, mid);

    // 获得PackageManager类
    cls = env->GetObjectClass(pm);
    // 得到getPackageInfo方法的ID
    mid = env->GetMethodID(cls, "getPackageInfo",
                           "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    // 获得应用包的信息
    jobject packageInfo = env->CallObjectMethod(pm, mid, packageName, 0x40); //GET_SIGNATURES = 64;
    // 获得PackageInfo 类
    cls = env->GetObjectClass(packageInfo);
    // 获得签名数组属性的ID
    jfieldID fid = env->GetFieldID(cls, "signatures", "[Landroid/content/pm/Signature;");
    // 得到签名数组
    jobjectArray signatures = (jobjectArray) env->GetObjectField(packageInfo, fid);
    // 得到签名
    jobject signature = env->GetObjectArrayElement(signatures, 0);

    // 获得Signature类
    cls = env->GetObjectClass(signature);
    // 得到toCharsString方法的ID
    mid = env->GetMethodID(cls, "toByteArray", "()[B");
    // 返回当前应用签名信息
    jbyteArray signatureByteArray = (jbyteArray) env->CallObjectMethod(signature, mid);

    return ToMd5(env, signatureByteArray);
}

void be_attached_check() {
    try {
        const int bufSize = 1024;
        char filename[bufSize];
        char line[bufSize];
        int pid = getpid();
        sprintf(filename, "/proc/%d/status", pid);
        FILE *fd = fopen(filename, "r");
        if (fd != nullptr) {
            while (fgets(line, bufSize, fd)) {
                if (strncmp(line, "TracerPid", 9) == 0) {
                    int statue = atoi(&line[10]);
                    LOGI("%s", line);
                    if (statue != 0) {
                        LOGI("be attached !! kill %d", pid);
                        fclose(fd);
                        int ret = kill(pid, SIGKILL);
                    }
                    break;
                }
            }
            fclose(fd);
        } else {
            LOGI("open %s fail...", filename);
        }
    } catch (...) {

    }
}

jobject getContext(JNIEnv *env) {
    jclass classNativeContextHolder = env->FindClass(CLASS_NAME_NATIVE);
    // 得到 getContext 静态方法
    jmethodID midGetContext = env->GetStaticMethodID(classNativeContextHolder,
                                                     METHOD_NAME_GET_CONTEXT,
                                                     METHOD_SIGNATURE_GETCONTEXT);
    // 调用 getContext 方法得到 Context 对象
    jobject appContext = env->CallStaticObjectMethod(classNativeContextHolder, midGetContext);

    return appContext;

}


bool isAppDebug(JNIEnv *env) {
    try {
        jobject contextObj = getContext(env);
        jclass contextCls = env->GetObjectClass(contextObj);
        jmethodID midGetApplicationInfo = env->GetMethodID(contextCls,
                                                           METHOD_NAME_GETAPPLICATIONINFO,
                                                           METHOD_SIGNATURE_GETAPPLICATIONINFO);
        jobject applicationObj = env->CallObjectMethod(contextObj, midGetApplicationInfo);
        jclass applicationCls = env->GetObjectClass(applicationObj);

        jfieldID fieldFlags = env->GetFieldID(applicationCls, FIELD_APPLICATION_FLAGS, "I");

        jfieldID fieldFlagsDebug = env->GetStaticFieldID(applicationCls,
                                                   FIELD_APPLICATION_FLAG_DEBUGGABLE, "I");

        jint flags = env->GetIntField(applicationObj, fieldFlags);
        jint FLAG_DEBUGGABLE = env->GetStaticIntField(applicationCls, fieldFlagsDebug);

        return (flags & FLAG_DEBUGGABLE) != 0;


    } catch (...) {
        LOGI("isDebug error");

    }

    return false;
}



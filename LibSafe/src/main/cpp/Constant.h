
#ifndef ANDROIDSAFEDEMO_CONSTANT_H
#define ANDROIDSAFEDEMO_CONSTANT_H
const char * const APP_SIGNATURE = "C2D4EB8ADB0E45FE658C032497EE43C5";

const char * const CLASS_NAME_NATIVE = "com/pasc/lib/safe/SafeUtil";

const char * const METHOD_NAME_GET_CONTEXT = "getContext";
const char * const METHOD_SIGNATURE_GETCONTEXT = "()Landroid/content/Context;";

const char * const METHOD_NAME_GETMD5 = "getMd5";
const char * const METHOD_SIGNATURE_GETMD5 = "()Ljava/lang/String;";

const char * const METHOD_NAME_CHECKEM = "checkEm";
const char * const METHOD_SIGNATURE_CHECKEM = "()V";

const char * const CLASS_APPLICATION_NAME="android/content/pm/ApplicationInfo";
const char * const FIELD_APPLICATION_FLAGS="flags";
const char * const FIELD_APPLICATION_FLAG_DEBUGGABLE="FLAG_DEBUGGABLE";
const char * const METHOD_NAME_GETAPPLICATIONINFO = "getApplicationInfo";
const char * const METHOD_SIGNATURE_GETAPPLICATIONINFO = "()Landroid/content/pm/ApplicationInfo;";

#endif //ANDROIDSAFEDEMO_CONSTANT_H

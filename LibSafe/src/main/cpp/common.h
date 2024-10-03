
#ifndef	__COMMON_H__
#define __COMMON_H__

#include <stdbool.h>
#include <sys/types.h>
#include <jni.h>

char *str_stitching(const char *str1, const char *str2);
char *str_stitching3(const char *str1, const char *str2, const char *str3);
void replace(char *s, char *old_char, char *new_char);
bool starts_with(const char *src, const char *prefix);
bool ends_with(const char *src, const char *suffix);
void substring(char *dest, const char *src, int start, int end);
int last_index_of(const char *src, const char *need);
int index_of(const char *src, const char *sub);
bool is_file_existed(const char *file_path);
void select_sleep(long sec, long msec);
inline jstring new_string(JNIEnv *env, char *origin);
int register_native_methods(JNIEnv *env, const char *class_name,
        JNINativeMethod *methods, int num_methods);

#endif

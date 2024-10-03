package com.pasc.lib.safe.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pasc.lib.encryption.symmetric.AESUtil;
import com.pasc.lib.safe.SafeUtil;

import javax.crypto.Cipher;



public class SharedPrefer {
    public void setInt(String key, int value) {
        setString(key,String.valueOf(value));
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key,String.valueOf(defaultValue));
        if (TextUtils.isEmpty(value)){
            return -1;
        }
        return Integer.valueOf(value);
    }

    public void setFloat(String key, float value) {
        setString(key,String.valueOf(value));
    }

    public float getFloat(String key, float defaultValue) {

        String value = getString(key,String.valueOf(defaultValue));
        if (TextUtils.isEmpty(value)){
            return -1.0f;
        }
        return Float.valueOf(value);
    }

    public void setLong(String key, long value) {
        setString(key,String.valueOf(value));
    }

    public long getLong(String key, long defaultValue) {
        String value = getString(key,String.valueOf(defaultValue));
        if (TextUtils.isEmpty(value)){
            return -1L;
        }
        return Long.valueOf(value);

    }

    public void setString(String key, String value) {
        if (preferences != null) {
            String password = SafeUtil.getPassword ();
            String source = AESUtil.aes (value, password, Cipher.ENCRYPT_MODE);
            preferences.edit ().putString (key, source).commit ();
        }
    }

    public String getString(String key, String value) {
        if (preferences != null) {
            String password = SafeUtil.getPassword ();
            String source = preferences.getString (key, value);
            if (source==null || TextUtils.isEmpty (source.trim ())){
                return value;
            }
            return AESUtil.aes (source, password, Cipher.DECRYPT_MODE);
        }
        return null;
    }

    public void setBoolean(String key, boolean value) {
        setString(key,String.valueOf(value));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key,String.valueOf(defaultValue));
        if (TextUtils.isEmpty(value)){
            return false;
        }
        return Boolean.valueOf(value);
    }


    private SharedPreferences preferences;

    private SharedPrefer(Builder builder) {
        preferences = SafeUtil.getContext ().getSharedPreferences (builder.shareName, Context.MODE_PRIVATE);
    }

    public static SharedPrefer newObject(String shareName) {
        return new Builder (shareName).build ();
    }

    public static SharedPrefer newObject() {
        return new Builder ().build ();

    }

    public static class Builder {

        private String shareName = "pasc_preference_name";

        public Builder() {
        }

        public Builder(String shareName) {
            if (shareName != null && TextUtils.isEmpty (shareName.trim ())){
                this.shareName = shareName;
            }
        }

        public SharedPrefer build() {
            return new SharedPrefer (this);
        }
    }


}

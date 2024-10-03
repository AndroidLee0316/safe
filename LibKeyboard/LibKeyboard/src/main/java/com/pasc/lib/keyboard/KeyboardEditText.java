package com.pasc.lib.keyboard;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 功能：自动带有自定义键盘的EditText
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/18
 */
public class KeyboardEditText extends EditText {

    protected Context mContext;

    /**
     * 自定义键盘
     */
    protected KeyboardPopWindow mKeyboardPopWindow;
    /**
     * 是否是安全密码
     */
    protected boolean mIsSafeInput = false;

    /**
     * 输入的密码
     */
    protected StringBuffer mInputPasswordSB = new StringBuffer();

    public KeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        KeyboardView.KeyboardTheme keyboardTheme = KeyboardView.initTheme(context, attrs);
        mIsSafeInput = keyboardTheme.password;
        initKeyboardPopWindow(context,keyboardTheme);
        initView();
    }

    /**
     *初始化键盘
     * @param context
     * @param keyboardTheme
     */
    protected void initKeyboardPopWindow(Context context, KeyboardView.KeyboardTheme keyboardTheme){
        mKeyboardPopWindow = KeyboardPopWindow.bindEdit((Activity) context,this, keyboardTheme);
    }

    /**
     * 初始化view
     */
    protected void initView(){
        if (mIsSafeInput){
            //安全输入
            mKeyboardPopWindow.setCallBack(new KeyboardView.CallBack() {
                @Override
                public void onKeyAdd(String key) {
                    mInputPasswordSB.append(key);
                    key = "*";
                    mKeyboardPopWindow.insertText(key);
                }

                @Override
                public void onKeyDelete() {
                    if (mInputPasswordSB.length()>0){
                        int index = mKeyboardPopWindow.deleteText();
                        if (index>=0 && mInputPasswordSB.length()>index){
                            mInputPasswordSB.deleteCharAt(index);
                        }
                    }
                }
            });
            mKeyboardPopWindow.setPassword(mIsSafeInput);
        }
        if (TextUtils.isEmpty(mInputPasswordSB.toString()) && !TextUtils.isEmpty(getText().toString())){
            mInputPasswordSB.append(getText().toString());
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        getContext().getSharedPreferences("sp", Context.MODE_PRIVATE).edit().putString("mInputPasswordSB",mInputPasswordSB.toString()).commit();
        return super.onSaveInstanceState();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        mInputPasswordSB = new StringBuffer();
        mInputPasswordSB.append(getContext().getSharedPreferences("sp", Context.MODE_PRIVATE).getString("mInputPasswordSB",""));
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    /**
     * 设置是否是安全输入
     * @param isSafeInput
     */
    protected void setIsSafeInput(boolean isSafeInput) {
        this.mIsSafeInput = isSafeInput;
    }

    /**
     * 获取安全环境下真正的输入字符串
     * @return
     */
    public String getRealSafeInputText(){
        return mInputPasswordSB.toString();
    }

    /**
     * 设置键盘主题
     * @param keyBoardTheme
     */
    public void setKeyBoardTheme(KeyboardView.KeyboardTheme keyBoardTheme){
        mKeyboardPopWindow.setKeyBoardTheme(keyBoardTheme);
    }

}

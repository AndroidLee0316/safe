package com.pasc.lib.keyboard;

import android.content.Context;
import android.util.AttributeSet;


/**
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify 密码虚拟密码键盘
 **/
public class PwdKeyboardView extends KeyboardView {

    public static final int sm3Mode=0;
    public static final int sha256Mode=1;
    public static final int simpleMode=2;

    private int mode=sm3Mode;

    public void setMode(int mode) {
        this.mode = mode;
    }

    private int maxLength =6;

    /**
     * 密码输入监控
     */
    private PwdKeyBoardListener listener;

    public PwdKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCallBack(new CallBack() {
            @Override
            public void onKeyAdd(String key) {
                addPassWord(key);
            }

            @Override
            public void onKeyDelete() {
                removePassWord();
            }
        });
    }


    public PwdKeyboardView(Context context, AttributeSet attrs, KeyboardTheme keyboardTheme) {
        super(context, attrs, keyboardTheme);
        setCallBack(new CallBack() {
            @Override
            public void onKeyAdd(String key) {
                addPassWord(key);
            }

            @Override
            public void onKeyDelete() {
                removePassWord();
            }
        });
    }

    /**
     * 设置最大可输入字符数量
     * @param maxLength
     */
    public void setMaxLength(int maxLength){
        this.maxLength = maxLength;
    }

    public boolean isSimplePassword(){
        return nativeIsPasswordSimple(mode);
    }

    /**
     * 获取输入后的密码
     * @return
     */
    public String getPassword(){
        return nativeGetPassword(mode);
    }

    public void setPwdBoardListener(PwdKeyBoardListener listener) {
        this.listener = listener;
    }

    private void addPassWord(final String pwd) {
        if (currentLength()>=maxLength){
            return;
        }
        nativeAddPassWord (pwd,mode);
        if (listener != null) {
            listener.addPassWord (currentLength (),maxLength);
            if (currentLength()==maxLength){
                listener.onPasswordInputFinish(nativeGetPassword(mode), nativeIsPasswordSimple(mode));
            }
        }
    }

    private void removePassWord() {
        nativeRemovePassWord ();
        if (listener != null) {
            listener.removePassWord (currentLength (),maxLength);
        }
    }

    public void clearPassWord() {
        nativeClearPassWord ();
        if (listener != null) {
            listener.clearPassWord (currentLength (),maxLength);
        }
    }

    {
        System.loadLibrary ("pascSafeKeyboard");
        clearPassWord ();
    }

    private native void nativeAddPassWord(String pwd,int mode);

    private native void nativeRemovePassWord();

    private native void nativeClearPassWord();

    private native int currentLength();

    private native String nativeGetPassword(int mode);

    private native boolean nativeIsPasswordSimple(int mode);


}

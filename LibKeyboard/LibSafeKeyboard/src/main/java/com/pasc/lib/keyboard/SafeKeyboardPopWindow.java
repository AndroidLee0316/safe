package com.pasc.lib.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/19
 */
public class SafeKeyboardPopWindow extends KeyboardPopWindow {

    private OnFinishListener mFinishListener;


    public SafeKeyboardPopWindow(Activity context) {
        super(context);
    }


    public SafeKeyboardPopWindow(Activity context, EditText editText, int keyType) {
        super(context,editText,keyType);
    }

    public SafeKeyboardPopWindow(Activity context, EditText editText, KeyboardView.KeyboardTheme theme) {
        super(context,editText,theme);
    }


    @Override
    protected void initKeyboardView(Context context, KeyboardView.KeyboardTheme keyboardTheme) {
        mKeyboardView = new PwdKeyboardView(context, null, keyboardTheme);
        ((PwdKeyboardView)mKeyboardView).setPwdBoardListener(new PwdKeyBoardListener() {
            @Override
            public void onPasswordInputFinish(String password, boolean isInvalidatePwd) {
                if (mFinishListener != null){
                    mFinishListener.onPasswordInputFinish(password,isInvalidatePwd);
                }
            }

            @Override
            public void addPassWord(int currentIndex, int totalLength) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i<currentIndex; i++){
                    sb.append("*");
                }
                mBindText.setText(sb.toString());
                mBindText.setSelection(mBindText.getText().length());
            }

            @Override
            public void removePassWord(int currentIndex, int totalLength) {

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i<currentIndex; i++){
                    sb.append("*");
                }
                mBindText.setText(sb.toString());
                mBindText.setSelection(mBindText.getText().length());
            }

            @Override
            public void clearPassWord(int currentIndex, int totalLength) {

                mBindText.setText("");
                mBindText.setSelection(mBindText.getText().length());
            }
        });

        mKeyboardView.setRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 静态绑定虚拟键盘
     * @param activity
     * @param editText
     * @return
     */
    public static SafeKeyboardPopWindow bindEdit(Activity activity, EditText editText){
        SafeKeyboardPopWindow keyBoardPopWindow = new SafeKeyboardPopWindow(activity, editText, KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL);
        return keyBoardPopWindow;
    }

    /**
     * 静态绑定虚拟键盘
     * @param activity
     * @param editText
     * @return
     */
    public static SafeKeyboardPopWindow bindEdit(Activity activity, EditText editText, int keyboardType){
        SafeKeyboardPopWindow keyBoardPopWindow = new SafeKeyboardPopWindow(activity, editText,keyboardType);
        return keyBoardPopWindow;
    }

    /**
     * 静态绑定虚拟键盘
     * @param activity
     * @param editText
     * @return
     */
    public static SafeKeyboardPopWindow bindEdit(Activity activity, EditText editText, KeyboardView.KeyboardTheme keyboardTheme){
        SafeKeyboardPopWindow keyBoardPopWindow = new SafeKeyboardPopWindow(activity, editText,keyboardTheme);
        return keyBoardPopWindow;
    }

    /**
     * 设置最大可输入长度
     * @param length
     */
    public void setMaxLength(int length){
        ((PwdKeyboardView)mKeyboardView).setMaxLength(length);
    }

    /**
     * 是否是简单密码
     * @return
     */
    public boolean isSimplePassword(){
        return ((PwdKeyboardView)mKeyboardView).isSimplePassword();
    }

    /**
     * 获取输入的加密数据
     * @return
     */
    public String getSafeInputString() {
        return ((PwdKeyboardView)mKeyboardView).getPassword();
    }

    /**
     * 设置输入完毕监听器
     * @param mFinishListener
     */
    public void setOnFinishListener(OnFinishListener mFinishListener) {
        this.mFinishListener = mFinishListener;
    }

    /**
     * 输入完毕监听器
     */
    public static interface OnFinishListener {
        void onPasswordInputFinish(String password, boolean isInvalidatePwd);
    }
}

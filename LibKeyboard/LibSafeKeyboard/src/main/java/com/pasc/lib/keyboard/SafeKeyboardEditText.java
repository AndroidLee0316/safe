package com.pasc.lib.keyboard;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;


/**
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify 密码虚拟密码键盘
 **/
public class SafeKeyboardEditText extends KeyboardEditText {

    public SafeKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIsSafeInput(true);
    }

    @Override
    protected void initKeyboardPopWindow(Context context, KeyboardView.KeyboardTheme keyboardTheme) {
        mKeyboardPopWindow = SafeKeyboardPopWindow.bindEdit((Activity) context,this,keyboardTheme);
    }

    @Override
    protected void initView() {
        //这里为空，是为了不让父类 KeyboardEditText 的initView执行
    }

    @Override
    public String getRealSafeInputText() {
        return ((SafeKeyboardPopWindow)mKeyboardPopWindow).getSafeInputString();

    }
}

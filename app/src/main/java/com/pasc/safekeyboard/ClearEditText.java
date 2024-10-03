//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.pasc.safekeyboard;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class ClearEditText extends AppCompatEditText implements OnFocusChangeListener, TextWatcher {
    public Drawable mClearDrawable;
    private Context context;
    private ClearEditText.EditTextChangeListener editTextChangeListener;
    private int focusCount;
    private boolean isNeedResetText;
    private boolean hasFocus;
    private int inputType;
    private ClearEditText.IconDismissListener iconDismissListener;
    private ClearEditText.InnerFocusChangeListener innerFocusChangeListener;

    public ClearEditText(Context context) {
        this(context, (AttributeSet)null);
    }

    public ClearEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 16842862);
    }

    public void setInnerFocusChangeListener(ClearEditText.InnerFocusChangeListener innerFocusChangeListener) {
        this.innerFocusChangeListener = innerFocusChangeListener;
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.focusCount = 0;
        this.isNeedResetText = false;
        this.innerFocusChangeListener = ClearEditText.InnerFocusChangeListener.NONE;
        this.init();
    }

    private void init() {
        this.mClearDrawable = this.getCompoundDrawables()[2];
        if (this.mClearDrawable == null) {
            this.mClearDrawable = this.getResources().getDrawable(R.drawable.pasc_keyboard_logo);
        }

        this.mClearDrawable.setBounds(0, 0, this.mClearDrawable.getIntrinsicWidth(), this.mClearDrawable.getIntrinsicHeight());
        this.setClearIconVisible(false);
        this.setOnFocusChangeListener(this);
        this.addTextChangedListener(this);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.mClearDrawable != null && event.getAction() == 1) {
            int eventX = (int)event.getRawX();
            int eventY = (int)event.getRawY();
            Rect rect = new Rect();
            this.getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (eventX <= rect.right && eventX >= rect.left) {
                this.setText("");
                if (this.iconDismissListener != null) {
                    this.iconDismissListener.onIconClick();
                }
            }
        }

        return super.onTouchEvent(event);
    }

    private void setClearIconVisible(boolean visible) {
        Drawable right = visible ? this.mClearDrawable : null;
        this.setCompoundDrawables(this.getCompoundDrawables()[0], this.getCompoundDrawables()[1], right, this.getCompoundDrawables()[3]);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            this.setClearIconVisible(this.getText().length() > 0);
            ++this.focusCount;
            this.inputType = this.getInputType();
        } else {
            if (TextUtils.isEmpty(this.getText())) {
                this.focusCount = 0;
            }

            this.setClearIconVisible(false);
        }

        this.innerFocusChangeListener.onInnerFocusChange(v, hasFocus);
    }

    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (this.hasFocus) {
            this.setClearIconVisible(text.length() > 0);
        }

    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (this.focusCount > 1 && this.isPasswordInputType() && count > 0) {
            this.focusCount = 1;
            this.setText("");
        }

    }

    public void afterTextChanged(Editable s) {
        if (this.editTextChangeListener != null) {
            this.editTextChangeListener.afterChange(s.toString());
        }

    }

    public void setShakeAnimation() {
        this.startAnimation(shakeAnimation(5));
    }

    public static Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0.0F, 10.0F, 0.0F, 0.0F);
        translateAnimation.setInterpolator(new CycleInterpolator((float)counts));
        translateAnimation.setDuration(1000L);
        return translateAnimation;
    }

    public void setEditTextChangeListener(ClearEditText.EditTextChangeListener editTextChangeListener) {
        this.editTextChangeListener = editTextChangeListener;
    }

    public void setIconDismissListener(ClearEditText.IconDismissListener iconDismissListener) {
        this.iconDismissListener = iconDismissListener;
    }

    private boolean isPasswordInputType() {
        return this.inputType == 16 || this.inputType == 129 || this.inputType == 144 || this.inputType == 224;
    }

    public interface EditTextChangeListener {
        void afterChange(String var1);
    }

    public interface IconDismissListener {
        void onIconClick();
    }

    public interface InnerFocusChangeListener {
        ClearEditText.InnerFocusChangeListener NONE = new ClearEditText.InnerFocusChangeListener() {
            public void onInnerFocusChange(View v, boolean hasFocus) {
            }
        };

        void onInnerFocusChange(View var1, boolean var2);
    }
}

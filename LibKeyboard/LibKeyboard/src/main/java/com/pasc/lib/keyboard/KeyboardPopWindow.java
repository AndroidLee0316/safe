package com.pasc.lib.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.lang.reflect.Method;

/**
 * 功能：
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/17
 */
public class KeyboardPopWindow extends PopupWindow{

    private Activity mActivity;

    /**
     * 键盘view
     */
    protected KeyboardView mKeyboardView;
    /**
     * 绑定的activity    */
    protected EditText mBindText;


    public KeyboardPopWindow(Activity context) {
        super(context);
        init(context,null, null);
    }

    public KeyboardPopWindow(Activity context, EditText editText, int keyType) {
        super(context);
        init(context,editText, keyType);
    }

    public KeyboardPopWindow(Activity context, EditText editText, KeyboardView.KeyboardTheme theme) {
        super(context);
        init(context,editText, theme);
    }

    /**
     * 初始化
     * @param context
     * @param editText
     */
    private void init(Activity context, EditText editText, int keyBoardType){
        KeyboardView.KeyboardTheme theme = new KeyboardView.KeyboardTheme();
        theme.keyboardType = keyBoardType;
        init(context, editText, theme);
    }

    /**
     * 初始化
     * @param context
     * @param editText
     * @param keyboardTheme
     */
    private void init(Activity context, EditText editText, KeyboardView.KeyboardTheme keyboardTheme){
        mActivity = context;

        initKeyboardView(context,keyboardTheme);
        // 设置SelectPicPopupWindow的View
        this.setContentView (mKeyboardView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth (ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight (ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置SelectPicPopupWindow弹出窗体可点击(会导致点击窗体外的按钮点击事件不能透传)
//        this.setFocusable (true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle (R.style.pay_pwd_pop_ainm);
        this.setOutsideTouchable(true);

        setBackgroundDrawable(new ColorDrawable(0));

        //绑定外面传入的edittext
        if (editText != null){
            mBindText = editText;
            bindEditText();
        }

    }

    /**
     * 初始化键盘
     * @param context
     * @param keyboardTheme
     */
    protected void initKeyboardView(Context context, KeyboardView.KeyboardTheme keyboardTheme){
        mActivity = (Activity) context;
        mKeyboardView = new KeyboardView(context, null, keyboardTheme);
        mKeyboardView.setCallBack(new KeyboardView.CallBack() {
            @Override
            public void onKeyAdd(String key) {
                insertText(key);
            }

            @Override
            public void onKeyDelete() {
                deleteText();
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
     * 绑定EditText
     */
    private void bindEditText(){

        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            mBindText.setInputType (InputType.TYPE_NULL);
        } else {
            if (mActivity != null) {
                mActivity.getWindow ().setSoftInputMode (
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod ("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible (true);
                setShowSoftInputOnFocus.invoke (mBindText, false);
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }

        //设置监听器
        mBindText.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show ();
            }
        });

        //设置焦点处理:注释掉，因为如果传入的edittext 已经设置了 onFocusChangeListener,会覆盖
//        mBindText.setOnFocusChangeListener (new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    KeyboardUtils.hideInputForce (mActivity);
//                    mBindText.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            show();
//                        }
//                    },500);
//                }else {
//                    dismiss ();
//                }
//            }
//        });

    }
    /**
     * 设置自定义的callback
     */
    public void setCallBack(KeyboardView.CallBack callBack){
        mKeyboardView.setCallBack(callBack);
    }


    /**
     * 显示键盘popwindow
     */
    public void show(){
        showAtLocation (mActivity.getWindow().getDecorView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 设置键盘主题
     * @param theme
     */
    public void setKeyBoardTheme(KeyboardView.KeyboardTheme theme){
        mKeyboardView.setKeyBoardTheme(theme);
    }

    /**
     * 设置键盘类型
     * @param keyBoardType
     */
    public void setKeyBoardType(int keyBoardType){
        mKeyboardView.setKeyBoardType(keyBoardType);
    }

    /**
     * 设置是否是密码键盘
     * @param password
     */
    public void setPassword(boolean password){
        mKeyboardView.setPassword(password);
    }

    /**
     * 添加字符
     * @param text
     */
    protected void insertText(String text){
        //获取光标所在位置
        int index = mBindText.getSelectionStart();
        //获取EditText的文字
        Editable edit = mBindText.getEditableText();
        if (index < 0 || index >= edit.length() ){
            //追加
            edit.append(text);
        }else{
            //光标所在位置插入文字
            edit.insert(index,text);
        }
    }

    /**
     * 删除字符
     */
    protected int deleteText (){

        int index = mBindText.getSelectionEnd();

        Editable edit = mBindText.getEditableText();

        if (edit.length() == 0 || index <= 0){
            return -1;
        }else if (index >= edit.length() ){
            int deleteIndex = edit.length()-1;
            edit.delete(deleteIndex, edit.length());
            return deleteIndex;
        }else{
            int deleteIndex = index -1;
            edit.delete(index -1 ,index);
            return deleteIndex;
        }

    }


    /**
     * 静态绑定虚拟键盘
     * @param activity
     * @param editText
     * @return
     */
    public static KeyboardPopWindow bindEdit(Activity activity, EditText editText){
        KeyboardPopWindow keyBoardPopWindow = new KeyboardPopWindow(activity, editText, KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL);
        return keyBoardPopWindow;
    }

    /**
     * 静态绑定虚拟键盘
     * @param activity
     * @param editText
     * @return
     */
    public static KeyboardPopWindow bindEdit(Activity activity, EditText editText, int keyboardType){
        KeyboardPopWindow keyBoardPopWindow = new KeyboardPopWindow(activity, editText,keyboardType);
        return keyBoardPopWindow;
    }

    /**
     * 静态绑定虚拟键盘
     * @param activity
     * @param editText
     * @return
     */
    public static KeyboardPopWindow bindEdit(Activity activity, EditText editText, KeyboardView.KeyboardTheme keyboardTheme){
        KeyboardPopWindow keyBoardPopWindow = new KeyboardPopWindow(activity, editText,keyboardTheme);
        return keyBoardPopWindow;
    }

    public void onEditFocesChange(View v, boolean hasFocus){
        if (hasFocus){
            KeyboardUtils.hideInputForce (mActivity);
            mBindText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    show();
                }
            },500);
        }else {
            dismiss ();
        }
    }

}

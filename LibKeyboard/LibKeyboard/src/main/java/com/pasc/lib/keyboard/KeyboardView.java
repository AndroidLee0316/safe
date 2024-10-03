package com.pasc.lib.keyboard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;

/**
 * 虚拟键盘
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify 密码虚拟键盘
 **/
public class KeyboardView extends FrameLayout {

    /**
     * 上下文
     */
    private Context context;

    protected LinearLayout mRootLL;
    //toolbar
    protected RelativeLayout mToolbarRL;
    //title
    protected LinearLayout mTitleLL;
    protected TextView mTitleTV;
    protected ImageView mTitleIV;

    //right
    protected LinearLayout mRightLL;
    protected TextView mRightTV;
    protected ImageView mRightIV;

    //分割线
    protected View mSpliteView;

    //普通字符view
    private KeyboardNormalView mKeyBoardNormalView;
    //特殊字符view
    private KeyboardSpecialView mKeyBoardSpecialView;
    //数字键盘view
    private KeyboardNumView mKeyBoardNumView;

    //显示隐藏动画效果
    private Animation enterAnim;
    private Animation exitAnim;

    protected View titleRvRoot;
    protected TextView titleRv;
    protected ImageView hideRv;

    public void showTitleView(boolean show){
        if (titleRvRoot!=null){
            titleRvRoot.setVisibility (show?View.VISIBLE:View.GONE);
        }
    }

    public void setTitleViewBg(int color){
        if (titleRvRoot!=null){
            titleRvRoot.setBackgroundColor (color);
        }
    }

    public void setTitle(String title){
        if (titleRv!=null){
            titleRv.setText (title);
        }
    }

    public void showHideArrow(boolean show){
        if (hideRv!=null){
            hideRv.setVisibility (show?View.VISIBLE:View.GONE);
        }
    }

    public void setHideImageRes(int icon){
        if (hideRv!=null){
            hideRv.setImageResource (icon);
        }
    }

    /**
     * 按键回调
     */
    private CallBack mCallBack;

    /**
     * 键盘主题
     */
    private KeyboardTheme mKeyboardTheme;

    public KeyboardView(Context context) {
        this (context, null);
    }


    public KeyboardView(Context context, AttributeSet attrs, KeyboardTheme theme) {
        this (context, null);
        if (theme != null){
            KeyboardTheme newTheme = new KeyboardTheme();
            if (theme.keyboardTheme == KeyboardTheme.THEME_WHITE){
                newTheme = initWhiteThemeKeyboard(context);
            }else if (theme.keyboardTheme == KeyboardTheme.THEME_BLACK){
                newTheme = initBlackThemeKeyboard(context);
            }
            newTheme.mergeTheme(theme);
            mKeyboardTheme.mergeTheme(newTheme);
            initViewByTheme();
        }
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        super (context, attrs);
        this.context = context;

        //初始化主题
        mKeyboardTheme = initTheme(context, attrs);
        //初始化显示隐藏动画
        initAnim ();
        //初始化view
        View view = View.inflate (context, R.layout.pasc_keyboard, null);
        initView(view);
        addView (view);
        //根据主题信息更新view
        initViewByTheme();
        //初始化监听
        initListener();
        //启动显示动画
        post (new Runnable () {
            @Override
            public void run() {
                startAnimation (enterAnim);
            }
        });
    }

    /**
     * 初始化主题
     */
    public static KeyboardTheme initTheme(Context context, AttributeSet attrs){

        KeyboardTheme keyboardTheme = new KeyboardTheme();

        TypedArray array = context.obtainStyledAttributes (attrs, R.styleable.KeyboardTheme);

        keyboardTheme.keyboardTheme = array.getInt (R.styleable.KeyboardTheme_keyboardTheme, KeyboardTheme.THEME_WHITE);
        if (keyboardTheme.keyboardTheme == KeyboardTheme.THEME_WHITE){
            keyboardTheme = initWhiteThemeKeyboard(context);
        }else if (keyboardTheme.keyboardTheme == KeyboardTheme.THEME_BLACK){
            keyboardTheme = initBlackThemeKeyboard(context);
        }

        keyboardTheme.password = array.getBoolean(R.styleable.KeyboardTheme_kb_password,false);

        keyboardTheme.keyboardType = array.getInt (R.styleable.KeyboardTheme_keyboardType, KeyboardBaseView.KeyboardNumberTheme.TYPE_NUMBER);

        if (array.getDrawable(R.styleable.KeyboardTheme_keyboardBG) != null){
            keyboardTheme.keyboardBG = array.getDrawable(R.styleable.KeyboardTheme_keyboardBG);
        }

        keyboardTheme.keyboardToolbarTheme.toolbarHeight = array.getDimensionPixelOffset (R.styleable.KeyboardTheme_kb_toolbarHeight, KeyboardTheme.UN_SET_DIMEN);
        if (array.getDrawable (R.styleable.KeyboardTheme_kb_toolbarBG) != null){
            keyboardTheme.keyboardToolbarTheme.toolbarBG = array.getDrawable (R.styleable.KeyboardTheme_kb_toolbarBG);
        }
        keyboardTheme.keyboardToolbarTheme.toolbarPaddingLeftRight = array.getDimensionPixelOffset (R.styleable.KeyboardTheme_kb_toolbarPaddingLeftRight, KeyboardTheme.UN_SET_DIMEN);

        keyboardTheme.keyboardToolbarTheme.titleIcon = array.getDrawable (R.styleable.KeyboardTheme_kb_titleIcon);
        keyboardTheme.keyboardToolbarTheme.titleText = array.getString (R.styleable.KeyboardTheme_kb_titleText);
        if (array.getColorStateList (R.styleable.KeyboardTheme_kb_titleColor)!=null){
            keyboardTheme.keyboardToolbarTheme.titleColor = array.getColorStateList (R.styleable.KeyboardTheme_kb_titleColor);
        }
        if (array.getColorStateList (R.styleable.KeyboardTheme_kb_splitColor)!=null){
            keyboardTheme.keyboardToolbarTheme.splitColor = array.getColorStateList (R.styleable.KeyboardTheme_kb_splitColor);
        }
        keyboardTheme.keyboardToolbarTheme.titleSize = array.getDimensionPixelSize (R.styleable.KeyboardTheme_kb_titleSize, KeyboardTheme.UN_SET_DIMEN);
        keyboardTheme.keyboardToolbarTheme.titleToLeft = array.getBoolean(R.styleable.KeyboardTheme_kb_titleToLeft,false);

        keyboardTheme.keyboardToolbarTheme.rightIcon = array.getDrawable (R.styleable.KeyboardTheme_kb_rightIcon);
        keyboardTheme.keyboardToolbarTheme.rightText = array.getString (R.styleable.KeyboardTheme_kb_rightText);
        if (array.getColorStateList (R.styleable.KeyboardTheme_kb_rightColor) != null){
            keyboardTheme.keyboardToolbarTheme.rightColor = array.getColorStateList (R.styleable.KeyboardTheme_kb_rightColor);
        }

        keyboardTheme.keyboardToolbarTheme.rightSize = array.getDimensionPixelSize (R.styleable.KeyboardTheme_kb_rightSize, KeyboardTheme.UN_SET_DIMEN);

        if (array.getDrawable (R.styleable.KeyboardTheme_keyNumberItemBG) != null){
            keyboardTheme.keyboardNumberTheme.keyNumberItemBG = array.getDrawable (R.styleable.KeyboardTheme_keyNumberItemBG);
        }
        if (array.getDrawable (R.styleable.KeyboardTheme_keyNumberDeleteIcon) != null){
            keyboardTheme.keyboardNumberTheme.keyNumberDeleteIcon = array.getDrawable (R.styleable.KeyboardTheme_keyNumberDeleteIcon);
        }

        keyboardTheme.keyboardNumberTheme.keyNumberRandom = array.getBoolean (R.styleable.KeyboardTheme_keyNumberRandom,false);

        if (array.getDrawable (R.styleable.KeyboardTheme_keyNormalItemBG) != null){
            keyboardTheme.keyboardNormalTheme.keyNormalItemBG = array.getDrawable (R.styleable.KeyboardTheme_keyNormalItemBG);
        }

        if (array.getDrawable (R.styleable.KeyboardTheme_keyNormalDeleteIcon) != null){
            keyboardTheme.keyboardNormalTheme.keyNormalDeleteIcon = array.getDrawable (R.styleable.KeyboardTheme_keyNormalDeleteIcon);
        }
        if (array.getDrawable (R.styleable.KeyboardTheme_keyNormalShiftIcon) != null){
            keyboardTheme.keyboardNormalTheme.keyNormalShiftIcon = array.getDrawable (R.styleable.KeyboardTheme_keyNormalShiftIcon);
        }
        if (array.getDrawable (R.styleable.KeyboardTheme_keyNormalSpecialBG) != null){
            keyboardTheme.keyboardNormalTheme.keyNormalSpecialBG = array.getDrawable (R.styleable.KeyboardTheme_keyNormalSpecialBG);
        }

        if (array.getColorStateList (R.styleable.KeyboardTheme_kb_itemTextColor) != null){
            keyboardTheme.keyboardNumberTheme.keyNumberItemTextColor = array.getColorStateList (R.styleable.KeyboardTheme_kb_itemTextColor);
            keyboardTheme.keyboardNormalTheme.keyNormalItemTextColor = array.getColorStateList (R.styleable.KeyboardTheme_kb_itemTextColor);
        }

        array.recycle ();
        return keyboardTheme;

    }

    /**
     * 设置键盘主题
     * @param theme
     */
    public void setKeyBoardTheme(KeyboardTheme theme){
        if (mKeyboardTheme != null){
            mKeyboardTheme.mergeTheme(theme);
            //根据主题信息更新view
            initViewByTheme();
        }
    }

    /**
     * 设置键盘类型
     * @param keyBoardType
     */
    public void setKeyBoardType(int keyBoardType){
        if (mKeyboardTheme != null){
            mKeyboardTheme.keyboardType = keyBoardType;
            //根据主题信息更新view
            initViewByTheme();
        }
    }

    /**
     * 设置键盘是否是密码
     * @param password
     */
    public void setPassword(boolean password){
        if (mKeyboardTheme != null){
            mKeyboardTheme.password = password;
            //根据主题信息更新view
            initViewByTheme();
        }
    }

    /**
     * 更新键盘页面信息
     */
    private void initViewByTheme(){
        if (mKeyboardTheme == null){
            return;
        }
        //键盘背景
        if (mKeyboardTheme.keyboardBG != null){
            mRootLL.setBackground(mKeyboardTheme.keyboardBG);
        }

        boolean showToolbar = false;
        //键盘toolbar
        if (mKeyboardTheme.keyboardToolbarTheme.toolbarHeight != KeyboardTheme.UN_SET_DIMEN){
            ViewGroup.LayoutParams layoutParams = mToolbarRL.getLayoutParams();
            layoutParams.height = mKeyboardTheme.keyboardToolbarTheme.toolbarHeight;
            mToolbarRL.setLayoutParams(layoutParams);
            showToolbar = true;
        }
        if (mKeyboardTheme.keyboardToolbarTheme.toolbarBG != null){
            mToolbarRL.setBackground(mKeyboardTheme.keyboardToolbarTheme.toolbarBG);
        }
        if (mKeyboardTheme.keyboardToolbarTheme.toolbarPaddingLeftRight != KeyboardTheme.UN_SET_DIMEN){
            mTitleLL.setPadding(mKeyboardTheme.keyboardToolbarTheme.toolbarPaddingLeftRight,0,mKeyboardTheme.keyboardToolbarTheme.toolbarPaddingLeftRight,0);
            mRightLL.setPadding(mKeyboardTheme.keyboardToolbarTheme.toolbarPaddingLeftRight,0,mKeyboardTheme.keyboardToolbarTheme.toolbarPaddingLeftRight,0);
        }

        //键盘title
        if (mKeyboardTheme.keyboardToolbarTheme.titleIcon != null){
            mTitleIV.setImageDrawable(mKeyboardTheme.keyboardToolbarTheme.titleIcon);
            mTitleLL.setVisibility(VISIBLE);
            showToolbar = true;
        }
        if (!TextUtils.isEmpty(mKeyboardTheme.keyboardToolbarTheme.titleText)){
            mTitleTV.setText(mKeyboardTheme.keyboardToolbarTheme.titleText);
            mTitleLL.setVisibility(VISIBLE);
            showToolbar = true;
        }
        if (mKeyboardTheme.keyboardToolbarTheme.titleColor != null){
            mTitleTV.setTextColor(mKeyboardTheme.keyboardToolbarTheme.titleColor);
        }

        if (mKeyboardTheme.keyboardToolbarTheme.titleSize != KeyboardTheme.UN_SET_DIMEN){
            mTitleTV.setTextSize(mKeyboardTheme.keyboardToolbarTheme.titleSize);
        }
        if (mKeyboardTheme.keyboardToolbarTheme.titleToLeft){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mTitleLL.getLayoutParams();
            layoutParams.addRule(ALIGN_PARENT_LEFT);
            mTitleLL.setLayoutParams(layoutParams);
        }

        //键盘right
        if (mKeyboardTheme.keyboardToolbarTheme.rightIcon != null){
            mRightIV.setImageDrawable(mKeyboardTheme.keyboardToolbarTheme.rightIcon);
            mRightLL.setVisibility(VISIBLE);
            showToolbar = true;
        }
        if (!TextUtils.isEmpty(mKeyboardTheme.keyboardToolbarTheme.rightText)){
            mRightTV.setText(mKeyboardTheme.keyboardToolbarTheme.rightText);
            mRightLL.setVisibility(VISIBLE);
            showToolbar = true;
        }
        if (mKeyboardTheme.keyboardToolbarTheme.rightColor != null){
            mRightTV.setTextColor(mKeyboardTheme.keyboardToolbarTheme.rightColor);
        }
        if (mKeyboardTheme.keyboardToolbarTheme.rightSize != KeyboardTheme.UN_SET_DIMEN){
            mRightTV.setTextSize(mKeyboardTheme.keyboardToolbarTheme.rightSize);
        }
        if (mKeyboardTheme.keyboardToolbarTheme.splitColor != null){
            mSpliteView.setBackgroundColor(mKeyboardTheme.keyboardToolbarTheme.splitColor.getDefaultColor());
        }
        if (showToolbar){
            mToolbarRL.setVisibility(VISIBLE);
        }else {
            mToolbarRL.setVisibility(GONE);
        }

        mKeyBoardNormalView.updateTheme(mKeyboardTheme.keyboardNormalTheme);
        mKeyBoardSpecialView.updateTheme(mKeyboardTheme.keyboardNormalTheme);

        mKeyboardTheme.keyboardNumberTheme.keyboardType = mKeyboardTheme.keyboardType;
        mKeyBoardNumView.updateKeybordTheme(mKeyboardTheme.keyboardNumberTheme);

        if (mKeyboardTheme.keyboardType == KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL || mKeyboardTheme.keyboardType == KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL_UPPER){//普通字符键盘
            mKeyBoardNormalView.setVisibility(VISIBLE);
            mKeyBoardSpecialView.setVisibility(GONE);
            mKeyBoardNumView.setVisibility(GONE);
            if (mKeyboardTheme.keyboardType == KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL){
                mKeyBoardNormalView.shif(false);
            }else {
                mKeyBoardNormalView.shif(true);
            }
        }else if (mKeyboardTheme.keyboardType == KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAR_SPECIAL){//特殊字符键盘
            mKeyBoardSpecialView.setVisibility(VISIBLE);
            mKeyBoardNormalView.setVisibility(GONE);
            mKeyBoardNumView.setVisibility(GONE);
        }else {//剩下的就当全是数字键盘类型

            mKeyBoardNumView.setVisibility(VISIBLE);
            mKeyBoardNormalView.setVisibility(GONE);
            mKeyBoardSpecialView.setVisibility(GONE);
        }

    }

    /**
     * 初始化view
     * @param view
     */
    private void initView(View view){

        titleRvRoot=view.findViewById (R.id.pasc_keyword_title_rv);
        titleRv=view.findViewById (R.id.pasc_keyword_title_tv);
        hideRv=view.findViewById (R.id.pasc_keyword_hide_rv);

        if (hideRv!=null){
            hideRv.setOnClickListener (new OnClickListener () {
                @Override
                public void onClick(View v) {
                    hide ();
                }
            });
        }

        mRootLL = view.findViewById(R.id.pasc_keyboard_root);
        mToolbarRL = view.findViewById(R.id.pasc_keyboard_toolbar);

        mTitleLL = view.findViewById(R.id.pasc_keyboard_title_ll);
        mTitleIV = view.findViewById(R.id.pasc_keyboard_title_iv);
        mTitleTV = view.findViewById(R.id.pasc_keyboard_title_tv);

        mRightLL = view.findViewById(R.id.pasc_keyboard_right_ll);
        mRightIV = view.findViewById(R.id.pasc_keyboard_right_iv);
        mRightTV = view.findViewById(R.id.pasc_keyboard_right_tv);

        mSpliteView = view.findViewById(R.id.pasc_keyboard_split);

        mKeyBoardNumView = view.findViewById(R.id.pasc_keyboard_kb_num_view);
        mKeyBoardNormalView = view.findViewById(R.id.pasc_keyboard_kb_normal_view);
        mKeyBoardSpecialView = view.findViewById(R.id.pasc_keyboard_kb_special_view);


    }

    /**
     * 初始化监听事件
     */
    protected void initListener(){

        mRightLL.setOnClickListener (new OnClickListener () {
            @Override
            public void onClick(View v) {
                startAnimation (exitAnim);
                setVisibility (View.INVISIBLE);
            }
        });

        mKeyBoardNormalView.setKeyClickCallBack(new KeyboardNormalView.KeyClickCallBack() {
            @Override
            public void onKeyAdd(String key) {
                if (mCallBack != null){
                    mCallBack.onKeyAdd(key);
                }
            }

            @Override
            public void onKeyDelete() {
                if (mCallBack != null){
                    mCallBack.onKeyDelete();
                }
            }

            @Override
            public void onKeyShowNumKeyboard() {
                mKeyBoardNumView.setKeyboardType(KeyboardNumView.KeyboardNumberTheme.TYPE_CHAR);
                mKeyBoardNumView.setVisibility(VISIBLE);
                mKeyBoardNormalView.setVisibility(GONE);
                mKeyBoardSpecialView.setVisibility(GONE);
            }

            @Override
            public void onKeyShowSpecialKeyboard() {
                mKeyBoardSpecialView.setVisibility(VISIBLE);
                mKeyBoardNormalView.setVisibility(GONE);
                mKeyBoardNumView.setVisibility(GONE);
            }
        });

        mKeyBoardSpecialView.setKeyClickCallBack(new KeyboardSpecialView.KeyClickCallBack() {
            @Override
            public void onKeyAdd(String key) {
                if (mCallBack != null){
                    mCallBack.onKeyAdd(key);
                }
            }

            @Override
            public void onKeyDelete() {
                if (mCallBack != null){
                    mCallBack.onKeyDelete();
                }
            }

            @Override
            public void onKeyShowNumKeyboard() {
                mKeyBoardNumView.setKeyboardType(KeyboardNumView.KeyboardNumberTheme.TYPE_CHAR);
                mKeyBoardNumView.setVisibility(VISIBLE);
                mKeyBoardSpecialView.setVisibility(GONE);
                mKeyBoardNormalView.setVisibility(GONE);
            }

            @Override
            public void onKeyShowNormalKeyboard() {
                mKeyBoardNormalView.setVisibility(VISIBLE);
                mKeyBoardSpecialView.setVisibility(GONE);
                mKeyBoardNumView.setVisibility(GONE);
            }
        });

        mKeyBoardNumView.setCallBack(new KeyboardNumView.CallBack() {
            @Override
            public void onKeyAdd(String key) {
                if (mCallBack != null){
                    mCallBack.onKeyAdd(key);
                }
            }

            @Override
            public void onKeyDelete() {
                if (mCallBack != null){
                    mCallBack.onKeyDelete();
                }
            }
        });

        mKeyBoardNumView.setCallBackChange(new KeyboardNumView.CallBackChange() {
            @Override
            public void onShowChar() {
                mKeyBoardNormalView.setVisibility(VISIBLE);
                mKeyBoardNumView.setVisibility(GONE);
                mKeyBoardSpecialView.setVisibility(GONE);
            }
        });

    }

    /**
     * 设置按键回调
     * @param mCallBack
     */
    public void setCallBack(CallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 设置title的点击事件
     * @param listener
     */
    public void setTitleOnclickListener(OnClickListener listener){
        mTitleLL.setOnClickListener(listener);
    }

    /**
     * 设置right的点击事件
     * @param listener
     */
    public void setRightOnclickListener(OnClickListener listener){
        mRightLL.setOnClickListener(listener);
    }

    /**
     * 切换显示状态（显示 - 隐藏  or  隐藏 - 显示）
     */
    public void switchShowOrHide() {
        if (getVisibility () == View.INVISIBLE) {
            show ();
        } else {
            hide ();
        }
    }

    /**
     * 显示键盘
     */
    public void show() {
        if (getVisibility () == View.INVISIBLE) {
            setFocusable (true);
            setFocusableInTouchMode (true);
            startAnimation (enterAnim);
            setVisibility (View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hide() {
        if (getVisibility () == View.VISIBLE) {
            startAnimation (exitAnim);
            setVisibility (View.INVISIBLE);
        }
    }

    /**
     * 数字键盘显示、关闭动画
     */
    private void initAnim() {
        enterAnim = AnimationUtils.loadAnimation (getContext (), R.anim.pasc_keyborad_push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation (getContext (), R.anim.pasc_keyborad_push_bottom_out);
    }

    /**
     * 初始化黑色主题键盘
     */
    private static KeyboardTheme initBlackThemeKeyboard(Context context){

        KeyboardTheme keyboardTheme = new KeyboardTheme();

        keyboardTheme.keyboardBG = context.getResources().getDrawable(R.color.pasc_keyboard_bg_theme_black);

        keyboardTheme.keyboardToolbarTheme.toolbarBG = context.getResources().getDrawable(R.color.pasc_keyboard_bg_theme_black);
        keyboardTheme.keyboardToolbarTheme.titleColor = ColorStateList.valueOf(context.getResources().getColor(R.color.white));

        keyboardTheme.keyboardToolbarTheme.rightColor = ColorStateList.valueOf(context.getResources().getColor(R.color.white));

        keyboardTheme.keyboardNormalTheme.keyNormalItemTextColor = ColorStateList.valueOf(context.getResources().getColor(R.color.white));
        keyboardTheme.keyboardNormalTheme.keyNormalDeleteIcon = context.getResources().getDrawable(R.drawable.pasc_selector_key_del_black);
        keyboardTheme.keyboardNormalTheme.keyNormalShiftIcon = context.getResources().getDrawable(R.drawable.pasc_icon_capital_normal_black);
        keyboardTheme.keyboardNormalTheme.keyNormalShiftUpperIcon = context.getResources().getDrawable(R.drawable.pasc_icon_capital_press_black);
        keyboardTheme.keyboardNormalTheme.keyNormalItemBG = context.getResources().getDrawable(R.drawable.pasc_selector_keyboard_btn_black);
        keyboardTheme.keyboardNormalTheme.keyNormalSpecialBG = context.getResources().getDrawable(R.drawable.pasc_selector_key_special_black);

        keyboardTheme.keyboardNumberTheme.keyNumberItemTextColor = ColorStateList.valueOf(context.getResources().getColor(R.color.white));
        keyboardTheme.keyboardNumberTheme.keyNumberDeleteIcon = context.getResources().getDrawable(R.drawable.pasc_selector_key_del_black);
        keyboardTheme.keyboardNumberTheme.keyNumberItemBG = context.getResources().getDrawable(R.drawable.pasc_selector_keyboard_btn_num_black);

        return keyboardTheme;
    }

    /**
     * 初始化白色主题键盘
     */
    private static KeyboardTheme initWhiteThemeKeyboard(Context context){
        KeyboardTheme keyboardTheme = new KeyboardTheme();
//        keyboardTheme.keyboardToolbarTheme.titleColor = ColorStateList.valueOf(context.getResources().getColor(R.color.col_key_text));
//        keyboardTheme.keyboardToolbarTheme.rightColor = ColorStateList.valueOf(context.getResources().getColor(R.color.col_key_text));
//        keyboardTheme.keyboardBG = context.getResources().getDrawable(R.color.pasc_keyborad_bg);
//
//        keyboardTheme.keyboardNormalTheme.keyNormalItemTextColor = ColorStateList.valueOf(context.getResources().getColor(R.color.col_key_text));
//        keyboardTheme.keyboardNormalTheme.keyNormalDeleteIcon = context.getResources().getDrawable(R.drawable.selector_key_del);
//        keyboardTheme.keyboardNormalTheme.keyNormalShiftIcon = context.getResources().getDrawable(R.drawable.icon_capital_normal_white);
//        keyboardTheme.keyboardNormalTheme.keyNormalItemBG = context.getResources().getDrawable(R.drawable.selector_keyboard_btn);
//        keyboardTheme.keyboardNormalTheme.keyNormalSpecialBG = context.getResources().getDrawable(R.drawable.selector_key_special);
//
//        keyboardTheme.keyboardNumberTheme.keyNumberItemTextColor = ColorStateList.valueOf(context.getResources().getColor(R.color.col_key_text));
//        keyboardTheme.keyboardNumberTheme.keyNumberDeleteIcon = context.getResources().getDrawable(R.drawable.selector_key_del);

        return keyboardTheme;
    }


    /**
     * 按键点击回调
     */
    public static interface CallBack{
        /**
         * 添加一个字符
         * @param key
         */
        public void onKeyAdd(String key);

        /**
         * 删除按钮
         */
        public void onKeyDelete();
    }


    /**
     * 键盘主题属性集合
     */
    public static class KeyboardTheme extends KeyboardBaseView.KeyboardBaseTheme{

        public KeyboardTheme() {
            keyboardToolbarTheme = new KeyboardBaseView.KeyboardToolbarTheme();
            keyboardNormalTheme = new KeyboardBaseView.KeyboardNormalTheme();
            keyboardNumberTheme = new KeyboardBaseView.KeyboardNumberTheme();
        }

        /**
         * 白色主题
         */
        public final static int THEME_WHITE = 0;
        /**
         * 黑色主题
         */
        public final static int THEME_BLACK = 1;


        public static final int DEFAULT_KEYBORD_TYPE = KeyboardBaseView.KeyboardNumberTheme.TYPE_NUMBER;

        public static final int DEFAULT_KEYBORD_THEME = THEME_WHITE;

        //键盘类型
        public int keyboardType = DEFAULT_KEYBORD_TYPE;
        //键盘主题
        public int keyboardTheme = DEFAULT_KEYBORD_THEME;
        //键盘背景
        public Drawable keyboardBG = null;
        //是否是密码键盘
        public boolean password = false;

        public KeyboardBaseView.KeyboardToolbarTheme keyboardToolbarTheme;

        public KeyboardBaseView.KeyboardNormalTheme keyboardNormalTheme;

        public KeyboardBaseView.KeyboardNumberTheme keyboardNumberTheme;

        public KeyboardTheme mergeTheme(KeyboardTheme inKeyboardTheme){

            if (inKeyboardTheme == null){
                return this;
            }
            if (inKeyboardTheme.keyboardType != DEFAULT_KEYBORD_TYPE){
                this.keyboardType = inKeyboardTheme.keyboardType;
            }
            if (inKeyboardTheme.keyboardTheme != DEFAULT_KEYBORD_THEME){
                this.keyboardTheme = inKeyboardTheme.keyboardTheme;
            }
            if (inKeyboardTheme.keyboardBG != null){
                this.keyboardBG = inKeyboardTheme.keyboardBG;
            }
            if (inKeyboardTheme.password){
                this.password = inKeyboardTheme.password;
            }
            this.keyboardToolbarTheme = this.keyboardToolbarTheme.merge(inKeyboardTheme.keyboardToolbarTheme);
            this.keyboardNormalTheme = this.keyboardNormalTheme.merge(inKeyboardTheme.keyboardNormalTheme);
            this.keyboardNumberTheme = this.keyboardNumberTheme.merge(inKeyboardTheme.keyboardNumberTheme);
            return this;
        }


    }



}

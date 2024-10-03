package com.pasc.lib.keyboard;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 功能：普通字符键盘VIEW
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/25
 */
public class KeyboardNormalView extends KeyboardBaseView {

    private Context mContext;
    /**
     * 普通英文按键列表
     */
    private List<String> mNormalKeyList ;

    //宽度
    private int viewWidth = 0;
    //item间隔
    private int itemSpace = 0;
    //item高度
    private int itemHeight = 0;
    //item宽度
    private int itemWidth = 0;
    //普通英文键盘容器
    private FrameLayout mNormalContainerFL;
    //上档键
    private LinearLayout mNormalShiftLL;
    //上档键
    private ImageView mNormalShiftIV;
    //删除键盘
    private LinearLayout mNormalDeleteLL;
    //删除键
    private ImageView mNormalDeleteIV;
    //上档键单击监听
    private OnClickListener mNormalShiftOnClickListener;
    //删除单击监听
    private OnClickListener mNormalDeleteOnClickListener;
    //是否显示大写字符键盘
    private boolean isShiftUpper = false;
    //普通键盘主题
    private KeyboardNormalTheme mKeyboardNormalTheme;

    //按键点击回调
    private KeyClickCallBack mCallBack;

    //第一行字符数目，用来计算按键的宽度以及间距
    private static final int LINE_NORMAL_ONE_KEY_SIZE = 10;

    //切换到数字键盘按键
    private static final String KEY_NUM = "123";
    //删除按钮
    private static final String KEY_DEL = "del";
    //上档键
    private static final String KEY_SHIFT = "shift";
    //切换到特殊字符键盘按键
    private static final String KEY_SPECIAL = "#+=";
    //空格键：英文
    private static final String KEY_SPACE_EN = "Space";
    //空格键：中文
    private static final String KEY_SPACE_CN = "空格";


    public KeyboardNormalView(Context context) {
        super(context,null);
    }

    public KeyboardNormalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initListener();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        viewWidth = wm.getDefaultDisplay().getWidth();
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 初始化字符键盘数据
     */
    private void initData(){
        mNormalKeyList = new ArrayList<>();
        //第一行
        mNormalKeyList.add("q");
        mNormalKeyList.add("w");
        mNormalKeyList.add("e");
        mNormalKeyList.add("r");
        mNormalKeyList.add("t");
        mNormalKeyList.add("y");
        mNormalKeyList.add("u");
        mNormalKeyList.add("i");
        mNormalKeyList.add("o");
        mNormalKeyList.add("p");

        //第二行
        mNormalKeyList.add("a");
        mNormalKeyList.add("s");
        mNormalKeyList.add("d");
        mNormalKeyList.add("f");
        mNormalKeyList.add("g");
        mNormalKeyList.add("h");
        mNormalKeyList.add("j");
        mNormalKeyList.add("k");
        mNormalKeyList.add("l");

        //第二行
        mNormalKeyList.add(KEY_SHIFT);
        mNormalKeyList.add("z");
        mNormalKeyList.add("x");
        mNormalKeyList.add("c");
        mNormalKeyList.add("v");
        mNormalKeyList.add("b");
        mNormalKeyList.add("n");
        mNormalKeyList.add("m");
        mNormalKeyList.add(KEY_DEL);

        //第三行
        mNormalKeyList.add(KEY_NUM);
        if(Locale.getDefault().equals(Locale.SIMPLIFIED_CHINESE) ||Locale.getDefault().equals(Locale.TRADITIONAL_CHINESE) || Locale.getDefault().equals(Locale.CHINESE) || Locale.getDefault().equals(Locale.CHINA) ){
            mNormalKeyList.add(KEY_SPACE_CN);
        }else {
            mNormalKeyList.add(KEY_SPACE_EN);
        }
        mNormalKeyList.add(KEY_SPECIAL);

    }

    /**
     * 初始化view
     */
    private void initView(){
        removeAllViews();

        itemSpace = (int) mContext.getResources().getDimension(R.dimen.pasc_keyboard_normal_item_spacing);
        itemHeight =  (int) mContext.getResources().getDimension(R.dimen.pasc_keyboard_normal_item_height);
        itemWidth = (viewWidth - (LINE_NORMAL_ONE_KEY_SIZE + 1) * itemSpace)/LINE_NORMAL_ONE_KEY_SIZE;

        initNormalKeys();
    }

    /**
     * 初始化普通字符按键
     */
    private void initNormalKeys(){

        mNormalContainerFL = new FrameLayout(mContext);
        int keySize = mNormalKeyList.size();
        int pointX = 0;
        int pointY = 0;
        for (int i = 0; i < keySize; i++){
            if (i < 10){//第一行
                TextView itemTV = getTextView(mNormalKeyList.get(i));
                initNormalKeyValue(itemTV);

                pointX = i * itemWidth + (i+1) * itemSpace;
                LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = itemSpace;
                mNormalContainerFL.addView(itemTV,lp);

            }else if (i<19){//第二行
                pointX = itemWidth/2 + itemSpace/2 + (i%10 * itemWidth + (i%10 + 1) * itemSpace);
                pointY = itemHeight + itemSpace*2;

                TextView itemTV = getTextView(mNormalKeyList.get(i));
                initNormalKeyValue(itemTV);
                LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = pointY;
                mNormalContainerFL.addView(itemTV,lp);
            }else if (i<28){//第三行
                pointY = itemHeight * 2 +itemSpace * 3;
                if (i == 19){//上档键
                    mNormalShiftLL = new LinearLayout(mContext);
                    LayoutParams lp = new LayoutParams(itemWidth+itemWidth/2 + itemSpace/2, itemHeight);
                    lp.leftMargin = itemSpace;
                    lp.topMargin = pointY;
                    mNormalShiftLL.setBackgroundResource(R.drawable.pasc_selector_key_special);
                    mNormalShiftLL.setOnClickListener(mNormalShiftOnClickListener);

                    mNormalShiftIV = new ImageView(mContext);
                    mNormalShiftIV.setImageResource(R.drawable.pasc_icon_capital_normal_white);
                    mNormalShiftIV.setOnClickListener(mNormalShiftOnClickListener);

                    mNormalShiftLL.setGravity(Gravity.CENTER);
                    mNormalShiftLL.addView(mNormalShiftIV);

                    mNormalContainerFL.addView(mNormalShiftLL,lp);
                }else if (i == 27){//删除键
                    mNormalDeleteLL = new LinearLayout(mContext);
                    LayoutParams lp = new LayoutParams(itemWidth + itemWidth/2 + itemSpace/2 , itemHeight);
                    lp.leftMargin = pointX + itemWidth + itemSpace;
                    lp.topMargin = pointY;
                    mNormalDeleteLL.setBackgroundResource(R.drawable.pasc_selector_key_special);
                    mNormalDeleteLL.setOnClickListener(mNormalDeleteOnClickListener);

                    mNormalDeleteIV = new ImageView(mContext);
                    mNormalDeleteIV.setImageResource(R.drawable.pasc_selector_key_del);
                    mNormalDeleteIV.setOnClickListener(mNormalDeleteOnClickListener);

                    mNormalDeleteLL.setGravity(Gravity.CENTER);
                    mNormalDeleteLL.addView(mNormalDeleteIV);

                    mNormalContainerFL.addView(mNormalDeleteLL,lp);
                }else {//其他普通按键
                    pointX = itemWidth+itemWidth/2+itemSpace + itemSpace/2 + i%20 * itemWidth + (i%20 + 1) * itemSpace;

                    TextView itemTV = getTextView(mNormalKeyList.get(i));
                    initNormalKeyValue(itemTV);
                    LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                    lp.leftMargin = pointX;
                    lp.topMargin = pointY;
                    mNormalContainerFL.addView(itemTV,lp);
                }

            }else if (i == 28){//第四行：数字键

                pointX = itemSpace;
                pointY = itemHeight*3 + itemSpace*4;

                TextView itemTV = getTextView(mNormalKeyList.get(i));
                initSpecialKeyValue(itemTV);
                LayoutParams lp = new LayoutParams(itemWidth*2 + itemSpace, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = pointY;
                lp.bottomMargin = itemSpace;
                mNormalContainerFL.addView(itemTV,lp);

            }else if (i == 29){//第四行：空格键

                pointX = itemWidth*2 + itemSpace*3;
                pointY = itemHeight*3 + itemSpace*4;

                TextView itemTV = getTextView(mNormalKeyList.get(i));
                initSpaceKeyValue(itemTV);
                LayoutParams lp = new LayoutParams(itemWidth*6 + itemSpace*5, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = pointY;
                mNormalContainerFL.addView(itemTV,lp);

            }else if (i == 30){//第四行：数字键特殊键

                pointX = itemWidth*8 + itemSpace*9;
                pointY = itemHeight*3 + itemSpace*4;

                TextView itemTV = getTextView(mNormalKeyList.get(i));
                initSpecialKeyValue(itemTV);
                LayoutParams lp = new LayoutParams(itemWidth*2 + itemSpace, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = pointY;
                mNormalContainerFL.addView(itemTV,lp);

            }
        }

        LayoutParams lp = new LayoutParams(viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mNormalContainerFL, lp);
    }

    /**
     * 初始化监听
     */
    private void initListener(){
        mNormalShiftOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShiftUpper){
                    isShiftUpper = false;
                    shiftLowerCase();
                }else {
                    isShiftUpper = true;
                    shiftUpperCase();
                }

            }
        };

        mNormalDeleteOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null){
                    mCallBack.onKeyDelete();
                }
            }
        };
    }


    /**
     * 切换大小写
     * @param isUpper
     */
    public void shif(boolean isUpper){
        isShiftUpper = isUpper;
        if (isShiftUpper){
            shiftUpperCase();
        }else {
            shiftLowerCase();
        }
    }

    /**
     * 切换键盘小写
     */
    private void shiftLowerCase(){

        for (int i = 0 ; i < mNormalContainerFL.getChildCount(); i++){
            if (mNormalContainerFL.getChildAt(i) instanceof TextView){
                String text = ((TextView) mNormalContainerFL.getChildAt(i)).getText().toString();
                ((TextView) mNormalContainerFL.getChildAt(i)).setText(text.toLowerCase());
            }
        }

        if (mKeyboardNormalTheme != null && mKeyboardNormalTheme.keyNormalShiftIcon != null){
            mNormalShiftIV.setImageDrawable(mKeyboardNormalTheme.keyNormalShiftIcon);
        }else {
            mNormalShiftIV.setImageResource(R.drawable.pasc_icon_capital_normal_white);
        }

    }

    /**
     * 切换键盘大写
     */
    private void shiftUpperCase(){
        for (int i = 0 ; i < mNormalContainerFL.getChildCount(); i++){
            if (mNormalContainerFL.getChildAt(i) instanceof TextView){
                String text = ((TextView) mNormalContainerFL.getChildAt(i)).getText().toString();
                ((TextView) mNormalContainerFL.getChildAt(i)).setText(text.toUpperCase());
            }
        }

        if (mKeyboardNormalTheme != null && mKeyboardNormalTheme.keyNormalShiftUpperIcon != null){
            mNormalShiftIV.setImageDrawable(mKeyboardNormalTheme.keyNormalShiftUpperIcon);
        }else {
            mNormalShiftIV.setImageResource(R.drawable.pasc_icon_capital_press_white);
        }
    }

    /**
     * 统一获取text
     * @param text
     * @return
     */
    private TextView getTextView(String text){
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setTextColor(getResources().getColor(R.color.black_55));
        textView.setTypeface(Typeface.SANS_SERIF);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null){
                    if (KEY_NUM.equals(text)){
                        mCallBack.onKeyShowNumKeyboard();
                    }else if (KEY_SPECIAL.equals(text)){
                        mCallBack.onKeyShowSpecialKeyboard();
                    }else if (KEY_SPACE_EN.equals(text) || KEY_SPACE_CN.equals(text)){
                        mCallBack.onKeyAdd(" ");
                    }else {
                        mCallBack.onKeyAdd(textView.getText().toString());
                    }
                }
            }
        });
        return textView;
    }


    /**
     * 初始化普通的按键
     * @param itemTV
     */
    private void initNormalKeyValue(TextView itemTV){
        itemTV.setGravity(Gravity.CENTER);
        itemTV.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.pasc_keyboard_key_normal_size));
        itemTV.setBackgroundResource(R.drawable.pasc_selector_keyboard_btn);
    }

    /**
     * 初始化特殊的按键
     * @param itemTV
     */
    private void initSpecialKeyValue(TextView itemTV){
        itemTV.setGravity(Gravity.CENTER);
        itemTV.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.pasc_keyboard_key_special_size));
        itemTV.setBackgroundResource(R.drawable.pasc_selector_key_special);
    }

    /**
     * 初始化空格键
     * @param itemTV
     */
    private void initSpaceKeyValue(TextView itemTV){
        itemTV.setGravity(Gravity.CENTER);
        itemTV.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.pasc_keyboard_key_special_size));
        itemTV.setBackgroundResource(R.drawable.pasc_selector_keyboard_btn);
    }

    /**
     * 刷新主题
     * @param theme
     */
    public void updateTheme(KeyboardNormalTheme theme){
        mKeyboardNormalTheme = theme;
        if (mNormalContainerFL != null && mKeyboardNormalTheme != null){
            //key循环
            for (int i = 0 ; i < mNormalContainerFL.getChildCount(); i++){
                if (mNormalContainerFL.getChildAt(i) instanceof TextView){
                    if (mKeyboardNormalTheme.keyNormalItemTextColor != null){
                        ((TextView) mNormalContainerFL.getChildAt(i)).setTextColor(mKeyboardNormalTheme.keyNormalItemTextColor);
                    }
                    if ((i == 28 || i == 30) && mKeyboardNormalTheme.keyNormalSpecialBG != null){
                        //special key
                        mNormalContainerFL.getChildAt(i).setBackground(mKeyboardNormalTheme.keyNormalSpecialBG.getConstantState().newDrawable());
                    }else if (mKeyboardNormalTheme.keyNormalItemBG != null){
                        //normal key
                        mNormalContainerFL.getChildAt(i).setBackground(mKeyboardNormalTheme.keyNormalItemBG.getConstantState().newDrawable());
                    }
                }
            }
            //shift key
            if (mNormalShiftLL != null && mKeyboardNormalTheme.keyNormalSpecialBG != null){
                mNormalShiftLL.setBackground(mKeyboardNormalTheme.keyNormalSpecialBG.getConstantState().newDrawable());
            }
            //del key
            if (mNormalDeleteLL != null && mKeyboardNormalTheme.keyNormalSpecialBG != null){
                mNormalDeleteLL.setBackground(mKeyboardNormalTheme.keyNormalSpecialBG.getConstantState().newDrawable());
            }

            if (mNormalShiftIV != null){
                if (isShiftUpper){
                    //shift key up
                    if (mKeyboardNormalTheme.keyNormalShiftUpperIcon != null){
                        mNormalShiftIV.setImageDrawable(mKeyboardNormalTheme.keyNormalShiftUpperIcon);
                    }
                }else {
                    //shift key normal
                    if (mKeyboardNormalTheme.keyNormalShiftIcon != null){
                        mNormalShiftIV.setImageDrawable(mKeyboardNormalTheme.keyNormalShiftIcon);
                    }
                }
            }

            if (mNormalDeleteIV != null && mKeyboardNormalTheme.keyNormalDeleteIcon != null){
                mNormalDeleteIV.setImageDrawable(mKeyboardNormalTheme.keyNormalDeleteIcon);
            }

        }

    }

    /**
     * 设置按键回调监听
     * @param mCallBack
     */
    public void setKeyClickCallBack(KeyClickCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }


    /**
     * 按键点击回调
     */
    public static interface KeyClickCallBack{
        /**
         * 添加一个字符
         * @param key
         */
        public void onKeyAdd(String key);

        /**
         * 删除按钮
         */
        public void onKeyDelete();

        /**
         * 显示数字键盘
         */
        public void onKeyShowNumKeyboard();

        /**
         * 显示特殊字符键盘
         */
        public void onKeyShowSpecialKeyboard();
    }



}

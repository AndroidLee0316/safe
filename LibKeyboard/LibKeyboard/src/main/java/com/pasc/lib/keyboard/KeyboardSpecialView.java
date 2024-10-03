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

/**
 * 功能：特殊字符键盘view
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/25
 */
public class KeyboardSpecialView extends KeyboardBaseView {

    private Context mContext;
    /**
     * 特殊字符键盘
     */
    private List<String> mSpecialKeyList;

    //宽度
    private int viewWidth = 0;
    //高度
    private int viewHeight = 0;
    //item间隔
    private int itemSpace = 0;
    //item高度
    private int itemHeight = 0;
    //item宽度
    private int itemWidth = 0;
    //普通英文键盘容器
    private FrameLayout mSpecialContainerFL;
    //删除键盘
    private LinearLayout mSpecialDeleteLL;
    //删除键
    private ImageView mSpecialDeleteIV;
    //删除单击监听
    private OnClickListener mSpecialDeleteOnClickListener;

    //按键点击回调
    private KeyClickCallBack mCallBack;

    //切换到数字键盘
    private static final String KEY_NUM = "123";
    //删除按钮
    private static final String KEY_DEL = "del";
    //切换到普通字符键盘
    private static final String KEY_NORMAL = "ABC";


    private static final int LINE_Special_ONE_KEY_SIZE = 10;

    public KeyboardSpecialView(Context context, Context mContext) {
        super(context,null);
    }

    public KeyboardSpecialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initData();
        initListener();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        viewWidth = wm.getDefaultDisplay().getWidth();
        viewHeight = wm.getDefaultDisplay().getHeight();
        initView();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initData(){
        
        mSpecialKeyList = new ArrayList<>();
        //第一行
        mSpecialKeyList.add(".");
        mSpecialKeyList.add(",");
        mSpecialKeyList.add("?");
        mSpecialKeyList.add("'");
        mSpecialKeyList.add("!");
        mSpecialKeyList.add("@");
        mSpecialKeyList.add("#");
        mSpecialKeyList.add("$");
        mSpecialKeyList.add("%");
        mSpecialKeyList.add("^");

        //第二行
        mSpecialKeyList.add("&");
        mSpecialKeyList.add("*");
        mSpecialKeyList.add("(");
        mSpecialKeyList.add(")");
        mSpecialKeyList.add("\"");
        mSpecialKeyList.add("=");
        mSpecialKeyList.add("_");
        mSpecialKeyList.add("`");
        mSpecialKeyList.add(":");
        mSpecialKeyList.add(";");

        //第三行
        mSpecialKeyList.add("~");
        mSpecialKeyList.add("|");
        mSpecialKeyList.add("+");
        mSpecialKeyList.add("-");
        mSpecialKeyList.add("\\");
        mSpecialKeyList.add("/");
        mSpecialKeyList.add("[");
        mSpecialKeyList.add("]");
        mSpecialKeyList.add(KEY_DEL);

        //第四行
        mSpecialKeyList.add(KEY_NUM);
        mSpecialKeyList.add("{");
        mSpecialKeyList.add("}");
        mSpecialKeyList.add("<");
        mSpecialKeyList.add(">");
        mSpecialKeyList.add(KEY_NORMAL);

    }

    private void initView(){
        removeAllViews();

        itemSpace = (int) mContext.getResources().getDimension(R.dimen.pasc_keyboard_normal_item_spacing);
        itemHeight =  (int) mContext.getResources().getDimension(R.dimen.pasc_keyboard_normal_item_height);
        itemWidth = (viewWidth - (LINE_Special_ONE_KEY_SIZE + 1) * itemSpace)/LINE_Special_ONE_KEY_SIZE;

        initSpecialKeys();
    }


    private void initSpecialKeys(){
        mSpecialContainerFL = new FrameLayout(mContext);
        int keySize = mSpecialKeyList.size();
        int pointX = 0;
        int pointY = 0;
        for (int i = 0; i < keySize; i++){
            if (i < 10){//第一行
                TextView itemTV1 = getTextView(mSpecialKeyList.get(i));
                initNormalKeyValue(itemTV1);

                pointX = i * itemWidth + (i+1) * itemSpace;
                LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = itemSpace;
                mSpecialContainerFL.addView(itemTV1,lp);

            }else if (i<20){//第二行
                pointX = i%10 * itemWidth + (i%10 + 1) * itemSpace;
                pointY = itemHeight + itemSpace*2;
                TextView itemTV = getTextView(mSpecialKeyList.get(i));
                initNormalKeyValue(itemTV);
                LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                lp.leftMargin = pointX;
                lp.topMargin = pointY;
                mSpecialContainerFL.addView(itemTV,lp);
            }else if (i<29){//第三行
                pointY = itemHeight * 2 +itemSpace * 3;
                if (i == 28){//删除键
                    mSpecialDeleteLL = new LinearLayout(mContext);
                    LayoutParams lp = new LayoutParams(itemWidth*2 + itemSpace , itemHeight);
                    lp.leftMargin = pointX + itemWidth + itemSpace;
                    lp.topMargin = pointY;
                    mSpecialDeleteLL.setBackgroundResource(R.drawable.pasc_selector_key_special);
                    mSpecialDeleteLL.setOnClickListener(mSpecialDeleteOnClickListener);

                    mSpecialDeleteIV = new ImageView(mContext);
                    mSpecialDeleteIV.setImageResource(R.drawable.pasc_selector_key_del);
                    mSpecialDeleteIV.setOnClickListener(mSpecialDeleteOnClickListener);

                    mSpecialDeleteLL.setGravity(Gravity.CENTER);
                    mSpecialDeleteLL.addView(mSpecialDeleteIV);

                    mSpecialContainerFL.addView(mSpecialDeleteLL,lp);
                }else {//其他普通按键

                    pointX = i%20 * itemWidth + (i%20 + 1) * itemSpace;

                    TextView itemTV = getTextView(mSpecialKeyList.get(i));
                    initNormalKeyValue(itemTV);
                    LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                    lp.leftMargin = pointX;
                    lp.topMargin = pointY;
                    mSpecialContainerFL.addView(itemTV,lp);

                }

            }else {
                if (i == 29){//第四行：数字键

                    pointX = itemSpace;
                    pointY = itemHeight*3 + itemSpace*4;

                    TextView itemTV = getTextView(mSpecialKeyList.get(i));
                    initSpecialKeyValue(itemTV);
                    LayoutParams lp = new LayoutParams(itemWidth*3 + itemSpace*2, itemHeight);
                    lp.leftMargin = pointX;
                    lp.topMargin = pointY;
                    lp.bottomMargin = itemSpace;
                    mSpecialContainerFL.addView(itemTV,lp);

                }else if (i == 34){//第四行：ABC键

                    pointX = pointX + + itemWidth + itemSpace;
                    pointY = itemHeight*3 + itemSpace*4;

                    TextView itemTV = getTextView(mSpecialKeyList.get(i));
                    initSpecialKeyValue(itemTV);
                    LayoutParams lp = new LayoutParams(itemWidth*3 + itemSpace*2, itemHeight);
                    lp.leftMargin = pointX;
                    lp.topMargin = pointY;
                    mSpecialContainerFL.addView(itemTV,lp);

                }else {//第四行：普通键

                    pointX = itemWidth*3 + itemSpace*3 + i%30 * itemWidth + (i%30 + 1) * itemSpace;
                    pointY = itemHeight*3 + itemSpace*4;

                    TextView itemTV = getTextView(mSpecialKeyList.get(i));
                    initNormalKeyValue(itemTV);
                    LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
                    lp.leftMargin = pointX;
                    lp.topMargin = pointY;
                    mSpecialContainerFL.addView(itemTV,lp);

                }
            }
        }

        LayoutParams lp = new LayoutParams(viewWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mSpecialContainerFL, lp);
    }

    /**
     * 初始化监听
     */
    private void initListener(){

        mSpecialDeleteOnClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null){
                    mCallBack.onKeyDelete();
                }
            }
        };
    }

    /**
     * 统一获取text
     * @param text
     * @return
     */
    private TextView getTextView(final String text){
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
                    }else if (KEY_NORMAL.equals(text)){
                        mCallBack.onKeyShowNormalKeyboard();
                    }else {
                        mCallBack.onKeyAdd(text);
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
     * 刷新主题
     * @param theme
     */
    public void updateTheme(KeyboardNormalTheme theme){
        if (mSpecialContainerFL != null){
            for (int i = 0 ; i < mSpecialContainerFL.getChildCount(); i++){
                if (mSpecialContainerFL.getChildAt(i) instanceof TextView){
                    if (theme.keyNormalItemTextColor != null){
                        ((TextView) mSpecialContainerFL.getChildAt(i)).setTextColor(theme.keyNormalItemTextColor);
                    }
                    if ((i == 29 || i == 34) && theme.keyNormalSpecialBG != null){
                        ((TextView) mSpecialContainerFL.getChildAt(i)).setBackground(theme.keyNormalSpecialBG.getConstantState().newDrawable());
                    }else if(theme.keyNormalItemBG != null){
                        ((TextView) mSpecialContainerFL.getChildAt(i)).setBackground(theme.keyNormalItemBG.getConstantState().newDrawable());
                    }
                }
            }
            if (mSpecialDeleteLL != null && theme.keyNormalSpecialBG != null){
                mSpecialDeleteLL.setBackground(theme.keyNormalSpecialBG);
            }
            if (mSpecialDeleteIV != null && theme.keyNormalDeleteIcon != null){
                mSpecialDeleteIV.setImageDrawable(theme.keyNormalDeleteIcon);
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
         * 显示普通字符键盘
         */
        public void onKeyShowNormalKeyboard();
    }


}

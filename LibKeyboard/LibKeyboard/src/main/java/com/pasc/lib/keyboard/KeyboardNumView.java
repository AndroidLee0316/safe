package com.pasc.lib.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * 虚拟键盘
 * @author yangzijian
 * @date 2019/2/13
 * @des
 * @modify 密码虚拟键盘
 **/
public class KeyboardNumView extends KeyboardBaseView {

    /**
     * 上下文
     */
    private Context context;

    //键盘gridview
    protected GridView mGridView;


    /**
     * 按键列表
     */
    protected ArrayList<String> valueList = new ArrayList<> ();

    //
    private static final String KEY_MONEY = ".";
    private static final String KEY_ID_CARD = "X";
    private static final String KEY_CHAR = "ABC";

    private KeyboardNumberTheme mKeyboardTheme = new KeyboardNumberTheme();

    /**
     * 按键回调
     */
    private CallBack mCallBack;


    /**
     * 按键回调之切换键盘
     */
    private CallBackChange mCallBackChange;



    public KeyboardNumView(Context context) {
        this (context, null);
    }

    public KeyboardNumView(Context context, AttributeSet attrs) {
        super (context, attrs);
        this.context = context;

        mGridView = new GridView(context);
        mGridView.setHorizontalSpacing((int) getResources().getDimension(R.dimen.pasc_keyboard_num_item_spacing));
        mGridView.setVerticalSpacing((int) getResources().getDimension(R.dimen.pasc_keyboard_num_item_spacing));
        mGridView.setNumColumns(3);
        addView (mGridView);

        //初始化数据
        initValueList ();
        setupView ();
        //初始化监听
        initListener();

    }


    /**
     * 初始化监听事件
     */
    protected void initListener(){

        mGridView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (mCallBack != null && valueList != null && valueList.size() > position){
                    String key = valueList.get (position);
                    if (position < 11) {    //点击0~9按钮
                        if (position == 9){
                            if (mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_ID_CARD || mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_MONEY){
                                //位置第10位，在类型为银行卡或者密码时候，因为是纯数字，所以是不可用的状态
                                mCallBack.onKeyAdd(key);
                            }else if(mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_CHAR){
                                if (mCallBackChange != null){
                                    mCallBackChange.onShowChar();
                                }
                            }
                        }else {
                            mCallBack.onKeyAdd(key);
                        }
                    } else {
                        if (position == 11) {      //点击退格键
                            mCallBack.onKeyDelete ();
                        }
                    }
                }
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
     * 设置切换键盘回调
     * @param mCallBackChange
     */
    public void setCallBackChange(CallBackChange mCallBackChange) {
        this.mCallBackChange = mCallBackChange;
    }

    /**
     * 初始化键盘
     */
    private void initValueList() {
        // 初始化按钮上应该显示的数字

        for (int i = 1; i < 13; i++) {
            if (i < 10) {
                valueList.add (String.valueOf (i));
            } else if (i == 10) {
                if (mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_MONEY) {
                    //钱
                    valueList.add (KEY_MONEY);
                } else if (mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_ID_CARD) {
                    //身份证
                    valueList.add (KEY_ID_CARD);
                } else if (mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_CHAR){
                    //切换到字符键盘
                    valueList.add(KEY_CHAR);
                }else {
                    valueList.add ("");
                }
            } else if (i == 11) {
                valueList.add ("0");
            } else if (i == 12) {
                valueList.add ("");
            }
        }

        //随机键盘
        if (mKeyboardTheme.keyNumberRandom){
            SecureRandom secureRandom = new SecureRandom();
            for (int i = 0; i < 12; i++) {
                int randomNum = secureRandom.nextInt(12);
                if (randomNum != 9 && randomNum != 11 && randomNum != 0){
                    String num0 = valueList.get(0);
                    valueList.set(0,valueList.get(randomNum));
                    valueList.set(randomNum,num0);
                }
            }
        }
    }

    /**
     * 重新刷新键盘列表
     */
    private void setupView() {
        KeyboardAdapter keyBoardAdapter = new KeyboardAdapter(context, valueList, mKeyboardTheme);
        if (mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_ID_CARD || mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_MONEY || mKeyboardTheme.keyboardType == KeyboardNumberTheme.TYPE_CHAR) {
            keyBoardAdapter.setTheTenBlockUsable (true);
        }
        mGridView.setAdapter (keyBoardAdapter);

    }


    /**
     * 设置键盘类型（0表示钱|金额输入、1表示身份证类型、2表示验证码类型、3表示银行卡类型、4表示密码类型）
     * @param keyboardType
     */
    public void setKeyboardType(int keyboardType) {
        mKeyboardTheme.keyboardType = keyboardType;
        valueList.clear ();
        initValueList ();
        setupView ();
        invalidate();
    }

    /**
     * 设置键盘主题
     * @param theme
     */
    public void updateKeybordTheme(KeyboardNumberTheme theme){
        if (theme == null){
            return;
        }
        mKeyboardTheme = theme;
        setKeyboardType(mKeyboardTheme.keyboardType);
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
     * 切换字符键盘监听
     */
    public static interface CallBackChange{
        /**
         * 显示字符键盘
         */
        public void onShowChar();
    }


}

package com.pasc.lib.keyboard;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 功能：键盘公共、基础view
 * <p>
 * create by lichangbao702
 * email : 1035268077@qq.com
 * date : 2019/4/28
 */
public class KeyboardBaseView extends FrameLayout {


    public KeyboardBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public static class KeyboardBaseTheme {

        //未设置参数
        public static final int UN_SET_DIMEN = -1;

    }

    /**
     * 普通字符键盘主题属性集合
     */
    public static class KeyboardNormalTheme extends KeyboardBaseTheme{


        /**
         * 普通键盘
         */
        public final static int TYPE_NORMAL = 10;
        /**
         * 普通键盘：大写
         */
        public final static int TYPE_NORMAL_UPPER = 11;
        /**
         * 特殊键盘
         */
        public final static int TYPE_NORMAR_SPECIAL = 12;

        //item字体颜色
        public ColorStateList keyNormalItemTextColor = null;


        //普通键盘相关
        /**
         * 普通键盘按钮背景
         */
        public Drawable keyNormalItemBG = null;
        /**
         * 普通键盘特殊按钮背景
         */
        public Drawable keyNormalSpecialBG = null;
        /**
         * 普通键盘删除键图标
         */
        public Drawable keyNormalDeleteIcon = null;
        /**
         * 普通键盘上档键图标
         */
        public Drawable keyNormalShiftIcon = null;
        /**
         * 普通键盘上档键图标:大写
         */
        public Drawable keyNormalShiftUpperIcon = null;


        public KeyboardNormalTheme merge(KeyboardNormalTheme theme){
            if (theme == null){
                return this;
            }

            if (theme.keyNormalItemTextColor != null){
                this.keyNormalItemTextColor = theme.keyNormalItemTextColor;
            }
            if (theme.keyNormalItemBG != null){
                this.keyNormalItemBG = theme.keyNormalItemBG;
            }
            if (theme.keyNormalSpecialBG != null){
                this.keyNormalSpecialBG = theme.keyNormalSpecialBG;
            }
            if (theme.keyNormalDeleteIcon != null){
                this.keyNormalDeleteIcon = theme.keyNormalDeleteIcon;
            }
            if (theme.keyNormalShiftIcon != null){
                this.keyNormalShiftIcon = theme.keyNormalShiftIcon;
            }
            if (theme.keyNormalShiftUpperIcon != null){
                this.keyNormalShiftUpperIcon = theme.keyNormalShiftUpperIcon;
            }


            return this;
        }

    }


    /**
     * 数字键盘主题属性集合
     */
    public static class KeyboardNumberTheme extends KeyboardBaseTheme{

        /**
         * 键盘类型：输入纯数字类型
         */
        public final static int TYPE_NUMBER = 21;
        /**
         * 键盘类型：输入金额
         */
        public final static int TYPE_MONEY = 22;
        /**
         * 键盘类型：输入身份证
         */
        public final static int TYPE_ID_CARD = 23;
        /**
         * 键盘类型：带切换到普通键盘的按钮
         */
        public final static int TYPE_CHAR = 24;

        public static final int DEFAULT_KEYBOARD_TYPE = TYPE_NUMBER;
        //键盘类型
        public int keyboardType = DEFAULT_KEYBOARD_TYPE;


        //item字体颜色
        public ColorStateList keyNumberItemTextColor = null;

        /**
         * 数字键盘按钮背景
         */
        public Drawable keyNumberItemBG = null;
        /**
         * 数字键盘删除按钮图标
         */
        public Drawable keyNumberDeleteIcon = null;
        /**
         * 是否是随机键盘
         */
        public boolean keyNumberRandom = false;


        public KeyboardNumberTheme merge(KeyboardNumberTheme theme){

            if (theme == null){
                return this;
            }

            if (theme.keyboardType != DEFAULT_KEYBOARD_TYPE){
                this.keyboardType = theme.keyboardType;
            }
            if (theme.keyNumberItemTextColor != null){
                this.keyNumberItemTextColor = theme.keyNumberItemTextColor;
            }
            if (theme.keyNumberItemBG != null){
                this.keyNumberItemBG = theme.keyNumberItemBG;
            }
            if (theme.keyNumberDeleteIcon != null){
                this.keyNumberDeleteIcon = theme.keyNumberDeleteIcon;
            }
            if (theme.keyNumberRandom != false){
                this.keyNumberRandom = theme.keyNumberRandom;
            }

            return this;
        }

    }

    /**
     * 键盘toolbar主题
     */
    public static class KeyboardToolbarTheme extends KeyboardBaseTheme{


        //toolbar高度
        public int toolbarHeight = UN_SET_DIMEN;
        //左右padding
        public int toolbarPaddingLeftRight = UN_SET_DIMEN;
        //toolbar背景
        public Drawable toolbarBG = null;


        //title图标
        public Drawable titleIcon = null;
        //title
        public String titleText = null;
        //title颜色
        public ColorStateList titleColor = null;
        //title字体大小
        public int titleSize = UN_SET_DIMEN;
        //title是否移动到左边
        public boolean titleToLeft = false;
        //分割线颜色
        public ColorStateList splitColor = null;


        //右边图标
        public Drawable rightIcon = null;
        //右边名称
        public String rightText = null;
        //右边名称颜色
        public ColorStateList rightColor = null;
        //右边字体大小
        public int rightSize = UN_SET_DIMEN;


        public KeyboardToolbarTheme merge(KeyboardToolbarTheme theme){
            if (theme == null){
                return this;
            }

            if (theme.toolbarHeight != UN_SET_DIMEN){
                this.toolbarHeight = theme.toolbarHeight;
            }
            if (theme.toolbarPaddingLeftRight != UN_SET_DIMEN){
                this.toolbarPaddingLeftRight = theme.toolbarPaddingLeftRight;
            }
            if (theme.toolbarBG != null){
                this.toolbarBG = theme.toolbarBG;
            }

            if (theme.titleIcon != null){
                this.titleIcon = theme.titleIcon;
            }
            if (theme.titleColor != null){
                this.titleColor = theme.titleColor;
            }
            if (theme.titleText != null){
                this.titleText = theme.titleText;
            }
            if (theme.titleSize != UN_SET_DIMEN){
                this.titleSize = theme.titleSize;
            }
            if (theme.titleToLeft){
                this.titleToLeft = theme.titleToLeft;
            }


            if (theme.splitColor != null){
                this.splitColor = theme.splitColor;
            }


            if (theme.rightIcon != null){
                this.rightIcon = theme.rightIcon;
            }
            if (theme.rightText != null){
                this.rightText = theme.rightText;
            }
            if (theme.rightColor != null){
                this.rightColor = theme.rightColor;
            }
            if (theme.rightSize != UN_SET_DIMEN){
                this.rightSize = theme.rightSize;
            }


            return this;
        }

    }



}

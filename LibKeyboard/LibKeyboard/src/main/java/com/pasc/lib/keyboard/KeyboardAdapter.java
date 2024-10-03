package com.pasc.lib.keyboard;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 虚拟键盘适配器
 * @author yangzijian
 * @date 2019/2/14
 * @des
 * @modify
 **/
public class KeyboardAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mValueList;

    //键盘主题
    private KeyboardNumView.KeyboardNumberTheme mKeyboardTheme;
    //第十个item是否可用(是否有key显示)
    private boolean isTheTenBlockUsable = false;

    public void setTheTenBlockUsable(boolean theTenBlockUsable) {
        isTheTenBlockUsable = theTenBlockUsable;
    }

    public KeyboardAdapter(Context mContext, ArrayList<String> mValueList, KeyboardNumView.KeyboardNumberTheme keyboardTheme) {
        this.mContext = mContext;
        this.mValueList = mValueList;
        this.mKeyboardTheme = keyboardTheme;
    }

    @Override
    public int getCount() {
        return mValueList.size();
    }

    @Override
    public Object getItem(int position) {
        return mValueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.pasc_keyboard_item, null);
            viewHolder = new ViewHolder();
            viewHolder.containerRL = convertView.findViewById(R.id.pasc_keyboard_item_rl);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.pasc_keyboard_item_tv);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.pasc_keyboard_item_iv);
            convertView.setTag(viewHolder);
            if (mKeyboardTheme.keyNumberItemTextColor != null){
                viewHolder.textView.setTextColor(mKeyboardTheme.keyNumberItemTextColor);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (position == 9) {
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.imageView.setImageDrawable(null);
            viewHolder.textView.setText(mValueList.get(position));
            if (!isTheTenBlockUsable){
                viewHolder.textView.setBackgroundColor(convertView.getResources().getColor(android.R.color.transparent));
            }else {
                if (mKeyboardTheme.keyNumberItemBG != null){
                    viewHolder.textView.setBackground(mKeyboardTheme.keyNumberItemBG.getConstantState().newDrawable());
                }else {
                    viewHolder.textView.setBackground(convertView.getResources().getDrawable(R.drawable.pasc_keyboard_bg_item_keyboard));
                }
            }
            viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.pasc_keyboard_key_special_size));
        } else if (position == 11) {
            if (mKeyboardTheme.keyNumberDeleteIcon != null){
                viewHolder.imageView.setImageDrawable(mKeyboardTheme.keyNumberDeleteIcon);
            }else {
                viewHolder.imageView.setImageResource(R.drawable.pasc_selector_key_del);
            }

            viewHolder.textView.setVisibility(View.GONE);
            viewHolder.textView.setBackgroundColor(convertView.getResources().getColor(android.R.color.transparent));
        } else {
            viewHolder.imageView.setImageDrawable(null);
            viewHolder.textView.setVisibility(View.VISIBLE);
            viewHolder.textView.setText(mValueList.get(position));
            viewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,mContext.getResources().getDimension(R.dimen.pasc_keyboard_key_normal_size));
            if (mKeyboardTheme.keyNumberItemBG != null){
                viewHolder.textView.setBackground(mKeyboardTheme.keyNumberItemBG.getConstantState().newDrawable());
            }else {
                viewHolder.textView.setBackground(convertView.getResources().getDrawable(R.drawable.pasc_selector_keyboard_btn_num));
            }
        }

        return convertView;
    }

    public final class ViewHolder {
        public RelativeLayout containerRL;
        public TextView textView;
        public ImageView imageView;
    }
}

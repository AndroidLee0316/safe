package com.pasc.safekeyboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T, H extends BaseHolder> extends BaseAdapter {
    public List<T> data;
    public Context context;

    public CommonAdapter(Context context, List<T> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size ();
    }

    @Override
    public Object getItem(int position) {
        return data.get (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        H baseHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from (context).inflate (layout (), null);
            baseHolder = createBaseHolder (convertView);
        } else {
            baseHolder = (H) convertView.getTag ();
        }
        setData (context,baseHolder, data.get (position));
        return convertView;
    }

    public abstract int layout();

    public abstract H createBaseHolder(View rootView);

    public abstract void setData(Context context,H holder, T data);

}

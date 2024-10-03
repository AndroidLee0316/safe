package com.pasc.safekeyboard;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pasc.safekeyboard.adapter.BaseHolder;

/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class ClassHolder extends BaseHolder {
    public Button btn;
    public ClassHolder(View rootView) {
        super (rootView);
        btn=rootView.findViewById (R.id.btn);

    }
}

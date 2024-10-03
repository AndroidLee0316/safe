package com.pasc.safekeyboard;

import android.app.Activity;

/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class ClassBean {
    public Class<? extends Activity> aClass;
    public String name;

    public ClassBean(Class<? extends Activity> aClass, String name) {
        this.aClass = aClass;
        this.name = name;
    }
}

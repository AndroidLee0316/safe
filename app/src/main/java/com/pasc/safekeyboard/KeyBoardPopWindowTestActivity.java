package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.keyboard.KeyboardView;


/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class KeyBoardPopWindowTestActivity extends AppCompatActivity {

    private EditText test_normal_activity_main_ket;
    private Button test_keyboard_popwindow_demo_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.test_keyboard_popwindow_demo);
        KeyboardPopWindow keyboardPopWindow = new KeyboardPopWindow(this);
        test_normal_activity_main_ket = findViewById(R.id.test_keyboard_popwindow_demo_ket);
        test_keyboard_popwindow_demo_btn = findViewById(R.id.test_keyboard_popwindow_demo_btn);
        test_keyboard_popwindow_demo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(KeyBoardPopWindowTestActivity.this,"test_keyboard_popwindow_demo_btn",Toast.LENGTH_SHORT).show();
                keyboardPopWindow.show();
            }
        });
        //KeyboardPopWindow.bindEdit(this,test_normal_activity_main_ket);
        keyboardPopWindow.setKeyBoardType(KeyboardBaseView.KeyboardNumberTheme.TYPE_NUMBER);
        keyboardPopWindow.setCallBack(new KeyboardView.CallBack() {
            @Override
            public void onKeyAdd(String s) {

            }

            @Override
            public void onKeyDelete() {

            }
        });
    }
}

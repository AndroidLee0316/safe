package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.pasc.lib.keyboard.KeyboardView;
import com.pasc.lib.keyboard.SafeKeyboardPopWindow;


/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class KeyBoardViewTestActivity extends AppCompatActivity {

    private EditText test_normal_activity_main_ket;


    private KeyboardView test_normal_activity_main_kv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.test_keyboard_view_demo);
        test_normal_activity_main_ket = findViewById(R.id.test_keyboard_view_demo_ket);
        test_normal_activity_main_kv = findViewById(R.id.test_keyboard_view_demo_kv);
        test_normal_activity_main_kv.setCallBack(new KeyboardView.CallBack() {
            @Override
            public void onKeyAdd(String key) {
                test_normal_activity_main_ket.setText(test_normal_activity_main_ket.getText().append(key));
            }

            @Override
            public void onKeyDelete() {

            }
        });
    }
}

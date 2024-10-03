package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pasc.lib.keyboard.KeyboardEditText;


/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class KeyBoardEditTestActivity extends AppCompatActivity {

    private KeyboardEditText editText;

    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.test_keyboard_edit_demo);
        editText = findViewById(R.id.test_keyboard_edit_demo_et);
        button = findViewById(R.id.test_keyboard_edit_demo_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KeyBoardEditTestActivity.this,editText.getRealSafeInputText(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

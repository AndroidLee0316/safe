package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.keyboard.SafeKeyboardPopWindow;


/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class SafeKeyBoardPopWindowTestActivity extends AppCompatActivity {

    private EditText test_normal_activity_main_ket;

    private Button commitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.test_keyboard_popwindow_demo);
        test_normal_activity_main_ket = findViewById(R.id.test_keyboard_popwindow_demo_ket);
        commitBtn = findViewById(R.id.test_keyboard_popwindow_demo_btn);
        SafeKeyboardPopWindow popWindow = SafeKeyboardPopWindow.bindEdit(this,test_normal_activity_main_ket);
        popWindow.setMaxLength(6);
        popWindow.setOnFinishListener(new SafeKeyboardPopWindow.OnFinishListener() {
            @Override
            public void onPasswordInputFinish(String password, boolean isInvalidatePwd) {
                Toast.makeText(SafeKeyBoardPopWindowTestActivity.this, "输入完毕：" + isInvalidatePwd, Toast.LENGTH_SHORT).show();
            }
        });
        commitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SafeKeyBoardPopWindowTestActivity.this,popWindow.getSafeInputString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

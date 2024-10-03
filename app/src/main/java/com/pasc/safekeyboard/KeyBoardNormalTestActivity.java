package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pasc.lib.keyboard.KeyboardBaseView;
import com.pasc.lib.keyboard.KeyboardEditText;
import com.pasc.lib.keyboard.KeyboardPopWindow;
import com.pasc.lib.keyboard.KeyboardView;
import com.pasc.lib.keyboard.SafeKeyboardEditText;
import com.pasc.lib.keyboard.SafeKeyboardPopWindow;


/**
 * @date 2019/4/24
 * @des
 * @modify
 **/
public class KeyBoardNormalTestActivity extends AppCompatActivity {

    private FormatEditText test_normal_activity_main_ket;

    private KeyboardEditText test_normal_activity_main_ket2;

    private SafeKeyboardEditText test_normal_activity_main_ket3;

    private Button test_normal_activity_main_button;

    private SafeKeyboardPopWindow safeKeyBoardPopWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.test_normal_activity_main);
        test_normal_activity_main_ket = findViewById(R.id.test_normal_activity_main_ket);
        test_normal_activity_main_ket2 = findViewById(R.id.test_normal_activity_main_ket2);
        test_normal_activity_main_ket3 = findViewById(R.id.test_normal_activity_main_ket3);

        KeyboardPopWindow.bindEdit(this, test_normal_activity_main_ket, KeyboardBaseView.KeyboardNormalTheme.TYPE_NORMAL);

        KeyboardView.KeyboardTheme keyboardTheme = new KeyboardView.KeyboardTheme();
        keyboardTheme.keyboardTheme = KeyboardView.KeyboardTheme.THEME_BLACK;
        keyboardTheme.keyboardType = KeyboardBaseView.KeyboardNumberTheme.TYPE_ID_CARD;
        keyboardTheme.keyboardToolbarTheme.titleIcon = getResources().getDrawable(R.drawable.pasc_keyboard_logo);
        keyboardTheme.keyboardToolbarTheme.titleText = "安全键盘";
        keyboardTheme.keyboardToolbarTheme.rightText = "收起";


        safeKeyBoardPopWindow = SafeKeyboardPopWindow.bindEdit(this,test_normal_activity_main_ket3,keyboardTheme);
        test_normal_activity_main_button = findViewById(R.id.test_normal_activity_main_button);
        test_normal_activity_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(KeyBoardNormalTestActivity.this,safeKeyBoardPopWindow.getSafeInputString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

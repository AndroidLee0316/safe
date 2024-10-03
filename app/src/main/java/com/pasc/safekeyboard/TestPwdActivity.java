package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.pasc.lib.keyboard.PwdKeyBoardListener;
import com.pasc.lib.keyboard.PwdKeyboardView;

/**
 * @date 2019/8/23
 * @des
 * @modify
 **/
public class TestPwdActivity extends AppCompatActivity {
    private EwalletPassWordView pwdView;
    private PwdKeyboardView pwdBoard;

    private void assignViews() {
        pwdView = (EwalletPassWordView) findViewById(R.id.pwd_view);
        pwdBoard = (PwdKeyboardView) findViewById(R.id.pwd_board);
        pwdBoard.setMaxLength (6);
        pwdBoard.showTitleView (true);
        pwdBoard.setTitle ("安全键盘");
        pwdBoard.setMode (PwdKeyboardView.simpleMode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.test_pwd_activity);
        assignViews ();

        pwdView.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                pwdBoard.show ();
            }
        });
        pwdBoard.setPwdBoardListener (new PwdKeyBoardListener () {
            @Override
            public void onPasswordInputFinish(String password, boolean isInvalidatePwd) {
                Log.e ("yzj", "onPasswordInputFinish: password: "+password+" , isInvalidatePwd: "+isInvalidatePwd );
                pwdBoard.clearPassWord ();
            }

            @Override
            public void addPassWord(int currentIndex, int totalLength) {
                pwdView.refresh (currentIndex,totalLength);
            }

            @Override
            public void removePassWord(int currentIndex, int totalLength) {
                pwdView.refresh (currentIndex,totalLength);

            }

            @Override
            public void clearPassWord(int currentIndex, int totalLength) {
                pwdView.refresh (currentIndex,totalLength);

            }
        });
    }
}

//package com.pasc.safekeyboard;
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Toast;
//
//import com.pasc.lib.keyboard.PwdKeyBoardListener;
//import com.pasc.lib.keyboard.PwdKeyboardView;
//
///**
// * @author yangzijian
// * @date 2019/2/14
// * @des
// * @modify 设置密码
// **/
//public class SettingPayPwdTestActivity extends AppCompatActivity {
//    PwdKeyboardView keyboardView;
//    PassWordView pwd;
//    private int count = 0;
//    private String prePwd;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate (savedInstanceState);
//        setContentView (R.layout.test_ewallet_setting_paypwd_activity);
//        keyboardView = findViewById (R.id.keyboardView22);
//        pwd = findViewById (R.id.pwd);
//        enableSecure (this);
//        pwd.setOnClickListener (new View.OnClickListener () {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        keyboardView.setPwdBoardListener (new PwdKeyBoardListener () {
//            @Override
//            public void onPasswordInputFinish(String password,boolean isInvalidatePwd) {
//                if (isInvalidatePwd){
//                    Toast.makeText (getApplicationContext (),"密码太简单了",Toast.LENGTH_SHORT).show ();
//                    return;
//                }
//                Log.e ("yzj", "password: native -> " + password);
//                if (count==0){
//                    count++;
//                    Log.e ("yzj","first ");
//                    prePwd=password;
//                }else if (count==1){
//                    count=0;
//                    Log.e ("yzj","sendCode ");
//                    if (password.equals (prePwd)){
//                        Log.e ("yzj","验证通过 ");
//                    }else {
//                        Log.e ("yzj","两次密码不一致 ");
//
//                    }
//                }
//            }
//            @Override
//            public void addPassWord(int currentIndex, int totalLength) {
//                pwd.refresh (currentIndex,totalLength);
//
//            }
//
//            @Override
//            public void removePassWord(int currentIndex, int totalLength) {
//                pwd.refresh (currentIndex,totalLength);
//
//            }
//
//            @Override
//            public void clearPassWord(int currentIndex, int totalLength) {
//                pwd.refresh (currentIndex,totalLength);
//
//            }
//        });
//    }
//
//    void clearPwd(){
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        disableSecure (this);
//        keyboardView.clearPassWord ();
//        clearPwd ();
//        super.onDestroy ();
//    }
//
//    public void enableSecure(Activity activity) {
//        activity.getWindow ().setFlags (WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//    }
//
//    public void disableSecure(Activity activity) {
//        activity.getWindow ().clearFlags (WindowManager.LayoutParams.FLAG_SECURE);
//    }
//
//}

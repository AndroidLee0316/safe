package com.pasc.safekeyboard;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.pasc.lib.safe.SafeUtil;

public class SafeTestActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_safe_demo);
        resultView = findViewById(R.id.activity_safe_demo_result);
        resultView.setOnClickListener(this);
        resultView.setText("是否是debug模式："+ SafeUtil.isApkInDebug(this) + "\n是否已root    ："+ SafeUtil.isDeviceRooted() + "\n是否已hoot     ："+ SafeUtil.isHooked());
    }

    @Override
    public void onClick(View v) {
    }
}

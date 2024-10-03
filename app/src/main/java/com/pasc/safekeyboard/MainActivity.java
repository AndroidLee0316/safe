package com.pasc.safekeyboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.pasc.safekeyboard.adapter.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    private List<ClassBean> datas = new ArrayList<> ();

    {
        datas.add (new ClassBean (KeyBoardNormalTestActivity.class, "普通键盘"));
        datas.add (new ClassBean (KeyBoardViewTestActivity.class, "自定义键盘view"));
        datas.add (new ClassBean (KeyBoardPopWindowTestActivity.class, "自定义键盘pop"));
        datas.add (new ClassBean (KeyBoardEditTestActivity.class, "自定义键盘Edit"));
        datas.add (new ClassBean (SafeKeyBoardPopWindowTestActivity.class, "安全键盘键盘pop"));
        datas.add (new ClassBean (EncryptionTestActivity.class, "encryption"));
        datas.add (new ClassBean (SafeTestActivity.class, "安全"));
        datas.add (new ClassBean (TestPwdActivity.class, "潜入键盘"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        listView = findViewById (R.id.listView);

        listView.setAdapter (new CommonAdapter<ClassBean, ClassHolder> (this, datas) {
            @Override
            public int layout() {
                return R.layout.class_item;
            }

            @Override
            public ClassHolder createBaseHolder(View rootView) {
                return new ClassHolder (rootView);
            }

            @Override
            public void setData(Context context, ClassHolder holder, ClassBean data) {
                holder.btn.setText (data.name);
                holder.btn.setOnClickListener (new View.OnClickListener () {
                    @Override
                    public void onClick(View v) {
                        startActivity (new Intent (MainActivity.this, data.aClass));
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

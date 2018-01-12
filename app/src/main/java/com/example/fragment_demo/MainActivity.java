package com.example.fragment_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.paic.crm.inputhelper.fragment.InputFragment;


public class MainActivity extends FragmentActivity
        implements InputFragment.OnRecordFinishCallback,
        InputFragment.OnSendCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout panelLayout =
                (FrameLayout) findViewById(R.id.panel_root);

        InputFragment inputFragment = new InputFragment();
        inputFragment.showBottom(this, R.id.panel_root);
    }

    @Override
    public void onRecordFinish(String path) {
        Toast.makeText(this,"录音结束，路径是："+path,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendBtnClick(String content) {
        Toast.makeText(this,"发送消息，内容是："+content,Toast.LENGTH_SHORT).show();

    }
}

package com.example.ex_zhangyuelei001.mylibrary;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.zylei_library.uihelper.fragment.ChatBottomFragment;


public class MainActivity extends FragmentActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ChatBottomFragment editView = new ChatBottomFragment();
        editView.showBottom(this,R.id.frame_layout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

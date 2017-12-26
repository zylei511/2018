package com.example.fragment_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;

import com.example.zylei_library.uihelper.fragment.InputFragment;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FrameLayout panelLayout =
                (FrameLayout) findViewById(R.id.panel_root);

        InputFragment inputFragment = new InputFragment();
        inputFragment.showBottom(this, R.id.panel_root);
    }
}

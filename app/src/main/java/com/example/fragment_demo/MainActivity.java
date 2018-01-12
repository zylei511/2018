package com.example.fragment_demo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.paic.crm.inputhelper.entity.QQFaceEntity;
import com.paic.crm.inputhelper.fragment.InputFragment;

import java.util.ArrayList;


public class MainActivity extends FragmentActivity
        implements InputFragment.OnRecordFinishCallback,
        InputFragment.OnSendCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputFragment inputFragment = new InputFragment();
        inputFragment.showBottom(this, R.id.panel_root, true, getAddMoreNames(), getAddMoreIcons(), QQFaceEntity.class);
    }

    private ArrayList<String> getAddMoreNames() {
        ArrayList<String> addMoreNames = new ArrayList<>();
        addMoreNames.add(getString(R.string.text_pictrue));
        addMoreNames.add(getString(R.string.text_replay));
        addMoreNames.add(getString(R.string.text_news));
        addMoreNames.add(getString(R.string.text_send_messege));
        return addMoreNames;
    }

    private ArrayList<Integer> getAddMoreIcons() {
        ArrayList<Integer> addMoreIcons = new ArrayList<>();
        addMoreIcons.add(R.drawable.icon_pictrue);
        addMoreIcons.add(R.drawable.icon_replay);
        addMoreIcons.add(R.drawable.icon_news);
        addMoreIcons.add(R.drawable.icon_send_messege);
        return addMoreIcons;
    }

    @Override
    public void onRecordFinish(String path) {
        Toast.makeText(this, "录音结束，路径是：" + path, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendBtnClick(String content) {
        Toast.makeText(this, "发送消息，内容是：" + content, Toast.LENGTH_SHORT).show();

    }
}

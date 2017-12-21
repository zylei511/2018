package com.example.zylei_library.uihelper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.ChatAddBean;
import com.example.zylei_library.uihelper.adapter.ChatAddGridAdapter;

import java.util.ArrayList;
import java.util.List;


public class AddMoreFragment extends Fragment implements AdapterView.OnItemClickListener {
    private int[] icons = {R.drawable.icon_pictrue, R.drawable.icon_replay, R.drawable.icon_news,
            R.drawable.icon_send_messege};
    private int[] names = {R.string.text_pictrue, R.string.text_replay, R.string.text_news,
            R.string.text_send_messege};


    public AddMoreFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.chat_add_grid);
        ChatAddGridAdapter addGridAdapter = new ChatAddGridAdapter(getActivity(),
                getDatas(), R.layout.item_chat_add_grid_layout);
        gridView.setAdapter(addGridAdapter);
        gridView.setOnItemClickListener(this);
        return view;
    }

    private List<ChatAddBean> getDatas() {
        List<ChatAddBean> list = new ArrayList<>();
        for (int i = 0;i<icons.length;i++){
            ChatAddBean chatAddBean = new ChatAddBean();
            chatAddBean.setIconName(getString(names[i]));
            chatAddBean.setIconRes(icons[i]);
            list.add(chatAddBean);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}

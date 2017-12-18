package com.example.zylei_library.uihelper;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.adapter.FaceView;
import com.example.zylei_library.uihelper.entity.MsgFaceModle;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatBottomFragment extends BaseFragment implements FaceView.OnFaceItemClickListener, TextWatcher {


    private EditText editText;
    private ImageButton sendBtn;
    private ImageButton faceBtn;
    private ImageButton addBtn;

    public ChatBottomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_view, container, false);
        editText = (EditText) view.findViewById(R.id.msg_et);
        editText.addTextChangedListener(this);
        faceBtn = (ImageButton) view.findViewById(R.id.face_btn);
        sendBtn = (ImageButton) view.findViewById(R.id.send_btn);
        addBtn = (ImageButton) view.findViewById(R.id.btn_chat_add);
        FaceFragment leftFragment = FaceFragment.newInstance("", "");
        AddMoreFragment rightFragment = AddMoreFragment.newInstance("", "");
        ChatAddHelper.newInstance()
                .bindView(getActivity(), rightFragment, R.id.layout, addBtn)
                .bindActiveState(R.drawable.icon_add_btn_pressed)
                .bindInActiveState(R.drawable.icon_add_btn_unpressed)
                .create();
        FaceHelper.newInstance().
                bindView(getActivity(), leftFragment, R.id.layout, faceBtn)
                .bindActiveState(R.drawable.icon_expression_pressed)
                .bindInActiveState(R.drawable.icon_expression_unpressed)
                .create();
        return view;
    }

    /**
     * 展示输入框
     *
     * @param context
     * @param layoutId
     */
    public void showBottom(Context context, int layoutId) {
        FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(layoutId, this);
        ft.commit();
    }

    @Override
    public void onFaceItemClick(MsgFaceModle msgFaceModle) {
        editText.setText(msgFaceModle.getCharacter());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
            sendBtn.setVisibility(View.GONE);
            addBtn.setVisibility(View.VISIBLE);
        } else {
            sendBtn.setVisibility(View.VISIBLE);
            addBtn.setVisibility(View.GONE);
        }
    }
}

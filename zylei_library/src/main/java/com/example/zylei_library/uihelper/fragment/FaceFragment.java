package com.example.zylei_library.uihelper.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.entity.EmojiFaceEntity;
import com.example.zylei_library.uihelper.entity.QQFaceEntity;


public class FaceFragment extends Fragment {

    private FaceFragmentHelper faceView;


    public FaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.msg_face_index_view);
        faceView = FaceFragmentHelper.newInstance()
                .addFace(getActivity(), QQFaceEntity.class)
                .addFace(getActivity(), EmojiFaceEntity.class)
                .addViewPager(viewPager)
                .addViewPagerCursor(layout)
                .create();
        return view;
    }

    public void setOnFaceOprateListener(OnFaceOprateListener onFaceOprateListener){
        faceView.setOnFaceOprateListener(onFaceOprateListener);
    }


}

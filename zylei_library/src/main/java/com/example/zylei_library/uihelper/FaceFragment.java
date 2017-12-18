package com.example.zylei_library.uihelper;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.zylei_library.R;
import com.example.zylei_library.uihelper.adapter.FaceView;
import com.example.zylei_library.uihelper.entity.EmojiFaceEntity;
import com.example.zylei_library.uihelper.entity.QQFaceEntity;


public class FaceFragment extends BaseFragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "FACE_ITEM_CLICK_LISTENER";

    private String mParam1;
    private String mParam2;
    private FaceView.OnFaceItemClickListener faceItemClickListener;


    public FaceFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaceFragment.
     */
    public static FaceFragment newInstance(String param1, String param2) {
        FaceFragment fragment = new FaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.msg_face_index_view);
        FaceView.newInstance().addFace(getActivity(), QQFaceEntity.class)
                .addFace(getActivity(), EmojiFaceEntity.class)
                .addViewPager(viewPager)
                .addViewPagerCursor(layout)
                .addFaceItemClickListener(faceItemClickListener)
                .create();
        return view;
    }
}

package com.paic.crm.sdk.ucmcore.uihelper.entity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.paic.crm.sdk.ucmcore.R;
import com.paic.crm.sdk.ucmcore.uihelper.ChatAddHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ex-zhangyuelei001 on 2017/12/13.
 */

public class FaceEntityFactory {
    private static FaceEntityFactory entityFactory;
    private static final int MAX_PAGE_SIZE = 23;

    public static FaceEntityFactory newInstance() {
        if (entityFactory == null) {
            synchronized (ChatAddHelper.class) {
                if (entityFactory == null) {
                    entityFactory = new FaceEntityFactory();
                }
            }
        }
        return entityFactory;
    }

    public void create(Class<BaseFaceEntity>... tClass) {
        BaseFaceEntity baseFaceEntity = null;
        try {
            for (Class<BaseFaceEntity> t : tClass) {
                baseFaceEntity = t.newInstance();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化表情数据
     *
     * @param faceEntity
     * @return
     */
    private void initFaceList(BaseFaceEntity faceEntity) {
        List<MsgFaceModle> list = new ArrayList<>();
        for (int i = 0; i < faceEntity.getFaceImgIds().size(); i++) {

            if (i % MAX_PAGE_SIZE <= MAX_PAGE_SIZE) {
                MsgFaceModle faceModle = new MsgFaceModle();
                faceModle.setId(faceEntity.getFaceImgIds().get(i));
                faceModle.setCharacter(faceEntity.getFaceImgNames().get(i));
            } else {

            }

        }
    }


    /**
     * 初始化GridView控件
     *
     * @param context
     * @return
     */
    private GridView initGridView(Context context) {
        GridView gridView = new GridView(context);
        int commonSpacing = (int) context.getResources().getDimension(R.dimen.padding_5dp);
        gridView.setHorizontalSpacing(commonSpacing);
        gridView.setVerticalSpacing(commonSpacing);
        gridView.setNumColumns(6);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) gridView.getLayoutParams();
        layoutParams.width = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewPager.LayoutParams.MATCH_PARENT;
        layoutParams.setMargins(commonSpacing, commonSpacing, commonSpacing, commonSpacing);
        gridView.setLayoutParams(layoutParams);
        return gridView;
    }
}

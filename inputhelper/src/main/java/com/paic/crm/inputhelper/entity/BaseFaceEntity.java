package com.paic.crm.inputhelper.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;

import java.util.List;
import java.util.regex.Pattern;

/**
 * 表情的基础类
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/11.
 */

public abstract class BaseFaceEntity {

    /**
     * 储存bitmap的集合
     */
    private SparseArray<Bitmap> bitmaps = new SparseArray<>();


    public abstract List<String> getFaceImgNames();


    public abstract List<Integer> getFaceImgIds();


    public abstract List<String> getFaceSymbols();


    public abstract Pattern getNamePattern();


    public abstract Pattern getSymbolPattern();


    /**
     * 将imageId 转化为bitmap，并放在集合中
     *
     * @param context
     */
    protected void initExpression(Context context) {
        for (int id : getFaceImgIds()) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            bitmaps.put(id, bitmap);
        }
    }


}

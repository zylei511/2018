package com.paic.crm.sdk.ucmcore.uihelper.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;
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
     * 表情名字的List集合
     */
    private List<String> faceImgNames = new ArrayList<>();

    /**
     * 表情图片的id的List集合
     */
    private List<Integer> faceImgIds = new ArrayList<>();

    /**
     * 表情符号的List集合
     */
    private List<String> faceSymbols = new ArrayList<>();

    /**
     * 储存bitmap的集合
     */
    private SparseArray<Bitmap> bitmaps = new SparseArray<>();

    /**
     * 匹配faceImgNames的正则表达式
     */
    private String namePattern;

    /**
     * 匹配faceSymbols的正则表达式
     */
    private String symbolPattern;


    public List<String> getFaceImgNames() {
        return faceImgNames;
    }

    protected void setFaceImgNames(List<String> faceImgNames) {
        this.faceImgNames = faceImgNames;
    }

    protected void setFaceImgNames(String[] names) {
        for (String name : names) {
            faceImgNames.add(name);
        }
    }

    public List<Integer> getFaceImgIds() {
        return faceImgIds;
    }

    protected void setFaceImgIds(List<Integer> faceImgIds) {
        this.faceImgIds = faceImgIds;
    }

    protected void setFaceImgIds(int[] ids) {
        for (int id : ids) {
            faceImgIds.add(id);
        }
    }

    public List<String> getFaceSymbols() {
        return faceSymbols;
    }

    protected void setFaceSymbols(List<String> faceSymbols) {
        this.faceSymbols = faceSymbols;
    }

    protected void setFaceSymbols(String[] symbols) {
        for (String symbol : symbols) {
            faceSymbols.add(symbol);
        }
    }

    public Pattern getNamePattern() {
        return TextUtils.isEmpty(namePattern) ? null : Pattern.compile(namePattern);
    }

    public void setNamePattern(String namePattern) {
        this.namePattern = namePattern;
    }

    public Pattern getSymbolPattern() {
        return TextUtils.isEmpty(symbolPattern) ? null : Pattern.compile(symbolPattern);
    }

    public void setSymbolPattern(String symbolPattern) {
        this.symbolPattern = symbolPattern;
    }

    /**
     * 初始化BaseFaceEntity
     *
     * @param context
     * @return
     */
    public abstract BaseFaceEntity init(Context context);


    /**
     * 将imageId 转化为bitmap，并放在集合中
     *
     * @param context
     */
    protected void initExpression(Context context) {
        for (int id : faceImgIds) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id);
            bitmaps.put(id, bitmap);
        }
    }


}

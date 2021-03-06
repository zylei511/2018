package com.paic.crm.inputhelper.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.paic.crm.inputhelper.entity.BaseFaceEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author ex-zhangyuelei001
 * @date 2017/12/12
 */

public class FaceUtil {
    private FaceUtil() {
    }

    public static FaceUtil getInstance() {
        return FaceUtilHolder.instance;
    }

    static class FaceUtilHolder {
        static FaceUtil instance = new FaceUtil();
    }

    private List<BaseFaceEntity> faceEntities = new ArrayList<>();

    public List<BaseFaceEntity> getFaceEntities() {
        return faceEntities;
    }

    public void setFaceEntities(@NonNull List<BaseFaceEntity> faceEntities) {
        this.faceEntities.addAll(faceEntities);
    }

    public SpannableString getExpression(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        SpannableString span = null;
        for (BaseFaceEntity faceEntity : getFaceEntities()) {
            span = getExpressionString(context, faceEntity, spannableString);
        }

        return span;
    }

    /**
     * 将SpannableString转换成表情
     *
     * @param context
     * @param faceEntity
     * @param spannableString
     * @return
     */
    private SpannableString getExpressionString(Context context, BaseFaceEntity faceEntity, SpannableString spannableString) {
        List<Integer> imageIds = faceEntity.getFaceImgIds();
        if (imageIds == null || imageIds.isEmpty()) {
            throw new IllegalArgumentException("faceImgIds can not is null");
        }
        //使用faceImgNames和namePattern进行解析
        List<String> faceImgNames = faceEntity.getFaceImgNames();
        Pattern namePattern = faceEntity.getNamePattern();
        if (namePattern != null && faceImgNames != null && !faceImgNames.isEmpty()) {
            dealExpression(context, faceImgNames, imageIds, spannableString, namePattern);
        }
        //使用faceSymbols和symbolPattern进行解析
        List<String> faceSymbols = faceEntity.getFaceSymbols();
        Pattern symbolPattern = faceEntity.getSymbolPattern();
        if (symbolPattern != null && faceSymbols != null && !faceSymbols.isEmpty()) {
            dealExpression(context, faceSymbols, imageIds, spannableString, namePattern);
        }

        return spannableString;
    }


    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param list
     * @param imageIds
     * @param spannableString
     * @param pattern
     * @return SpannableString
     */
    private SpannableString dealExpression(Context context, List<String> list, List<Integer> imageIds,
                                           SpannableString spannableString, Pattern pattern) {
        Matcher matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            if (matcher.start() < 0) {
                continue;
            }
            int index = list.indexOf(key);
            if (index < 0) {
                continue;
            }
            int imageId = imageIds.get(index);

            Drawable drawable = context.getResources().getDrawable(imageId);
            drawable.setBounds(0, 0, 60, 60);
            ImageSpan imageSpan = new ImageSpan(drawable);

            // 计算该图片名字的长度，也就是要替换的字符串的长度
            int end = matcher.start() + key.length();
            // 将该图片替换字符串中规定的位置中
            spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }
}

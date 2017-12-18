package com.example.zylei_library.uihelper.faceUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;


import com.example.zylei_library.uihelper.entity.BaseFaceEntity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ex-zhangyuelei001 on 2017/12/12.
 */

public class BaseFaceUtil {

    /**
     *将SpannableString转换成表情
     *
     * @param context
     * @param faceEntity
     * @param spannableString
     * @return
     */
    public SpannableString getExpressionString(Context context, BaseFaceEntity faceEntity, SpannableString spannableString) {
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

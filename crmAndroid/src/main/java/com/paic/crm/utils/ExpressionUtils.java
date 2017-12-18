package com.paic.crm.utils;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yueshaojun on 16/7/11.
 */
public class ExpressionUtils {

    public static SpannableString dispatchExpression(Context context,String input){
        SpannableString spannableString;
        String unicode = EmojiParser.getInstance(context).parseEmoji(input);
        spannableString = ParseEmojiMsgUtil.getExpressionString(context, unicode);
        FaceUtil.getExpressionString(context,spannableString);
        return spannableString;
    }
}

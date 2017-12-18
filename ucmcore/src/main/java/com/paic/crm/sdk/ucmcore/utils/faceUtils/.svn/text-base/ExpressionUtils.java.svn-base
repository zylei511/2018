package com.paic.crm.sdk.ucmcore.utils.faceUtils;

import android.content.Context;
import android.text.SpannableString;

import com.paic.crm.sdk.ucmcore.utils.CRMLog;

/**
 * Created by yueshaojun on 16/7/11.
 */
public class ExpressionUtils {

    public static SpannableString dispatchExpression(Context context,String input){
        SpannableString spannableString;
        long preTime = System.currentTimeMillis();
        String unicode = EmojiParser.getInstance(context).parseEmoji(input);
        long dert = System.currentTimeMillis() - preTime;
        CRMLog.LogInfo("ExpressionUtils","parseEmoji time::"+dert);

        preTime = System.currentTimeMillis();
        spannableString = ParseEmojiMsgUtil.getExpressionString(context, unicode);
        long dert1 = System.currentTimeMillis() - preTime;
        CRMLog.LogInfo("ExpressionUtils","ParseEmojiMsgUtil time::"+dert1);

        preTime = System.currentTimeMillis();
        FaceUtil.getExpressionString(context,spannableString);
        long dert2 = System.currentTimeMillis() - preTime;
        CRMLog.LogInfo("ExpressionUtils","FaceUtil time::"+dert2);
        return spannableString;
    }
}

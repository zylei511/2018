package com.paic.crm.sdk.ucmcore.utils.faceUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import com.paic.crm.sdk.ucmcore.R;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseEmojiMsgUtil {
	private static final String TAG = ParseEmojiMsgUtil.class.getSimpleName();
	private static final String REGEX_STR = "\\[e\\](.*?)\\[/e\\]";

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 */
	public static void dealExpression(Context context,
			SpannableString spannableString, Pattern patten, int start)
			throws Exception {
			Matcher matcher = patten.matcher(spannableString);
			while (matcher.find()) {
				String key = matcher.group();
				if (matcher.start() < start) {
					continue;
				}
				Field field = R.drawable.class
						.getDeclaredField("emoji_"
								+ key.substring(key.indexOf("]") + 1,
								key.lastIndexOf("[")));
				int resId = Integer.parseInt(field.get(null).toString());
				if (resId != 0) {
					Bitmap bitmap = BitmapFactory.decodeResource(
							context.getResources(), resId);
					Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
					drawable.setBounds(0, 0, 60, 60);
					ImageSpan imageSpan = new ImageSpan(drawable);
					int end = matcher.start() + key.length();
					spannableString.setSpan(imageSpan, matcher.start(), end,
							Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				}
		}
	}
	private static ExecutorService service= Executors.newFixedThreadPool(5);
	/**
	 * @desc <pre>
	 * 解析字符串中的表情字符串替换成表情图片
	 * </pre>
	 * @author Weiliang Hu
	 * @date 2013-12-17
	 * @param context
	 * @param str
	 * @return
	 */
	public static SpannableString getExpressionString(Context context,
			String str) {
		SpannableString spannableString = new SpannableString(str);
		Pattern pattern = Pattern.compile(REGEX_STR,Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, pattern, 0);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
		}
		return spannableString;
	}

	/**
	 * @desc <pre>
	 * 表情解析,转成unicode字符
	 * </pre>
	 * @author Weiliang Hu
	 * @date 2013-12-17
	 * @param cs
	 * @param mContext
	 * @return
	 */
	public static String convertToMsg(CharSequence cs, Context mContext) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
		ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
		for (int i = 0; i < spans.length; i++) {
			ImageSpan span = spans[i];
			String c = span.getSource();
			int a = ssb.getSpanStart(span);
			int b = ssb.getSpanEnd(span);
			Log.i("ccccc",c);
			if (c.contains("[")) {
//				if(FaceUtil.hasFace(c)){
//					Log.i("cccc",c);
//					return FaceUtil.convertMsgToFace(mContext,c,0);
//				}else {
					ssb.replace(a, b, convertUnicode(c));
//				}
			}
		}
		ssb.clearSpans();
		return ssb.toString();
	}

	private static String convertUnicode(String emo) {
		emo = emo.substring(1, emo.length() - 1);
		if (emo.length() < 6) {
			return new String(Character.toChars(Integer.parseInt(emo, 16)));
		}
		String[] emos = emo.split("_");
		char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
		char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
		char[] emoji = new char[char0.length + char1.length];
		for (int i = 0; i < char0.length; i++) {
			emoji[i] = char0[i];
		}
		for (int i = char0.length; i < emoji.length; i++) {
			emoji[i] = char1[i - char0.length];
		}
		return new String(emoji);
	}

}

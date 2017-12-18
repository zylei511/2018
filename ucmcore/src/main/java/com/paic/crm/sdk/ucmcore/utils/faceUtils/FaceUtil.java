package com.paic.crm.sdk.ucmcore.utils.faceUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.paic.crm.sdk.ucmcore.utils.CRMLog;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情工具类
 *
 *
 */
public class FaceUtil {
	private static final String QQ_FACE_NAME ="\\[[\\u4e00-\\u9fa5|A-Z]{1,}\\]";
	private static final String QQ_FACE_SYMBOL = "/::\\)" + "|/::~" + "|/::B" + "|/::\\|" + "|/:8\\-\\)" +
			"|/::<" + "|/::\\$" + "|/::X" + "|/::Z" + "|/::'\\(" + "|/::\\-\\|" + "|/::@" + "|/::P" +
			"|/::D" + "|/::O" + "|/::\\(" + "|/::\\+" + "|/:\\-\\-b" + "|/::Q" + "|/::T" + "|/:,@P" +
			"|/:,@\\-D" + "|/::d" + "|/:,@o" + "|/::g" + "|/:\\|\\-\\)" + "|/::!" + "|/::L" + "|/::>" +
			"|/::,@" + "|/:,@f" + "|/::\\-S" + "|/:\\?" + "|/:,@x" + "|/:,@@" + "|/::8" + "|/:,@!" +
			"|/:!!!" + "|/:xx" + "|/:bye" + "|/:wipe" + "|/:dig" + "|/:handclap" + "|/:&\\-\\(" +
			"|/:B\\-\\)" + "|/:<@" + "|/:@>" + "|/::\\-O" + "|/:>\\-\\|" + "|/:P\\-\\(" + "|/::'\\|" +
			"|/:X\\-\\)" + "|/::\\*" + "|/:@x" + "|/:8\\*" + "|/:pd" + "|/:<W>" + "|/:beer" + "|/:basketb" +
			"|/:oo" + "|/:coffee" + "|/:eat" + "|/:pig" + "|/:rose" + "|/:fade" + "|/:showlove" + "|/:heart" +
			"|/:break" + "|/:cake" + "|/:li" + "|/:bome" + "|/:kn" + "|/:footb" + "|/:ladybug" + "|/:shit" +
			"|/:moon" + "|/:sun" + "|/:gift" + "|/:hug" + "|/:strong" + "|/:weak" + "|/:share" + "|/:v" +
			"|/:@\\)" + "|/:jj" + "|/:@@" + "|/:bad" + "|/:ivu" + "|/:no" + "|/:ok" + "|/:love" + "|/:<L>" +
			"|/:jump" + "|/:shake" + "|/:<O>" + "|/:circle" + "|/:kotow" + "|/:turn" + "|/:skip" + "|/:oY" +
			"|/:#\\-0" + "|/:hiphot" + "|/:kiss" + "|/:<&" + "|/:&>";
	private static final String QQ_FACE_REGULAR = QQ_FACE_NAME +"|" +QQ_FACE_SYMBOL;
	/**
	 * 是否包含这个名字的情，只匹配一个
	 *
	 * @param name
	 * @return
	 */
	public static boolean hasFace(String name) {
		CRMLog.LogInfo("isHasFace", name);
		return MsgQQFaceUtils.faceImageNames.contains(name)||MsgQQFaceUtils.faceSymbol.contains(name);
	}
	/**
	 * 判断是否含有表情
	 * @param input
	 * @return
	 * */
	public static boolean isHasFace(String input){
		int start = input.indexOf("[");
		int end = input.indexOf("]");
		if(start!=-1&&end!=-1){
			String inputItem = input.substring(start,end+1);
			CRMLog.LogInfo("inputItem",inputItem);
			if(hasFace(inputItem)){
				CRMLog.LogInfo("inputItem",""+hasFace(inputItem));
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取表情名称的资源ID
	 *
	 * @param mContext
	 * @param name
	 * @return
	 */
	private static int getFaceResourceId(Context mContext, String name) {
		if (mContext == null) {
			throw new IllegalArgumentException("Null Context, can not find the ID");
		}
		int nameIndex = MsgQQFaceUtils.faceImageNames.indexOf(name);
		if(nameIndex == -1) {
			nameIndex = MsgQQFaceUtils.faceSymbol.indexOf(name);
		}
		return MsgQQFaceUtils.faceImageIds.get(nameIndex);
	}

	/**
	 * 根据索引获取表情名称
	 *
	 * @param index
	 * @return
	 */
	public static String getFacesName(int index) {
		if (index > MsgQQFaceUtils.faceImageNames.size() || index < 0) {
			return "";
		}
		return MsgQQFaceUtils.faceImageNames.get(index);
	}

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 *
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws NumberFormatException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static SpannableString dealExpression(Context context, SpannableString spannableString,
	                                             Pattern patten, int start)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			if (matcher.start() < start) {
				continue;
			}
			int resId = getFaceResourceId(context, key);
			if (resId != 0) {
				//这种方法由于进行IO操作会导致耗时。
//				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
				Bitmap bitmap = MsgQQFaceUtils.bitmaps.get(resId);
				//位图的缩放，也会导致耗时。
//				bitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, false);
				Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
				drawable.setBounds(0,0,60,60);
				ImageSpan imageSpan = new ImageSpan(drawable);

				int end = matcher.start() + key.length(); // 计算该图片名字的长度，也就是要替换的字符串的长度
				spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
			}
		}
		CRMLog.LogInfo("matcher","spannableString = "+spannableString);

		return spannableString;
	}


	public static void dealExpression(Context context, SpannableString spannableString,int start,int end,String faceStr) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {

		int resId = getFaceResourceId(context, faceStr);
		if (resId != 0) {
			ImageSpan imageSpan = new ImageSpan(context, resId);
			spannableString.setSpan(imageSpan, start, end+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中

		}
	}


	/**
	 * 得到丄1�7个SpanableString对象，�1�7�过传入的字符串,并进行正则判斄1�7
	 *
	 * @param context
	 * @param spannableString
	 * @return
	 */
	public static SpannableString getExpressionString(Context context, SpannableString spannableString) {

		Pattern namePattern = Pattern.compile(QQ_FACE_REGULAR, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString, namePattern, 0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		CRMLog.LogInfo("matcher","getExpressionString  spannableString = "+spannableString);

		return spannableString;
	}
	/**
	 * 把发送的文字转化成表情
	 * @param context
	 * @param start
	 * @param str
	 * */
	public static String convertMsgToFace(Context context, CharSequence str,int start) {
		StringBuffer sb = new StringBuffer(str);
		Pattern sinaPatten = Pattern.compile(QQ_FACE_NAME, Pattern.CASE_INSENSITIVE);
		Matcher matcher = sinaPatten.matcher(str);
		while(matcher.find()){
			String name = matcher.group();
			int mStart = matcher.start();
			int end = matcher.end();
			if(mStart<start){
				continue;
			}
			int faceIndex = MsgQQFaceUtils.faceImageNames.indexOf(name);
			sb.replace(mStart,end+1, MsgQQFaceUtils.faceImageNames.get(faceIndex));
		}
		return sb.toString();
	}

}

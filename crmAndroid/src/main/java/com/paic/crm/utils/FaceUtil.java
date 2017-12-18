package com.paic.crm.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;

import com.paic.crm.android.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表情工具类
 * 
 * 
 */
public class FaceUtil {
	public static final List<String> faceSymbol = new ArrayList<String>();
	private static final List<String> faceNames = new ArrayList<>();
	public static final String REGEX_EXP = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|(/::\\$)|/::X|/::Z|/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|(/:--b)|/::Q|/::T|/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";
	static {
		faceSymbol.add("占位笄1�7");
		faceSymbol.add("/::)");
		faceSymbol.add("/::~");
		faceSymbol.add("/::B");
		faceSymbol.add("/::|");
		faceSymbol.add("/:8-)");
		faceSymbol.add("/::<");
		faceSymbol.add("/::$");
		faceSymbol.add("/::X");
		faceSymbol.add("/::Z");
		faceSymbol.add("/::'(");
		faceSymbol.add("/::-|");
		faceSymbol.add("/::@");
		faceSymbol.add("/::P");
		faceSymbol.add("/::D");
		faceSymbol.add("/::O");
		faceSymbol.add("/::(");
		faceSymbol.add("/::+");
		faceSymbol.add("/:--b");
		faceSymbol.add("/::Q");
		faceSymbol.add("/::T");
		faceSymbol.add("/:,@P");
		faceSymbol.add("/:,@-D");
		faceSymbol.add("/::d");
		faceSymbol.add("/:,@o");
		faceSymbol.add("/::g");
		faceSymbol.add("/:|-)");
		faceSymbol.add("/::!");
		faceSymbol.add("/::L");
		faceSymbol.add("/::>");
		faceSymbol.add("/::,@");
		faceSymbol.add("/:,@f");
		faceSymbol.add("/::-S");
		faceSymbol.add("/:?");
		faceSymbol.add("/:,@x");
		faceSymbol.add("/:,@@");
		faceSymbol.add("/::8");
		faceSymbol.add("/:,@!");
		faceSymbol.add("/:!!!");
		faceSymbol.add("/:xx");
		faceSymbol.add("/:bye");
		faceSymbol.add("/:wipe");
		faceSymbol.add("/:dig");
		faceSymbol.add("/:handclap");
		faceSymbol.add("/:&-(");
		faceSymbol.add("/:B-)");
		faceSymbol.add("/:<@");
		faceSymbol.add("/:@>");
		faceSymbol.add("/::-O");
		faceSymbol.add("/:>-|");
		faceSymbol.add("/:P-(");
		faceSymbol.add("/::'|");
		faceSymbol.add("/:X-)");
		faceSymbol.add("/::*");
		faceSymbol.add("/:@x");
		faceSymbol.add("/:8*");
		faceSymbol.add("/:pd");
		faceSymbol.add("/:<W>");
		faceSymbol.add("/:beer");
		faceSymbol.add("/:basketb");
		faceSymbol.add("/:oo");
		faceSymbol.add("/:coffee");
		faceSymbol.add("/:eat");
		faceSymbol.add("/:pig");
		faceSymbol.add("/:rose");
		faceSymbol.add("/:fade");
		faceSymbol.add("/:showlove");
		faceSymbol.add("/:heart");
		faceSymbol.add("/:break");
		faceSymbol.add("/:cake");
		faceSymbol.add("/:li");
		faceSymbol.add("/:bome");
		faceSymbol.add("/:kn");
		faceSymbol.add("/:footb");
		faceSymbol.add("/:ladybug");
		faceSymbol.add("/:shit");
		faceSymbol.add("/:moon");
		faceSymbol.add("/:sun");
		faceSymbol.add("/:gift");
		faceSymbol.add("/:hug");
		faceSymbol.add("/:strong");
		faceSymbol.add("/:weak");
		faceSymbol.add("/:share");
		faceSymbol.add("/:v");
		faceSymbol.add("/:@)");
		faceSymbol.add("/:jj");
		faceSymbol.add("/:@@");
		faceSymbol.add("/:bad");
		faceSymbol.add("/:ivu");
		faceSymbol.add("/:no");
		faceSymbol.add("/:ok");
		faceSymbol.add("/:love");
		faceSymbol.add("/:<L>");
		faceSymbol.add("/:jump");
		faceSymbol.add("/:shake");
		faceSymbol.add("/:<O>");
		faceSymbol.add("/:circle");
		faceSymbol.add("/:kotow");
		faceSymbol.add("/:turn");
		faceSymbol.add("/:skip");
		faceSymbol.add("/:oY");
		faceSymbol.add("/:#-0");
		faceSymbol.add("/:hiphot");
		faceSymbol.add("/:kiss");
		faceSymbol.add("/:<&");
		faceSymbol.add("/:&>");
	}


	static {
		faceNames.add("占位笄1�7");
		faceNames.add("[微笑]");
		faceNames.add("[撇嘴]");
		faceNames.add("[色]");
		faceNames.add("[发呆]");
		faceNames.add("[得意]");
		faceNames.add("[流泪]");
		faceNames.add("[害羞]");
		faceNames.add("[闭嘴]");
		faceNames.add("[睡]");
		faceNames.add("[大哭]");
		faceNames.add("[尴尬]");
		faceNames.add("[发怒]");
		faceNames.add("[调皮]");
		faceNames.add("[呲牙]");
		faceNames.add("[惊讶]");
		faceNames.add("[难过]");
		faceNames.add("[酷]");
		faceNames.add("[冷汗]");
		faceNames.add("[抓狂]");
		faceNames.add("[吐]");
		faceNames.add("[偷笑]");
		faceNames.add("[愉快]");
		faceNames.add("[白眼]");
		faceNames.add("[傲慢]");
		faceNames.add("[饥饿]");
		faceNames.add("[困]");
		faceNames.add("[惊恐]");
		faceNames.add("[流汗]");
		faceNames.add("[憨笑]");
		faceNames.add("[悠闲]");
		faceNames.add("[奋斗]");
		faceNames.add("[咒骂]");
		faceNames.add("[疑问]");
		faceNames.add("[嘘]");
		faceNames.add("[晕]");
		faceNames.add("[疯了]");
		faceNames.add("[衰]");
		faceNames.add("[骷髅]");
		faceNames.add("[敲打]");
		faceNames.add("[再见]");
		faceNames.add("[擦汗]");
		faceNames.add("[抠鼻]");
		faceNames.add("[鼓掌]");
		faceNames.add("[糗大了]");
		faceNames.add("[坏笑]");
		faceNames.add("[左哼哼]");
		faceNames.add("[右哼哼]");
		faceNames.add("[哈欠]");
		faceNames.add("[鄙视]");
		faceNames.add("[委屈]");
		faceNames.add("[快哭了]");
		faceNames.add("[阴险]");
		faceNames.add("[亲亲]");
		faceNames.add("[吓]");
		faceNames.add("[可怜]");
		faceNames.add("[菜刀]");
		faceNames.add("[西瓜]");
		faceNames.add("[啤酒]");
		faceNames.add("[篮球]");
		faceNames.add("[乒乓]");
		faceNames.add("[咖啡]");
		faceNames.add("[饭]");
		faceNames.add("[猪头]");
		faceNames.add("[玫瑰]");
		faceNames.add("[凋谢]");
		faceNames.add("[嘴唇]");
		faceNames.add("[爱心]");
		faceNames.add("[心碎]");
		faceNames.add("[蛋糕]");
		faceNames.add("[闪电]");
		faceNames.add("[炸弹]");
		faceNames.add("[刀]");
		faceNames.add("[足球]");
		faceNames.add("[瓢虫]");
		faceNames.add("[便便]");
		faceNames.add("[月亮]");
		faceNames.add("[太阳]");
		faceNames.add("[礼物]");
		faceNames.add("[拥抱]");
		faceNames.add("[强]");
		faceNames.add("[弱]");
		faceNames.add("[握手]");
		faceNames.add("[胜利]");
		faceNames.add("[抱拳]");
		faceNames.add("[勾引]");
		faceNames.add("[拳头]");
		faceNames.add("[差劲]");
		faceNames.add("[爱你]");
		faceNames.add("[NO]");
		faceNames.add("[OK]");
		faceNames.add("[爱情]");
		faceNames.add("[飞吻]");
		faceNames.add("[跳跳]");
		faceNames.add("[发抖]");
		faceNames.add("[怄火]");
		faceNames.add("[转圈]");
		faceNames.add("[磕头]");
		faceNames.add("[回头]");
		faceNames.add("[跳绳]");
		faceNames.add("[投降]");
		faceNames.add("[激动]");
		faceNames.add("[乱舞]");
		faceNames.add("[献吻]");
		faceNames.add("[左太极]");
		faceNames.add("[右太极]");
	}

	/**
	 * 是否包含这个名字的情，只匹配一个
	 *
	 * @param name
	 * @return
	 */
	public static boolean hasFace(String name) {
		CRMLog.LogInfo("isHasFace", name);

		return faceSymbol.contains(name)||faceNames.contains(name);
	}
	/**
	 * 判断是否含有表情
	 * @param input
	 * @return
	 * */
	public static boolean isHasFace(String input){
//		CRMLog.LogInfo("isHasFace input ",input);
//		Pattern pattern = Pattern.compile("\\[[^\\x00-\\xff]{1,3}\\]");
//		Matcher matcher = pattern.matcher(input);
//		CRMLog.LogInfo("isHasFace output ",matcher.matches()+"");
//		return matcher.matches();
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
	public static int getFaceResourceId(Context mContext, String name) {
		if (mContext == null) {
			throw new IllegalArgumentException("Null Context, can not find the ID");
		}
		if(hasFace(name)) {
			name = "face" + faceSymbol.indexOf(name);
		}
			Field field = null;
			int resId = 0;
			try {
				field = R.drawable.class.getDeclaredField(name);
				resId = Integer.parseInt(field.get(null).toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			return resId;
			//return mContext.getResources().getIdentifier(name, "drawable", mContext.getPackageName());
	}

	/**
	 * 根据索引获取表情名称
	 *
	 * @param index
	 * @return
	 */
	public static String getFacesName(int index) {
		if (index > faceSymbol.size() || index < 0) {
			return "";
		}
		return faceSymbol.get(index);
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
	public static void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start) throws SecurityException, NoSuchFieldException, NumberFormatException, IllegalArgumentException, IllegalAccessException {
		Matcher matcher = patten.matcher(spannableString);
		while (matcher.find()) {
			String key = matcher.group();
			if (matcher.start() < start) {
				continue;
			}
			int resId = getFaceResourceId(context, key);
			if (resId != 0) {
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resId);
				Drawable drawable = new BitmapDrawable(context.getResources(),bitmap);
				drawable.setBounds(0,0,60,60);
				//bitmap = Bitmap.createScaledBitmap(bitmap, 60, 60, false);
				ImageSpan imageSpan = new ImageSpan(drawable);

				int end = matcher.start() + key.length(); // 计算该图片名字的长度，也就是要替换的字符串的长度
				Log.i("matcher",matcher.start()+"||"+end+"||"+key.length());
				spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); // 将该图片替换字符串中规定的位置中
				if(end<spannableString.length()){
					dealExpression(context,spannableString,patten,end);
				}
				break;
			}
		}
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

//		int start=str.indexOf("[");
//		int end=str.indexOf("]");
//		String faceStr=str.substring(start, end+1);
		//SpannableString spannableString = new SpannableString(str);
		Pattern pattern = Pattern.compile(REGEX_EXP, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(context, spannableString,pattern,0);
		} catch (Exception e) {
			Log.e("dealExpression", e.getMessage());
		}
		return spannableString;
	}
	/**
	 * 把发送的文字转化成表情
	 * @param context
	 * @param start
	 * @param str
	 * */
	public static String convertMsgToFace(Context context, String str,int start) {
		StringBuffer sb = new StringBuffer(str);
		Pattern sinaPatten = Pattern.compile("\\[[\\u4e00-\\u9fa5]{1,}\\]", Pattern.CASE_INSENSITIVE);
		CRMLog.LogInfo("matcher","length"+str.length()+"");
		Matcher matcher = sinaPatten.matcher(str);
		while(matcher.find()){
			String name = matcher.group();
			int mStart = matcher.start();
			int end = matcher.end();
			if(mStart<start){
				continue;
			}
			int faceIndex = faceNames.indexOf(name);
			sb.replace(mStart,end+1,faceSymbol.get(faceIndex));
			if(end<str.length()){
				convertMsgToFace(context,str,end);
			}
		}
		return sb.toString();
	}

}

package com.paic.crm.sdk.ucmcore.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.google.gson.Gson;
import com.paic.crm.sdk.ucmcore.utils.encrypt.Aes128CbcUtils;
import com.paic.crm.sdk.ucmcore.utils.encrypt.HMACSHA1Util;
import com.paic.crm.sdk.ucmcore.utils.encrypt.HmacSHA1Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Created by hanyh on 16/1/22.
 */
public class CommonUtils {

    private static final String TAG = "CommonUtils";
    private static final String CHARSET_NAME = "utf-8";
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }


    public static void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        final View decorView = activity.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                boolean hide = (double) displayHeight / height > 0.8;
                listener.onSoftKeyBoardChange(height - displayHeight, !hide);

            }
        });
    }


    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }


    /**
     * 将一个List按照固定的大小拆成很多个小的List
     *
     * @param listObj  需要拆分的List
     * @param groupNum 每个List的最大长度
     * @return 拆分后的List的集合
     */
    public static <T> List<List<T>> getSubList(List<T> listObj, int groupNum) {
        List<List<T>> resultList = new ArrayList<List<T>>();
        // 获取需要拆分的List个数
        int loopCount = (listObj.size() % groupNum == 0) ? (listObj.size() / groupNum)
                : ((listObj.size() / groupNum) + 1);
        // 开始拆分
        for (int i = 0; i < loopCount; i++) {
            // 子List的起始值
            int startNum = i * groupNum;
            // 子List的终止值
            int endNum = (i + 1) * groupNum;
            // 不能整除的时候最后一个List的终止值为原始List的最后一个
            if (i == loopCount - 1) {
                endNum = listObj.size();
            }
            // 拆分List
            List<T> listObjSub = listObj.subList(startNum, endNum);
            // 保存差分后的List
            resultList.add(listObjSub);
        }
        return resultList;

    }


    public static String getH5P(String key, String umId) {
//        String key = "83519aa6d30ecdc3";// "paic1234";
        String dec = null;
        String decrypt = null;
        long timestamp = new Date().getTime() + 1000 * 60 * 10;
        CRMLog.LogInfo(TAG, "CommonUtils"+timestamp + "");
        long nonce = new Random().nextInt(100000);
        String toSign = timestamp + "" + nonce;
        System.out.println("signature：" + toSign + "2");
        String signature = HmacSHA1Utils.getSignature(toSign, key);
        System.out.println("加密signature：" + signature + "3");
        signature = signature.replaceAll("\r|\n", "");
        System.out.println("加密signature：" + signature + "1");
        String content = "timestamp=" + timestamp + "&nonce=" + nonce + "&signature=" + signature + "&umId=" + umId;
        //&customerId=170B3FA426736D29E0530F0A1F0AA100";
        // 加密字符串
        // String content="abc";
        System.out.println("加密前的：" + content + "5");
        System.out.println("加密密钥：" + key);
        // 加密方法
        String enc = Aes128CbcUtils.encrypt(content, key);
        System.out.println("加密前的：" + enc + "4");
        enc = enc.replaceAll("\r|\n", "");
        //URL encode
        try {
            enc = URLEncoder.encode(enc, CHARSET_NAME);

            System.out.println("URL encode：" + enc);
            //URL decode
            dec = URLDecoder.decode(enc, CHARSET_NAME);
            System.out.println("URL decode：" + dec);
            // 解密方法
            decrypt = Aes128CbcUtils.decrypt(dec, key);
            System.out.println("解密后的内容：" + dec);
            System.out.println("解密后的内容：" + decrypt);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enc;
    }

    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager manager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            manager.getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception ex) {

        }
        return 1.0f;
    }

    public static int calcOfProportionWidth(Context context, int realTotalWidth, int realWidth) {
        return realWidth * getScreenWidth(context) / realTotalWidth;
    }

    public static int calcOfProportionHeight(Context context, int realTotalHeight, int realHeight) {
        return realHeight * getScreenHeight(context) / realTotalHeight;
    }

    /**
     * 获取当前应用版本名 [一句话功能简述]<BR>
     * [功能详细描述]
     *
     * @return
     */
    public static String getLocalVersionName(Context context) {
        String versionName = "";
        try {
            PackageInfo pinfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(),
                            PackageManager.GET_CONFIGURATIONS);
            versionName = "V" + pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取屏幕的高度
     *
     * @return 屏幕高度
     */
    public static int getDisplayHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }

    /**
     * 获取顶部状态栏高度
     *
     * @return 状态栏高度
     */
    public static int getStatusHeigth(Activity activity) {
        Rect rectgle = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rectgle);
        int statusBarHeight = rectgle.top;
        return statusBarHeight;
    }

    /**
     * 设置背景透明度
     *
     * @param activity
     * @param alpha
     */
    public static void setBackGroundAlpha(Activity activity, float alpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = alpha;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * 设置视图背景透明度
     *
     * @param v
     * @param alpha
     */
    public static void setViewBackGroundAlpha(View v, float alpha) {
        v.setAlpha(alpha);
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) context
                .getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;

    }

    public static <T> T handleHttpResult(Class<T> cls, Object obj) {

        try {
            String objs = (String) obj;
            T t = new Gson().fromJson(objs, cls);
            return t;
        } catch (Exception e) {
            CRMLog.LogInfo(TAG,"CommonUtils"+ "gson 解析异常:   " + e.getMessage());
            return null;
        }

    }


    // 隐藏软键盘
    public static void hideInputManager(Context ct) {
        try {
            ((InputMethodManager) ct.getSystemService(ct.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct)
                    .getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            Log.e("test", "hideInputManager Catch error,skip it!", e);
        }
    }


    public static void hideInputManager(Context ct, View v) {
        if (null != v) {
            ((InputMethodManager) ct.getSystemService(ct.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            CRMLog.LogInfo("hideInputManager", "hide defeat");
        }
    }
    /**
     * 生成消息ID
     *
     * @return
     */
    public static String generateMsgId() {

        String msgHeader = "m";
        long timeStamp = System.currentTimeMillis();
        int radomNumber = randomNumber();
        String msgId = msgHeader + timeStamp + radomNumber;

        return msgId;
    }

    /**
     * 生成随机数
     *
     * @return
     */
    public static int randomNumber() {

        Random random = new Random();
        int max = 99999999;
        int min = 10000000;
        int s = random.nextInt(max) % (max - min + 1) + min;
        System.out.println(s);
        return s;
    }


    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {


        int i;
        int j;
        if (bmp.getHeight() > bmp.getWidth()) {
            i = bmp.getWidth();
            j = bmp.getWidth();
        } else {
            i = bmp.getHeight();
            j = bmp.getHeight();
        }

        Bitmap localBitmap = Bitmap.createBitmap(i, j, Config.RGB_565);
        Canvas localCanvas = new Canvas(localBitmap);

        while (true) {
            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i, j), null);
            if (needRecycle)
                bmp.recycle();
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                    localByteArrayOutputStream);
            localBitmap.recycle();
            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
            try {
                localByteArrayOutputStream.close();
                return arrayOfByte;
            } catch (Exception e) {
                //F.out(e);
            }
            i = bmp.getHeight();
            j = bmp.getHeight();
        }
    }

    /**
     * 获取设备Id
     *
     * @param
     * @return
     */
    public static String getUniquePsuedoID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();

    }

    private static CommonUtils commonUtils;

    private CommonUtils() {

    }

    public static Bitmap getRoundCornerImage(Bitmap bitmap_bg, Bitmap bitmap_in) {
        Bitmap roundConcerImage = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, 500, 500);
        Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap_in, rectF, rect, paint);
        return roundConcerImage;
    }

    public static Bitmap getShardImage(Bitmap bitmap_bg, Bitmap bitmap_in) {
        Bitmap roundConcerImage = Bitmap.createBitmap(500, 500, Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConcerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, 500, 500);
        paint.setAntiAlias(true);
        NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
        patch.draw(canvas, rect);
        Rect rect2 = new Rect(2, 2, 498, 498);
        canvas.drawBitmap(bitmap_in, rect, rect2, paint);
        return roundConcerImage;
    }

    public static String getPhotopath(Context context) {

        File parent = null;
        //如果手机挂载sdCard就用sdCard的外层目录
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            parent = context.getExternalCacheDir();
        } else {//否则就用应用的缓存目录

            parent = context.getCacheDir();
        }
        CRMLog.LogInfo(TAG,"CommonUtils"+"fileName parent  " + parent);
        DateFormat df = new DateFormat();

        String name = df.format("yyyyMMdd_hhmmss",
                Calendar.getInstance(Locale.CHINA))
                + ".png";
        String fileName = parent + name;
        File file = new File(parent, fileName);
        CRMLog.LogInfo(TAG,"CommonUtils"+ "fileName   " + fileName);
        if (!file.exists()) {
            Log.e("TAG", "第一次创建文件夹");
            file.mkdirs();// 如果文件夹不存在，则创建文件夹
        }

        return fileName;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//设置为ture,只读取图片的大小，不把它加载到内存中去
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);//此处，选取了480x800分辨率的照片

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;//处理完后，同时需要记得设置为false

        return BitmapFactory.decodeFile(filePath, options);
    }

    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 判断service是否运行
     *
     * @param className
     * @param context
     * @return
     */
    public static boolean isServiceWorked(String className, Context context) {
        ActivityManager myManager = (ActivityManager) context
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager
                .RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 计算表格布局高度
     *
     * @param gridView
     */
    public static void setListViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            // 利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            // 利用反射，取得横向分割线高度
            CRMLog.LogInfo(TAG,"CommonUtils"+"total columns:   " + columns);
            Field horizontalSpacing = clazz
                    .getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        CRMLog.LogInfo(TAG,"CommonUtils"+ "total rows:   " + rows);
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { // 只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1);// 最后加上分割线总高度
        gridView.setLayoutParams(params);


        CRMLog.LogInfo(TAG,"CommonUtils"+ "total height:   " + totalHeight);

    }

    public static String createToken(String key, String systemId,
                                     long expiryDate) throws NoSuchAlgorithmException {
        String strVal = key + systemId + expiryDate;
        MessageDigest md = MessageDigest.getInstance("SHA");
        try {
            md.update(strVal.getBytes("GBK"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteVal = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0, l = byteVal.length; i < l; i++) {
            // byte b = byteVal[i];
            String h = Integer.toHexString(byteVal[i] & 0xFF);
            if (h.length() == 1) {
                h = "0" + h;
            }
            sb.append(h);
        }
        return sb.toString();
    }

    /*
   时间戳
*/
    public static String getTimeStamp() {

        long times = System.currentTimeMillis();

        return String.valueOf(times);
    }

    /*
  * 返回长度为【strLength】的随机数，在前面补0
  */
    public static String getNonceString(int strLength) {

        Random rm = new Random();

        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);

        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);

        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public static String getSignature(String time, String once, String addonsKey) {

        String signature = null;
        String msg = time + once;
        signature = HMACSHA1Util.getHmacSHA1(msg, addonsKey);

        return signature;
    }


    public static Uri imageUri = Uri.parse(Environment.getExternalStorageDirectory() + "/picture");

    /**
     * 图片裁剪
     *
     * @param data
     * @return
     */
    public static Intent getCropImageIntent(Bitmap data) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.putExtra("data", data);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", true);
        return intent;
    }

    //获取图片
    public static Uri startPhotoZoom(Context context, Uri uri) {

        Uri mUri = null;
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (FileNotFoundException e) {
            CRMLog.LogInfo(TAG,"CommonUtils"+"FileNotFoundException   " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            CRMLog.LogInfo(TAG,"CommonUtils"+ "IOException   " + e.getMessage());
        }

        //这里做了判断  如果图片大于 512KB 就进行压缩
        if (bitmap.getRowBytes() * bitmap.getHeight() > 512 * 1024) {//压缩图片
            bitmap = compressImage(bitmap, 50);
            mUri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
        }

        return mUri;

    }


    public static Uri startPhotoZoom(Context context, Bitmap bm) {

        Uri mUri = null;
        Bitmap bitmap = bm;


        //这里做了判断  如果图片大于 512KB 就进行压缩
        if (bitmap.getRowBytes() * bitmap.getHeight() > 512 * 1024) {
            bitmap = compressImage(bitmap, 50);
            mUri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
        }

        return mUri;

    }

    //图片压缩
    private static Bitmap compressImage(Bitmap bitmap, int quality) {

        Bitmap bitmap1 = bitmap;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 因为质量压缩不是可以无限缩小的，所以一张高质量的图片，再怎么压缩，
        // 最终size可能还是大于你指定的size，造成异常
        // 所以不建议循环压缩，而是指定quality，进行一次压缩就可以了
        bitmap1.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        CRMLog.LogInfo(TAG,"CommonUtils"+ "原始大小：" + baos.toByteArray().length);
        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(
                baos.toByteArray(), 0, baos.toByteArray().length);
        return compressedBitmap;
    }


    public static File getFile(Bitmap bitmap) {
        String pictureDir = "";
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;
        File file = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();
            String saveDir = Environment.getExternalStorageDirectory()
                    + "/dreamtownImage";
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            file = new File(saveDir, UUID.randomUUID().toString() + ".jpg");
            file.delete();
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            pictureDir = file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
            CRMLog.LogInfo(TAG,"CommonUtils"+ "getFile Exception " + e.getMessage());
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        return file;
    }


    /**
     * 消息初始化的时候的json参数的生成
     *
     * @param key
     * @param word
     * @return
     */
    public static String convertJsonKeyWords(String key, String word) {

        JSONObject object = new JSONObject();
        try {
            object.put(key, word);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return object.toString();
    }

    /***
     * check activity is on foreground or not
     *
     * @param context
     * @return
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(context.getPackageName())) {
            return true;
        }

        return false;
    }

    /***
     * 检查特定的activity是不是在前台。
     *
     * @return
     */
    public static boolean isTargetRunningForeground(Context context,List<String> targetActivityNames) {
//        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
//        CRMLog.LogInfo(TAG,"isTargetRunningForeground:-->"+cn.getClassName());
//        String[] strs = cn.getClassName().split("\\.");
//        String topActivityName = strs[strs.length-1];
        String topActivityName =  ((Activity)context).getClass().getSimpleName();
        CRMLog.LogInfo("FloatViewHelper","isTargetRunningForeground:-->"+topActivityName);
        if (!TextUtils.isEmpty(topActivityName) && targetActivityNames.contains(topActivityName)) {
            return true;
        }

        return false;
    }

	/**
     * 获取网络运营商信息
     * @param context
     * @return
     */
    public static String getMso(Context context) {
        String mso = null;
        TelephonyManager telManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

        String operator = telManager.getSimOperator();

        if (operator != null)

        {
            if (operator.equals("46000") || operator.equals("46002") || operator.equals("46007")) {

                mso = "00";//中国移动

            } else if (operator.equals("46001")) {

                mso = "01";//中国联通

            } else if (operator.equals("46003")) {

                mso = "03";//中国电信

            }else {
                mso="20";
            }
            return mso;
        }
        return "20";
    }

}

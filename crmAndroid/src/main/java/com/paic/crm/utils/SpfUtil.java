package com.paic.crm.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.pingan.paimkit.common.userdata.PMDataManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * SharedPreference 工具类
 *
 * @author hanyh
 * @date 2013-9-9
 * @time 上午11:42:54
 */
@SuppressLint("WorldReadableFiles")
public class SpfUtil {



    private static final String NAME = "crm_sp";

    public static final String SHIROKEY="shiroKey";
    public static final String UMID="umId";
    public static final String UM_NAME="um_name";
    public static final String UM_SEX="um_sex";
    public static final String UM_PHONE="um_phone";
    public static final String UM_EXTENSIONNUMBER="extensionNumber";
    public static final String BIZSERIES="bizseries";
    public static final String PAD_BIZ_SERIES="PAD_BIZ_SERIES";
    public static final String LOGIN_FLAG="login_flag";
    public static final String IM_FLAG="IM";
    public static final String EMAIL_FLAG="email";
    public static final String MSG_FLAG="notMessage";
    public static final String WEIXIN_FLAG="weiXin";
    public static final String WORLD_FLAG="world";
    public static final String WEB_FLAG="web";
    public static final String MSO_FLAG="mso";//
    public static final String UCP_CODE_FLAG="ucp_code";//
    public static final String BIZ_CHANNEL="biz_channel";//
    public static final String APP_STATUS="appStatus";//
    public static final String VIBRATE_STATE = "vibrateState";
    public static final String SOUND_STATE = "soundState";
    public static final String KEYBORAD_HEIGHT = "keyboardHeight";
    public static final String STATUS_KZKF = "status";
    public static final String CHECK_IN_TIME_KZKF = "check_in_time_kzkf";
    /* 刷新数据时间 */
    private static final String KEY_SYNC_DATA_TIME = "key_sync_data_tiem";


    public static final String KEY_SYNC_SERVER_TIME_T = "key_sync_server_t";// 当前本地时间与服务器时间戳差值

    /* 拉取好友本地时间戳记录 */
    private static final String KEY_LAST_QUERY_FRIENDS_TIME = "key_last_query_friends_flag";


    /**
     * 添加我为好友需要验证设置开关
     */
    private static final String KEY_ADD_FRIEND_VALIDATE_SETTING = "key_add_friend_validate_setting";

    /**
     * 是否需要上传通讯录,用户通讯录修改后标记
     */
    private static final String KEY_NEED_UPLOAD_PHONECONTACT = "key_need_Upload_phonecontact";

    /**
     * 设置SharePreference文件中的字段的值
     *
     * @param ctx 上下文
     * @param key 字段
     * @param value 值
     */
    @SuppressLint("WorldWriteableFiles")
    public static boolean setValue(Context ctx, String key, Object value) {
        boolean status = false;
        SharedPreferences spf = null;
        if (null == ctx || null == key || null == value) {
            return false;
        }
        spf = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String type = value.getClass().getSimpleName();// 获取数据类型
        SharedPreferences.Editor editor = spf.edit();
        if (spf != null) {
            if ("String".equals(type)) {
                editor.putString(key, (String) value);
            } else if ("Integer".equals(type)) {
                editor.putInt(key, (Integer) value);
            } else if ("Boolean".equals(type)) {
                editor.putBoolean(key, (Boolean) value);
            } else if ("Long".equals(type)) {
                editor.putLong(key, (Long) value);
            } else if ("Float".equals(type)) {
                editor.putFloat(key, (Float) value);
            }
            status = editor.commit();
        }
        return status;
    }

    /**
     * 获得SharePreference的值
     *
     * @param ctx 上下文
     * @param key 字段
     * @param defValue 默认值
     * @return 获得对应key的值
     */
    @SuppressLint("WorldWriteableFiles")
    public static Object getValue(Context ctx, String key, Object defValue) {
        SharedPreferences spf = null;
        spf = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String type = defValue.getClass().getSimpleName();// 获取数据类型
        if (spf != null) {
            if (type.equals("String")) {
                return spf.getString(key, (String) defValue);
            } else if (type.equals("Integer")) {
                return spf.getInt(key, (Integer) defValue);
            } else if (type.equals("Boolean")) {
                return spf.getBoolean(key, (Boolean) defValue);
            } else if (type.equals("Long")) {
                return spf.getLong(key, (Long) defValue);
            } else if (type.equals("Float")) {
                return spf.getFloat(key, (Float) defValue);
            }
        }
        return null;
    }

    /**
     * 将所有SharedPreferences文件删除
     */
    public static void clearAllShare(Context ctx) {

    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
//        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     *
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({ "unchecked", "rawtypes" })
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

    /**
     * 将SharedPreferences转化成xml格式的字符串
     *
     * @param spName SharedPreferences文件的名字
     * @return xml根式的字符串
     */

    @SuppressLint("SdCardPath")
    public static String SharedPreferencesTransfromXmlString(String spName) {
        String path = "/data/data/com.pingan.mobilewallet/shared_prefs/" + spName + ".xml";
        File file = new File(path);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            if (fileInputStream != null) {
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine = new String("");
                while ((sTempOneLine = tBufferedReader.readLine()) != null) {
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * @mark 获取本地与服务器的时间差
     * @author 陈大龙
     * @date 2015-3-27
     * @time 下午5:02:08
     * @return String
     */
    public static String getSyncServerTKey() {
        return PMDataManager.getInstance().getUsername() + KEY_SYNC_SERVER_TIME_T;
    }



    /**
     * 获取被添加好友需要验证开关
     *
     * @param username
     * @return
     */
    public static String getAddFriendValidate(String username) {
        return username + KEY_ADD_FRIEND_VALIDATE_SETTING;
    }

    /**
     * 获取是否需要上传通讯录,用户通讯录修改后标记
     *
     * @param username
     * @return
     */
    public static String getNeedUploadPhoneContact(String username) {
        return username + KEY_NEED_UPLOAD_PHONECONTACT;
    }

    /**
     * 获取服务模块数据key
     */
    private static String KEY_SERVICE_DATA = "key_service_data";

    /**
     * 获取服务模块数据
     *
     * @param username
     * @return
     */
    public static String getServiceDataKey(String username) {
        return username + KEY_SERVICE_DATA;
    }

    /**
     * 获取服务模块数据最后时间key
     */
    public static String KEY_SERVICE_DATA_LAST_TIME = "key_service_data_last_time";

    /**
     * 获取服务模块数据最后时间key
     *
     * @param username
     * @return
     */
    public static String getServiceDataLastTimeKey(String username) {
        return username + KEY_SERVICE_DATA_LAST_TIME;
    }
}

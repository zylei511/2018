package com.paic.crm.sdk.ucmcore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

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

    /**
     * 设置SharePreference文件中的字段的值
     *
     * @param ctx   上下文
     * @param key   字段
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
     * @param ctx      上下文
     * @param key      字段
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
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
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
    public static String SharedPreferencesTransfromXmlString(Context ctx,String spName) {
        String path = "/data/data/"+ctx.getPackageName()+"/shared_prefs/" + spName + ".xml";
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
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


}

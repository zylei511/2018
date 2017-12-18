package com.paic.crm.utils;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Iterator;

public final class NetUtils {
    private final static String TAG = "NetUtils";

    public static String getNetworkType(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null ? info.getTypeName() : null;
    }

    public static String getWifiMacAddress(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = manager.getConnectionInfo();
            return null != wifiInfo ? wifiInfo.getMacAddress() : null;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        final ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo();
    }

    public static boolean isMobileDataConnected(NetworkInfo activeNetwork) {
        return isNetworkConnected(activeNetwork)
                && ConnectivityManager.TYPE_MOBILE == activeNetwork.getType();
    }

    public static boolean isMobileDataConnected(Context context) {
        return isMobileDataConnected(getActiveNetworkInfo(context));
    }

    public static boolean isWifiConnected(NetworkInfo activeNetwork) {
        return isNetworkConnected(activeNetwork)
                && ConnectivityManager.TYPE_WIFI == activeNetwork.getType();
    }

    public static boolean isWifiConnected(Context context) {
        return isWifiConnected(getActiveNetworkInfo(context));
    }

    public static boolean isWifiAvailable(Context context) {
        return isWifiAvailable(getActiveNetworkInfo(context));
    }

    public static boolean isWifiAvailable(NetworkInfo activeNetwork) {
        return isNetworkAvailable(activeNetwork)
                && ConnectivityManager.TYPE_WIFI == activeNetwork.getType();
    }

    public static boolean isNetworkConnected(NetworkInfo activeNetwork) {
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean isNetworkConnected(Context context) {
        return isNetworkConnected(getActiveNetworkInfo(context));
    }

    /**
     * Check whether network is available.
     *
     * <p>
     * 不同于{@link #isNetworkConnected}, 网络正在连接和连接上都会返回<code>true</code>
     * <p>
     *
     * @param context Application context
     * @return <code>true</code> if network available, <code>false</code> otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        return isNetworkAvailable(getActiveNetworkInfo(context));
    }

    public static boolean isNetworkAvailable(NetworkInfo activeNetwork) {
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Returns the default link's IP addresses, if any, taking into account IPv4 and IPv6 style
     * addresses.
     * @param context the application context
     * @return the formatted and newline-separated IP addresses, or null if none.
     *
     *         192.168.0.2
     *         fe80:d222:beff:fe2b:a9fb
     */
//    public static String getDefaultIpAddresses(Context context) {
//        return formatIpAddresses(getActiveLinkProperties(context));
//    }

    /**
     * Returns the default link's IPv4 addresse
     * @param context the application context
     * @return the formatted IPv4 address, or null if none.
     */
//    public static String getDefaultIpv4Address(Context context) {
//        return formatIpv4Address(getActiveLinkProperties(context));
//    }

//    private static LinkProperties getActiveLinkProperties(Context context) {
//        ConnectivityManager cm = (ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        n_net_try {
//            Method m = ConnectivityManager.class
//                    .getDeclaredMethod("getActiveLinkProperties", (Class<?>[]) null);
//            m.setAccessible(true);
//            return (LinkProperties) m.invoke(cm, (Object[]) null);
//        } catch (Exception ignored) {
//            return null;
//        }
//    }

//    private static String formatIpv4Address(LinkProperties prop) {
//        if (prop == null) return null;
//        Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
//        // If there are no entries, return null
//        if (!iter.hasNext()) return null;
//        return iter.next().getHostAddress();
//    }
//
//    private static String formatIpAddresses(LinkProperties prop) {
//        if (prop == null) return null;
//        Iterator<InetAddress> iter = prop.getAllAddresses().iterator();
//        // If there are no entries, return null
//        if (!iter.hasNext()) return null;
//        // Concatenate all available addresses, comma separated
//        StringBuilder addresses = new StringBuilder();
//        while (iter.hasNext()) {
//            addresses.append(iter.next().getHostAddress());
//            if (iter.hasNext()) addresses.append('\n');
//        }
//        return addresses.toString();
//    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wfm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wfm.isWifiEnabled();
    }

    public static boolean toggleWifiEnabled(Context context) {
        try {
            WifiManager wfm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wfm.setWifiEnabled(!wfm.isWifiEnabled());
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean setWifiEnabled(Context context, boolean enabled) {
        try {
            WifiManager wfm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            return wfm.setWifiEnabled(enabled);
        } catch (Exception ignored) {
        }
        return false;
    }

    public static boolean isMobileDataEnabled(Context context){
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            return (Boolean) cm.getClass()
                    .getMethod("getMobileDataEnabled", (Class[]) null)
                    .invoke(cm, (Object[]) null);
        } catch (Throwable e) {
            Log.w(TAG, "Failed to execute getMobileDataEnabled", e);
        }
        return false;
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) {
        try {
            ConnectivityManager cm = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);

            Field mServiceFiled = cm.getClass().getDeclaredField("mService");
            mServiceFiled.setAccessible(true);
            Object service = mServiceFiled.get(cm);

            Method setMobileDataEnabled = service.getClass()
                    .getDeclaredMethod("setMobileDataEnabled", boolean.class);
            setMobileDataEnabled.setAccessible(true);
            setMobileDataEnabled.invoke(service, enabled);
        } catch (Throwable e) {
            Log.w(TAG, "Failed to execute setMobileDataEnabled", e);
        }
    }

    public static char getKeyD() {
        return 'z';
    }

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_2G = 2;
    public static final int TYPE_3G = 3;
    public static final int TYPE_4G = 4;

    // 3G类型，http://zh.wikipedia.org/wiki/TD-SCDMA.
    // Android SDK 21里的TelephonyManager.NETWORK_TYPE_XXX只到16，在此处添加常量
    public static final int NETWORK_TD_SCDMA = 17;

    // 特殊机型特有的3G网络类型，例如华为荣耀。
    // Android SDK 21里的TelephonyManager.NETWORK_TYPE_XXX只到16，在此处添加常量
    public static final int NETWORK_HUAWEI_TDS_HSDPA = 18;

    public static String getCategorizedSubTypeName(Context context) {
        return getCategorizedSubTypeName(context, "NA");
    }

    public static String getCategorizedSubTypeName(Context context, String defName) {
        return getCategorizedSubTypeName(context, defName, getCategorizedSubType(context));
    }

    public static String getCategorizedSubTypeName(Context context, String defName, int type) {
        switch (type) {
            case TYPE_2G:
                return "2G";
            case TYPE_3G:
                return "3G";
            case TYPE_4G:
                return "4G";
            case TYPE_WIFI:
                return "WIFI";
            case TYPE_UNKNOWN:
                return "NA";
            default:
                return defName;
        }
    }

    public static int getCategorizedSubType(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (info != null && info.isConnectedOrConnecting()) {
            return TYPE_WIFI;
        } else {
            info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (null != info && info.isAvailable() && info.isConnectedOrConnecting()) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return TYPE_2G;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case NETWORK_TD_SCDMA:
                    case NETWORK_HUAWEI_TDS_HSDPA:
                        return TYPE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return TYPE_4G;
                    default:
                        break;
                }
            }
        }
        return TYPE_UNKNOWN;
    }
}

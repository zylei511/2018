package com.paic.crm.module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.paic.crm.android.BuildConfig;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.net.VolleyRequest;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Constants;
import com.paic.crm.utils.DownLoadManager;
import com.paic.crm.utils.HttpUrls;
import com.paic.crm.utils.SpfUtil;
import com.paic.crm.widget.AlertView;
import com.paic.crm.widget.OnItemClickListener;

import org.json.JSONObject;

import java.io.File;
import java.util.regex.Pattern;

/**
 * 与视图密切相关的功能模块类
 * Created by yueshaojun on 16/9/8.
 */
public class Update {
    private static Context mContext;
    public Update(Context context) {
        this.mContext = context;
    }

    public void versionUpdate(final boolean openPrompt, final UpdateOverListener updateOverListener) {

            String umId = (String) SpfUtil.getValue(mContext, SpfUtil.UMID, "");
            String shiroKey = SpfUtil.getValue(mContext, SpfUtil.SHIROKEY, "").toString();

            if (shiroKey != null && !shiroKey.equals("")) {
                CRMLog.LogError(Constants.LOG_TAG, "shiroKey----->" + shiroKey);
                VolleyRequest.httpVersionUpdata(mContext, HttpUrls.HTTP_VERSION_UPDATA, umId, shiroKey, new VolleyInterface() {
                    @Override
                    public void onSuccess(Object obj) {
                        if(updateOverListener!=null) {
                            updateOverListener.onOver();
                        }
                        try {
                            String ver = BuildConfig.VERSION_NAME;
                            JSONObject object = new JSONObject(obj.toString());
                            String resultCode = object.getString("resultCode");
                            if (resultCode.equals("200")) {
                                JSONObject data = (JSONObject) object.get("data");
                                String versionNumber = data.getString("versionNumber");
                                String androidDownloadUrl = data.getString("androidDownloadUrl");
                                if (!ver.equals(versionNumber)) {
                                    showUpdataDialog(androidDownloadUrl);
                                } else if (openPrompt) {
                                    Toast.makeText(mContext,"已经是最新版本！",Toast.LENGTH_LONG).show();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError ve) {
                        if(updateOverListener!=null) {
                            updateOverListener.onOver();
                        }
                    }

                    @Override
                    public void onLogOutside() {

                    }
                });
            }
        }

    protected void showUpdataDialog(final String downLoadUrl) {
//        AlertDialog.Builder builer = new AlertDialog.Builder(this);
//        builer.setTitle("版本升级");
//        builer.setMessage("有最新的软件包哦,亲快下载吧~\"");
//        // 当点确定按钮时从服务器上下载 新的apk 然后安装 װ
//        builer.setPositiveButton("现在升级", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                downLoadApk(downLoadUrl);
//            }
//        });
//        builer.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog dialog = builer.create();
//        dialog.show();
            AlertView alertView=new AlertView("版本升级", "有最新的软件包哦,亲快下载吧~", null, new String[]{"确定"}, null, mContext, AlertView.Style.Alert, new OnItemClickListener() {
                @Override
                public void onItemClick(Object o, int position) {
                    downLoadApk(downLoadUrl);
                }
            }).setCancelable(false);
            alertView.show();
        }

    protected void downLoadApk(final String downLoadUrl) {
            final ProgressDialog pd; // 进度条对话框
            pd = new ProgressDialog(mContext);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);
            pd.setMessage("正在下载更新");
            pd.show();
            new Thread() {
                @Override
                public void run() {
                    try {
                        File file = DownLoadManager.getFileFromServer(downLoadUrl,
                                pd);
                        sleep(3000);
                        installApk(file);
                        pd.dismiss(); // 结束掉进度条对话框
                    } catch (Exception e) {
                    }
                }
            }.start();
        }

        // 安装apk
    protected void installApk(File file) {
            Intent intent = new Intent();
            // 执行动作
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 执行的数据类型
            intent.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }

    private String getVersionName() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                0);

        return packInfo.versionName;
    }

    private int getVersionCode() throws Exception {
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageManager packageManager = mContext.getPackageManager();
        PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(),
                0);

        return packInfo.versionCode;
    }


    public static boolean isVersion(String version) {
        if (version == null)
            return false;
        return Pattern.matches("^[0-9]{1,2}+(.[0-9]{1,2}){0,2}$", version);
        // return Pattern.matches("^\\d+(\\.\\d+){0,2}$", version);
    }

    public static boolean isNewVersion(String version, String lastVersion) {
        if (lastVersion == null) {
            return true;
        }
        if (!isVersion(version) || !isVersion(lastVersion)) {
            return false;
        }
        String[] now = toFullVersion(version).split("\\.");
        String[] last = toFullVersion(lastVersion).split("\\.");
        int v1 = Integer.valueOf(now[0]);
        int v2 = Integer.valueOf(now[1]);
        int v3 = Integer.valueOf(now[2]);
        int l1 = Integer.valueOf(last[0]);
        int l2 = Integer.valueOf(last[1]);
        int l3 = Integer.valueOf(last[2]);
        return (v1 > l1) || (v1 == l1 && v2 > l2) || (v1 == l1 && v2 == l2 && v3 > l3);
    }


    public static String toFullVersion(String version) {
        if (version.contains(".")) {
            if (version.replaceFirst("\\.", "").contains(".")) {// 两个点以上
                return version;
            } else {// 一个点
                return "0." + version;
            }
        } else {// 没有点
            return "0.0." + version;
        }
    }

    public interface UpdateOverListener{
        void onOver();
    }
}

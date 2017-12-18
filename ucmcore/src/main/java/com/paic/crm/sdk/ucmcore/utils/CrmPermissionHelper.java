package com.paic.crm.sdk.ucmcore.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.paic.crm.sdk.ucmcore.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ex-lisuyang001 on 17/4/7.
 */

public class CrmPermissionHelper {
    private static final String TAG = "CrmPermissionHelper";
    private AskPermissionCallBack mCallBack = null;
    public interface AskPermissionCallBack {
        void onSuccess(int requestCode);

        void onFailed();
    }

    private Activity mContext;
    private PromptDialog mPromptDialog;
    private String[] mPermissionNames;
    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            if(position==0){
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + mContext.getPackageName()));
                mContext.startActivity(intent);
            }else {
                if (mCallBack != null) {
                    mCallBack.onFailed();
                }
            }
        }
    };
    public CrmPermissionHelper(Activity context,AskPermissionCallBack callBack){
        mContext = context;
        mCallBack = callBack;
        mPromptDialog = DialogFactory.getPromptDialog(mContext,mItemClickListener);
    }
    public void checkPermission(int requestCode,String[] permissions,String[] permissionNames) {
        List<String>  unGrantedList = new ArrayList<>();
        mPermissionNames = permissionNames;
        for(int i = 0 ; i <permissions.length;i++) {
            if (ContextCompat.checkSelfPermission(mContext, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                //没有权限，申请权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(mContext, permissions[i])) {
                    //以前被拒绝授予权限，而且不再提示
                    CRMLog.LogInfo("lsy", "以前被拒绝授予权限，而且不再提示");
                    mPromptDialog.setTitle("app需要开启权限才能使用"+permissionNames[i]+"功能");
                    mPromptDialog.show();

                } else {
                    //申请权限
                    CRMLog.LogInfo("lsy", "准备首次申请权限");
                }
                unGrantedList.add(permissions[i]);
            }
        }
        if(unGrantedList.size()==0){
            mPromptDialog.dismiss();
            mCallBack.onSuccess(requestCode);
        }else {
            String[] requestPermissions = new String[unGrantedList.size()];
            for(int i = 0;i<unGrantedList.size();i++){
                requestPermissions[i] = unGrantedList.get(i);
            }
            ActivityCompat.requestPermissions(mContext, requestPermissions, requestCode);
        }
    }

    /**
     * 在Activity的onRequestPermissionsResult中进行回调
     */
    public void registActivityResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        List<String>  unGrantedList = new ArrayList<>();
        CRMLog.LogInfo(TAG
                , "请求权限完成 requestCode="
                        + requestCode
                        + "   permissions="
                        + permissions
                        + "    results="
                        + grantResults);

        for(int i = 0;i< grantResults.length;i++){
            if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                unGrantedList.add(permissions[i]);
                mPromptDialog.setTitle("功能"+mPermissionNames[i]+"权限未开启,点击设置开启");
                mPromptDialog.show();
            }
        }
        if(unGrantedList.size()==0){
            mPromptDialog.dismiss();
            mCallBack.onSuccess(requestCode);
        }

    }


}

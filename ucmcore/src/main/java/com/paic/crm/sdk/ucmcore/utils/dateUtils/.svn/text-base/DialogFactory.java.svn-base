package com.paic.crm.sdk.ucmcore.utils.dateUtils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

import com.paic.crm.sdk.ucmcore.R;
import com.paic.crm.sdk.ucmcore.utils.CommonUtils;


/**
 * @author hanyh
 * @date 2016-2-29
 *
 */
public class DialogFactory {
    private static final String TAG = DialogFactory.class.getSimpleName();

    /**
     * 加载框
     * @param context
     * @param tip 提示内容
     */
    public static Dialog getSelectDialog(Context context, String tip) {
        Dialog loadingDialog = new Dialog(context, R.style.dialog);
//        loadingDialog.setContentView(R.layout.crm_sdk_select_popview_layout);
        loadingDialog.setCancelable(true);
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = CommonUtils.getScreenWidth(context);
        lp.width = (int) (0.5 * width);
//        TextView titleTxtv = (TextView) loadingDialog.findViewById(R.id.tvLoad);
        loadingDialog.setCanceledOnTouchOutside(false);
        if (tip == null || tip.length() == 0) {
//            titleTxtv.setText("tishi");
        } else {
//            titleTxtv.setText(tip);
        }
        return loadingDialog;
    }




    
}

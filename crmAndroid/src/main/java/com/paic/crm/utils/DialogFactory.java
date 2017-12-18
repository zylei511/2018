package com.paic.crm.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.paic.crm.android.R;

/**
 * @author hanyh
 * @date 2016-2-29
 *
 */
public class DialogFactory {
    private static final String TAG = DialogFactory.class.getSimpleName();


// TODO: 2017/11/22 应该改成可以修改title的
    /**
     * 加载框
     * @param context
     * @param tip 提示内容
     */

    public static Dialog getLoadingDialog(Context context, String tip) {
        Dialog loadingDialog = new Dialog(context, R.style.dialog);
        loadingDialog.setContentView(R.layout.dialog_loading_layout);
        loadingDialog.setCancelable(true);
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = CommonUtils.getScreenWidth(context);
        lp.width = (int) (0.5 * width);
        TextView titleTxtv = (TextView) loadingDialog.findViewById(R.id.tvLoad);
        loadingDialog.setCanceledOnTouchOutside(false);
        if (tip == null || tip.length() == 0) {
            titleTxtv.setText("tishi");
        } else {
            titleTxtv.setText(tip);
        }
        return loadingDialog;
    }




    
}

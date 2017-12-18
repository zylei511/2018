package com.paic.crm.android.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.Config;
import com.paic.crm.utils.Constants;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.ConstantsAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by pingan001 on 16/6/3.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Config.getConfig("APP_ID"), false);
        api.handleIntent(getIntent(), this);
//        api.registerApp(AppContext.APP_ID);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        switch (baseReq.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                Toast.makeText(this, baseReq.getType(), Toast.LENGTH_LONG).show();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                Toast.makeText(this, baseReq.getType(), Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, baseReq.getType(), Toast.LENGTH_LONG).show();
                break;

        }
    }

    @Override
    public void onResp(BaseResp baseResp) {
//        String result = "";
//
//        switch (baseResp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                result = 1+"ERR_OK";
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = 2+"ERR_USER_CANCEL";
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = 3+"ERR_AUTH_DENIED";
//                break;
//            default:
//                result = 4+"unKnow";
//                break;
//        }
        CRMLog.LogInfo(Constants.LOG_TAG,"baseResp:"+baseResp.errCode+"||"+baseResp.errCode);
        finish();
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
}

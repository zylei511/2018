package com.paic.crm.sdk.ucmcore.interlayer;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by yueshaojun on 2017/7/19.
 */

public interface IInterLayer {
	void execute(Context context, HashMap<String, Object> param);
}

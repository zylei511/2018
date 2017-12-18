package com.paic.crm.uihelper;

import android.content.Context;
import android.view.View;

/**
 * Created by yueshaojun on 2017/8/9.
 */

public class BaseHelper {
	Context mContext;
	View mContainer;
	BaseHelper(Context context, View container){
		mContext = context;
		mContainer = container;
	}
}

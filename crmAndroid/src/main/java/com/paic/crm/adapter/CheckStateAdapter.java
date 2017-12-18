package com.paic.crm.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.paic.crm.android.R;
import com.paic.crm.common.CommonAdapter;
import com.paic.crm.common.ViewHolder;
import com.paic.crm.entity.KZKFCheckStateBean;

import java.util.List;

/**
 * Created by ex-zhangyuelei001 on 2017/11/8.
 */

public class CheckStateAdapter extends CommonAdapter<KZKFCheckStateBean> {

    public CheckStateAdapter(Context context, List<KZKFCheckStateBean> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, KZKFCheckStateBean checkStateBean, int position) {
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_title = holder.getView(R.id.tv_title);
        View divider = holder.getView(R.id.divider_line);
        tv_title.setText(checkStateBean.tv_title);
        tv_content.setText(checkStateBean.tv_content);
        if (mDatas.size() != position +1){
            divider.setVisibility(View.VISIBLE);
        }
    }
}

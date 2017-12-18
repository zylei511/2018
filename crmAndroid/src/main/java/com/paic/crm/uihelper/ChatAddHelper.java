package com.paic.crm.uihelper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.paic.crm.adapter.ChatAddGridAdapter;
import com.paic.crm.android.R;
import com.paic.crm.app.ChatAddHolder;
import com.paic.crm.utils.CRMLog;


/**
 * Created by yueshaojun on 2017/8/29.
 */

public class ChatAddHelper extends BaseHelper implements AdapterView.OnItemClickListener{
	private GridView mGridView;
	private ChatAddGridAdapter mGridAdapter ;
	private AdapterView.OnItemClickListener mItemClickListener;
	private int mHeight = 0;
	private ViewGroup.LayoutParams lp;
	public ChatAddHelper(Context context, View container,int defaultHeight) {
		super(context, container);
		mGridView = (GridView) mContainer.findViewById(R.id.chat_add_grid);

		mGridAdapter = new ChatAddGridAdapter(
				mContext
				, ChatAddHolder.getChatAddBeans()
				, R.layout.item_chat_add_grid_layout);
		mGridView.setAdapter(mGridAdapter);
		mHeight = defaultHeight;
		lp = mContainer.getLayoutParams();
	}
	public void active(){
		mContainer.setVisibility(View.VISIBLE);
		lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
		lp.height = mHeight;
		mContainer.setLayoutParams(lp);
		CRMLog.LogDebg("chat","helper active :"+mHeight);
	}
	public void inActive(){
		mContainer.setVisibility(View.GONE);
	}
	public void setItemClickListener(AdapterView.OnItemClickListener itemClickListener){
		mGridView.setOnItemClickListener(this);
		mItemClickListener = itemClickListener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String iconName = ChatAddHolder.getChatAddBeans().get(position).iconName;
		view.setTag(iconName);
		if(mItemClickListener ==null){
			return;
		}
		mItemClickListener.onItemClick(parent, view, position, id);
	}
	public void setHeight(int height){
		mHeight = height;
	}
}

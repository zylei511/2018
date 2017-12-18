package com.paic.crm.widget;

import java.util.List;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;


public class SwipeMenuView extends LinearLayout implements OnClickListener {

    private PullToRefreshSwipeMenuListView mListView;
    private SwipeMenuLayout mLayout;
    private SwipeMenu mMenu;
    private OnSwipeItemClickListener onItemClickListener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public SwipeMenuView(SwipeMenu menu, PullToRefreshSwipeMenuListView listView) {
        super(menu.getContext());
        mListView = listView;
        mMenu = menu;
        int id = 0;
        List<SwipeMenuItem> items = menu.getMenuItems();
        for (SwipeMenuItem item : items) {
            addItem(item, id ++);
        }
    }

    private void addItem(SwipeMenuItem item, int id) {
        LayoutParams params = new LayoutParams(item.getWidth(), LayoutParams.MATCH_PARENT);
        LinearLayout parent = new LinearLayout(getContext());
        parent.setId(id);
        parent.setTag(item);
        parent.setGravity(Gravity.CENTER);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        parent.setBackgroundDrawable(item.getBackground());
        parent.setOnClickListener(this);
        if(item.getIcon()!=null) {
            parent.addView(item.getIconView());
        }
        if(!TextUtils.isEmpty(item.getTitle())) {
            parent.addView(item.getTitleView());
        }
        addView(parent);

    }

    @Override
    public void onClick(View v) {
        int itemId = ((SwipeMenuItem)v.getTag()).getId();
        if (onItemClickListener != null && mLayout.isOpen()) {
            onItemClickListener.onItemClick(this, mMenu, itemId);
        }
    }

    public OnSwipeItemClickListener getOnSwipeItemClickListener() {
        return onItemClickListener;
    }

    public void setOnSwipeItemClickListener(OnSwipeItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setLayout(SwipeMenuLayout mLayout) {
        this.mLayout = mLayout;
    }

    public void reset(){
       invalidate();
    }

}

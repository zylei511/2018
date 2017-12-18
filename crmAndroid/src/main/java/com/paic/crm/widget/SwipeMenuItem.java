package com.paic.crm.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author hanyh
 * @date 2014-8-23
 * 
 */
public class SwipeMenuItem {

	private int id;
	private Context mContext;
	private Drawable background;
	private int width;
	private int iconId;
	private Drawable icon;
	private String title;
	private TextView titleView;
	private ImageView iconView;

	public SwipeMenuItem(Context context) {
		mContext = context;
		titleView = new TextView(context);
		titleView.setGravity(Gravity.CENTER);
		iconView = new ImageView(context);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public void setTitleSize(int titleSize) {
		this.titleView.setTextSize(titleSize);
	}

	public void setTitleColor(int titleColor) {
		this.titleView.setTextColor(titleColor);
	}

	public void setTitle(String title) {
		this.title = title;
		this.titleView.setText(title);
	}

	public void setTitle(int resId) {
		title = mContext.getString(resId);
		setTitle(title);
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
		iconView.setImageDrawable(icon);
	}

	public void setIcon(int resId) {
		this.icon = mContext.getResources().getDrawable(resId);
		iconView.setImageResource(resId);
	}

	public Drawable getBackground() {
		return background;
	}

	public void setBackground(Drawable background) {
		this.background = background;
	}

	public void setBackground(int resId) {
		this.background = mContext.getResources().getDrawable(resId);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getIconId() {
		return iconId;
	}

	public void setIconId(@IdRes int iconId) {
		this.iconId = iconId;
	}

	public Drawable getIcon() {
		return icon;
	}

	public String getTitle() {
		return title;
	}

	public ImageView getIconView() {
		return iconView;
	}

	public TextView getTitleView() {
		return titleView;
	}
}


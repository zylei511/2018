package com.paic.crm.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.paic.crm.android.R;
import com.paic.crm.net.VolleyHttpConnector;
import com.paic.crm.net.VolleyInterface;
import com.paic.crm.photoview.PhotoViewAttacher;
import com.paic.crm.utils.BizSeriesUtil;
import com.paic.crm.utils.CRMLog;
import com.paic.crm.utils.CommonUtils;
import com.paic.crm.utils.HttpUrls;


/**
 * Created by pingan001 on 16/1/21.
 */
public class ImageScaleActivity extends BaseActivity{
    private ImageView imageView;
    private ImageView iv_back;
    private PhotoViewAttacher photoViewAttacher;
    private String url;
    private TextView actionTitle;
    private ProgressBar progressBar;
    private LinearLayout iv_back_parent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_scale_layout);
        init();
        requsetImage();
    }
    public void init(){
        Bundle bundle = getIntent().getExtras();
        String msgUrl = bundle.getString("picUrl");
        if (BizSeriesUtil.isKZKF(this)){
            url = HttpUrls.UCP_PIC_PRIFEX_KZKF;
        } else {
            url = HttpUrls.UCP_PIC_PRIFEX;
        }
        url= url + msgUrl;
        CRMLog.LogInfo("YY_picUrl", url);
        imageView = (ImageView)findViewById(R.id.scale_image);
        progressBar = (ProgressBar) findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);
        actionTitle = (TextView) findViewById(R.id.action_title);
        actionTitle.setText("图片");
        photoViewAttacher = new PhotoViewAttacher(imageView);
        iv_back_parent = (LinearLayout)findViewById(R.id.iv_back_parent);
        iv_back_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setImageResource(R.drawable.icon_btn_back);
        photoViewAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });

    }
    private void requsetImage(){
        VolleyHttpConnector.volleyGetImage(this,url, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new VolleyInterface() {
            @Override
            public void onSuccess(Object obj) {
                Bitmap bitmap = (Bitmap) obj;
                doOnGetImage(bitmap);
            }

            @Override
            public void onError(VolleyError ve) {
                doOnGetImageDefeat();
            }

            @Override
            public void onLogOutside() {
                CommonUtils.exitWhenHasLogIn(ImageScaleActivity.this);
            }
        });
    }

    private void doOnGetImage(Bitmap bitmap){
        progressBar.setVisibility(View.GONE);
        imageView.setImageBitmap(bitmap);
        photoViewAttacher.update();
    }

    private void doOnGetImageDefeat(){
        progressBar.setVisibility(View.GONE);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_pic_defalt);
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(rl);
        imageView.setImageBitmap(bitmap);
        photoViewAttacher.update();
    }
}

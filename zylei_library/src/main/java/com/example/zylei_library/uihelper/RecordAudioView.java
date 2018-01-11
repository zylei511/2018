package com.example.zylei_library.uihelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zylei_library.R;

/**
 *
 * @author ex-zhangyuelei001
 * @date 2018/1/3
 */

public class RecordAudioView {
    private static Dialog mDialog;
    private ImageView mVoice;
    private TextView mLabel;
    private RoundProgressBar mRoundProgressBar;

    private RecordAudioView(){}

    static class RecordAudioViewHolder{
        private static RecordAudioView instance = new RecordAudioView();
    }

    public static RecordAudioView newInstance(){

        return RecordAudioViewHolder.instance;
    }

    public RecordAudioView initDialog(Context context){
        if (mDialog == null){
            mDialog = new Dialog(context, R.style.Theme_AudioDialog);
            mDialog.setContentView(R.layout.layout_dialog_record_audio);
            mVoice = (ImageView) mDialog.findViewById(R.id.image);
            mLabel = (TextView) mDialog.findViewById(R.id.text_tips);
            mRoundProgressBar = (RoundProgressBar) mDialog.findViewById(R.id.progress_bar);
            mDialog.setCanceledOnTouchOutside(true);
        }
        mRoundProgressBar.setVisibility(View.VISIBLE);
        mVoice.setImageResource(R.drawable.crm_sdk_record_audio);
        setTips(context.getString(R.string.crm_sdk_audio_slide_cancel_send));
        return this;
    }

    public RecordAudioView setImageLevel(int level){
        mVoice.setImageLevel(level);
        return this;
    }

    public RecordAudioView setImageResource(int resourceId){
        mVoice.setImageResource(resourceId);
        mRoundProgressBar.setVisibility(View.INVISIBLE);
        return this;
    }

    public RecordAudioView setRoundProgressBar(float progress){
        mRoundProgressBar.setProgress(progress);
        return this;
    }

    public RecordAudioView setTips(String tips){
        mLabel.setText(tips);
        return this;
    }

    public void show(){
        if (mDialog == null){
            return;
        }
        mDialog.show();
    }

    public void dismiss(){
        if (mDialog == null){
            return;
        }
        mDialog.dismiss();
        mDialog = null;
    }

    public void delayDismiss(){
        mVoice.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mDialog == null){
                    return;
                }
                mDialog.dismiss();
                mDialog = null;
            }
        },500);

    }
}

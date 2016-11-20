package com.app.basevideo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.basevideo.R;
import com.app.basevideo.base.MFBaseDialog;

import java.util.Timer;
import java.util.TimerTask;

public class ProgressDialog extends MFBaseDialog {
    private View mView;
    private ImageView mImageView1, mImageView2, mImageView3;
    private TextView mTitle;
    private int count;
    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(count);
            count = (count + 1 >= 3) ? 0 : count + 1;
        }
    };
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setImage(true, false, false);
                    break;
                case 1:
                    setImage(false, true, false);
                    break;
                case 2:
                    setImage(false, false, true);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public ProgressDialog(Context context, int layout, int style) {
        super(context, style);
        init(context, layout);
    }

    private void init(final Context context, int layout) {
        mView = LayoutInflater.from(context).inflate(layout, null);
        mImageView1 = (ImageView) mView.findViewById(R.id.image1);
        mImageView2 = (ImageView) mView.findViewById(R.id.image2);
        mImageView3 = (ImageView) mView.findViewById(R.id.image3);
        mTitle = (TextView) mView.findViewById(R.id.title);
        setContentView(mView);
    }

    private void setImage(boolean isImg1Checked, boolean isImg2Checked, boolean isImg3Checked) {
        mImageView1.setImageResource(isImg1Checked ? R.drawable.mf_dot_white : R.drawable.mf_dot_orange);
        mImageView2.setImageResource(isImg2Checked ? R.drawable.mf_dot_white : R.drawable.mf_dot_orange);
        mImageView3.setImageResource(isImg3Checked ? R.drawable.mf_dot_white : R.drawable.mf_dot_orange);

    }

    @Override
    public void show() {
        super.show();
        timer.schedule(timerTask, 0, 400);
    }

    @Override
    public void dismiss() {
        if (timerTask == null || timer == null) {
            return;
        }
        super.dismiss();
        timerTask.cancel();
        timer.cancel();
    }

    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title == null ? "" : title);
        }
    }
}

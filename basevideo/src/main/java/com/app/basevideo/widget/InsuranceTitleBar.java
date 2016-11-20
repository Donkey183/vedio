package com.app.basevideo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.app.basevideo.R;
import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.base.MFBaseLayout;

import x.ImageView;
import x.LinearLayout;
import x.TextView;

public class InsuranceTitleBar extends MFBaseLayout {

    private TextView mTxtTitle;
    private LinearLayout mBackBtn;
    private ImageView mBtnRight;

    public InsuranceTitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InsuranceTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InsuranceTitleBar(Context context) {
        super(context);
    }

    @Override
    protected int onInitLayoutResId() {
        return R.layout.finance_common_title_bar_layout;
    }

    @Override
    protected void onInit() {
        super.onInit();
        initView();
    }

    private void initView() {
        mTxtTitle = (TextView) findViewById(R.id.title_bar_title);
        mBtnRight = (ImageView) findViewById(R.id.title_bar_right_btn);
        mBackBtn = (x.LinearLayout) findViewById(R.id.title_bar_back_btn);
        this.setBackgroundColor(getResources().getColor(R.color.finance_191d20));
        mBtnRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MFBaseApplication.getInstance(), "亲爱的再点我一次", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setBackBtnClickListener(OnClickListener listener) {
        mBackBtn.setOnClickListener(listener);
    }

    public void setRightMenuClickListener(OnClickListener listener) {
//        mBtnRight.setOnClickListener(listener);
    }

    public void setTitle(int resID) {
        mTxtTitle.setText(resID);
    }

    public LinearLayout getBackBtn() {
        return mBackBtn;
    }
}

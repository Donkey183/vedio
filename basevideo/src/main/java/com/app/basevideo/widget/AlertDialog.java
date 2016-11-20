package com.app.basevideo.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.basevideo.R;
import com.app.basevideo.base.MFBaseDialog;
import com.app.basevideo.util.StringHelper;

public class AlertDialog extends MFBaseDialog {
    private String mTitle;
    private String mContent;
    public AlertDialog(Context context, String title, String content, View.OnClickListener confirmOnClikListener) {
        super(context, R.style.finance_alert_dialog);
        init(context,title,content,confirmOnClikListener);
    }

    private void init(Context context,String title,String content,View.OnClickListener confirmOnClikListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.finance_alert_dialog, null);
        if(!StringHelper.isEmpty(title)){
            TextView titleTextView = (TextView) view.findViewById(R.id.alertDialogTitle);
            titleTextView.setText(title);
        }
        if(!StringHelper.isEmpty(content)){
            TextView contentTextView = (TextView) view.findViewById(R.id.alertDialogContent);
            contentTextView.setText(content);
        }
        if(confirmOnClikListener!=null){
            TextView confirmTextView = (TextView) view.findViewById(R.id.alertDialogConfirm);
            confirmTextView.setOnClickListener(confirmOnClikListener);
        }
        TextView cancelTextView = (TextView) view.findViewById(R.id.alertDialogCancel);
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(view);

    }

    @Override
    public void show() {
        super.show();
    }
}

package com.app.video.ui.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.video.R;

public class CommonAlert{

    private Context context;
    private ImageView alert_cha;
    private CheckBox check_wechat;
    private CheckBox check_zhifu;
    private RelativeLayout zhifu1;
    private RelativeLayout zhifu2;
    AlertDialog alert;

    public CommonAlert(Context context) {
        this.context = context;
    }

    public void showAlert(String a, String b){
        alert = new AlertDialog.Builder(context).create();

        alert.show();
        Window window = alert.getWindow();
        window.setContentView(R.layout.dialog_layout);

        check_wechat = (CheckBox) window.findViewById(R.id.check_wechat);
        check_zhifu = (CheckBox) window.findViewById(R.id.check_zhifu);

        alert_cha = (ImageView) window.findViewById(R.id.dialog_cha);

        zhifu1 = (RelativeLayout) window.findViewById(R.id.layout_zhifu1);

        zhifu2 = (RelativeLayout) window.findViewById(R.id.layout_zhifu2);



        if(b==null){
            zhifu2.setVisibility(View.GONE);
        }

        alert_cha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        check_wechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check_zhifu.setChecked(false);
                }
            }
        });
        check_zhifu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check_wechat.setChecked(false);
                }
            }
        });

        zhifu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "支付111111", Toast.LENGTH_SHORT).show();
            }
        });
        zhifu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "支付222222", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

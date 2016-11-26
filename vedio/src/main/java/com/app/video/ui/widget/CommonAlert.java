package com.app.video.ui.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.config.Payoff;

public class CommonAlert {

    private Context context;
    private ImageView alert_cha;
    private CheckBox check_wechat;
    private CheckBox check_zhifu;
    private RelativeLayout zhifu1;
    private RelativeLayout zhifu2;

    private ImageView dialog_img;
    private ImageView zhifu1_img;
    private TextView zhifu1_text1;
    private TextView zhifu1_text2;
    private TextView zhifu1_text;

    private ImageView zhifu2_img;
    private TextView zhifu2_text1;
    private TextView zhifu2_text2;
    private TextView zhifu2_text;

    AlertDialog alert;

    public CommonAlert(Context context) {
        this.context = context;
    }

    public void showAlert(Payoff pay1, Payoff pay2,int id) {
        alert = new AlertDialog.Builder(context).create();

        alert.show();
        Window window = alert.getWindow();
        LayoutInflater inflate = LayoutInflater.from(context);
        View contentView = inflate.inflate(R.layout.dialog_layout, null);
        WindowUtil.resizeRecursively(contentView);
        window.setContentView(contentView);

        dialog_img = (ImageView) window.findViewById(R.id.dialog_img);

        zhifu1_img = (ImageView) window.findViewById(R.id.zhifu1_img);
        zhifu1_text = (TextView) window.findViewById(R.id.zhifu1_text);
        zhifu1_text1 = (TextView) window.findViewById(R.id.zhifu1_text1);
        zhifu1_text2 = (TextView) window.findViewById(R.id.zhifu1_text2);

        dialog_img.setImageResource(id);

        zhifu1_text.setText("￥"+pay1.getVip_money());
        zhifu1_text1.setText(pay1.getVip_name());
        zhifu1_text2.setText(pay1.getVip_message());
        zhifu1_img.setImageResource(pay1.getVip_img());

        zhifu2_img = (ImageView) window.findViewById(R.id.zhifu2_img);
        zhifu2_text = (TextView) window.findViewById(R.id.zhifu2_text);
        zhifu2_text1 = (TextView) window.findViewById(R.id.zhifu2_text1);
        zhifu2_text2 = (TextView) window.findViewById(R.id.zhifu2_text2);

        check_wechat = (CheckBox) window.findViewById(R.id.check_wechat);
        check_zhifu = (CheckBox) window.findViewById(R.id.check_zhifu);

        alert_cha = (ImageView) window.findViewById(R.id.dialog_cha);

        zhifu1 = (RelativeLayout) window.findViewById(R.id.layout_zhifu1);

        zhifu2 = (RelativeLayout) window.findViewById(R.id.layout_zhifu2);


        if (pay2 == null) {
            zhifu2.setVisibility(View.GONE);
        }else{
            zhifu2_text.setText("￥"+pay2.getVip_money());
            zhifu2_text1.setText(pay2.getVip_name());
            zhifu2_text2.setText(pay2.getVip_message());
            zhifu2_img.setImageResource(pay2.getVip_img());
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
                if (isChecked) {
                    check_zhifu.setChecked(false);
                }
            }
        });
        check_zhifu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
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

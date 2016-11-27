package com.app.video.ui.activity;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.video.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

public class UserInfoActivity extends AppCompatActivity {

    private Button btn_show;
    private ImageView back;
    private TextView user_pass2;
    private ImageView user_head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        btn_show = (Button) findViewById(R.id.btn_show);
        back = (ImageView) findViewById(R.id.user_back) ;
        user_head = (ImageView) findViewById(R.id.user_head);
        user_pass2 = (TextView) findViewById(R.id.user_pass2);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_pass2.getInputType()==InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
                    user_pass2.setInputType(InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                user_pass2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });

        Glide.with(this).load("http://img.taopic.com/uploads/allimg/121017/234940-12101FR22825.jpg").asBitmap().centerCrop().into(new BitmapImageViewTarget(user_head) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                user_head.setImageDrawable(circularBitmapDrawable);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

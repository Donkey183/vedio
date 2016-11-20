package com.app.basevideo.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class ContactUtil {
    public static void Call(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (!StringHelper.isEmpty(phoneNumber)) {
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                context.startActivity(intent);

            }
        } else {
            Toast.makeText(context, "电话号码不合法", Toast.LENGTH_SHORT).show();
        }

    }
}

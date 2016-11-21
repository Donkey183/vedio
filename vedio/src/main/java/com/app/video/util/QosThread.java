package com.app.video.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Handler;

import com.app.video.config.QosObject;


public class QosThread extends Thread {

    private Context mContext;
    private Handler mHandler;
    private CpuUtil mCpuStats;
    private Debug.MemoryInfo mi;
    private QosObject mQosObject;
    private String mPackageName;

    private boolean mRunning;

    public QosThread(ActivityManager manager, Handler handler, Context context) {
        mHandler = handler;
        mCpuStats = new CpuUtil();
        mi = new Debug.MemoryInfo();
        mRunning = true;
        mQosObject = new QosObject();
        if(context != null)
            mPackageName = context.getPackageName();
        mContext = context;
    }

    @Override
    public void run() {
        while(mRunning) {
            mCpuStats.parseTopResults(mPackageName);

            Debug.getMemoryInfo(mi);

            if(mHandler != null) {
                mQosObject.cpuUsage = mCpuStats.getProcessCpuUsage();
                mQosObject.pss = mi.getTotalPss();
                mQosObject.vss = mi.getTotalPrivateDirty();
                mHandler.obtainMessage(9, mQosObject).sendToTarget();
            }
            try {
                sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        mRunning = false;
    }
}

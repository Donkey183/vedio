package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.adaptor.BlueFragmentAdapter;
import com.app.video.adaptor.VIPFragmentAdaptor;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VideoModel;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.ui.widget.CommonAlert;

public class BlueFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener,View.OnClickListener{

    private RecyclerView vip_recyclerView;
    private VIPFragmentAdaptor mAdapter;
    private VideoModel mModel;
    private LinearLayout btn_layout;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn_next;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VideoModel(this);
        getVideoInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        btn_layout = (LinearLayout) view.findViewById(R.id.btn_layout);
        btn_layout.setVisibility(View.GONE);
        vip_recyclerView = (RecyclerView) view.findViewById(R.id.vip_recycler);
        GridLayoutManager manager = new GridLayoutManager(getActivity(),4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 4 : 1;
            }
        });
        vip_recyclerView.setLayoutManager(manager);

        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn3 = (Button) view.findViewById(R.id.btn3);
        btn4 = (Button) view.findViewById(R.id.btn4);
        btn5 = (Button) view.findViewById(R.id.btn5);
        btn6 = (Button) view.findViewById(R.id.btn6);
        btn7 = (Button) view.findViewById(R.id.btn7);
        btn_next = (Button) view.findViewById(R.id.btn_next);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        WindowUtil.resizeRecursively(view);
        return view;
    }

    private void getVideoInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, "6");
        request.addParam(VedioConstant.PAGE_NO, "1");
        mModel.sendHttpRequest(request, VideoModel.GET_VEDIO_BLUE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoInfo();
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new VIPFragmentAdaptor(this.getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.showVIPView(mModel.videoData.page.result,mModel.videoData.page.list1);
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
        if(!Constants.config.getVip_now().equals(Constants.BLUE)){
            CommonAlert alert = new CommonAlert(getActivity());
            alert.showAlert(Constants.config.getPay1(),Constants.config.getPay2(),Constants.config.getPay_img(),R.id.vip_layout);
        }else{
            String recourseUrl = "";
            String img = "";
            if (obj instanceof VideoData.Page.Video) {
                recourseUrl = ((VideoData.Page.Video) obj).getDyres();
                img = ((VideoData.Page.Video) obj).getDypic();
            } else if (obj instanceof VideoData.Page.Banner) {
                recourseUrl = ((VideoData.Page.Banner) obj).getDyresource();
                img = ((VideoData.Page.Banner) obj).getDypic();
            }
            Intent intent = new Intent(getActivity(),VideoPlayerActivity.class);
            intent.putExtra("path", recourseUrl);
            intent.putExtra("img",img);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view){
        if(!Constants.config.getVip_now().equals(Constants.RED)){
            CommonAlert alert = new CommonAlert(getActivity());
            alert.showAlert(Constants.config.getPay1(),Constants.config.getPay2(),Constants.config.getPay_img(),R.id.vip_layout);
        }
    }


}

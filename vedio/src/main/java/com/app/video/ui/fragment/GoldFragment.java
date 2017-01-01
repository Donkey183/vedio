package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.basevideo.base.MFBaseApplication;
import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.basevideo.util.WindowUtil;
import com.app.video.R;
import com.app.video.adaptor.VIPFragmentAdaptor;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VideoModel;
import com.app.video.ui.activity.VideoPlayerActivity;
import com.app.video.ui.widget.CommonAlert;
import com.app.video.util.PlayCountUtil;


public class GoldFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView vip_recyclerView;
    private VIPFragmentAdaptor mAdapter;
    private VideoModel mModel;


    private SwipeRefreshLayout mSwipeRefresh;
    private int lastVisibleItem;
    GridLayoutManager mLayoutManager;
    private int offsetHeight;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new VideoModel(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vip, container, false);

        vip_recyclerView = (RecyclerView) view.findViewById(R.id.vip_recycler);

        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefresh.setOnRefreshListener(this);
        vip_recyclerView.setHasFixedSize(true);
        vip_recyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 3 : 1;
            }
        });

        vip_recyclerView.setLayoutManager(mLayoutManager);


        vip_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mSwipeRefresh.setRefreshing(true);
                    getVideoInfo(String.valueOf(mModel.curPageNo + 1));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                offsetHeight = recyclerView.computeVerticalScrollOffset();
            }

        });

        WindowUtil.resizeRecursively(view);
        return view;
    }

    private void getVideoInfo(String pageNo) {
        if (mModel.curPageNo < 0 && mModel.videoData.page != null && mModel.videoData.page.result != null && mModel.videoData.page.result.size() > 0) {
            mSwipeRefresh.setRefreshing(false);
//            Toast.makeText(MFBaseApplication.getInstance(), "已加载全部视频!", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.R_TYPE, "2");
        request.addParam(VedioConstant.PAGE_NO, pageNo);
        mModel.sendHttpRequest(request, VideoModel.GET_VEDIO_GLOD);
    }

    @Override
    public void onResume() {
        super.onResume();
        getVideoInfo("" + mModel.curPageNo);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new VIPFragmentAdaptor(this.getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(this);
        vip_recyclerView.setAdapter(mAdapter);
        mAdapter.showVIPView(mModel.videoData.page.result, mModel.videoData.page.list1);
        mSwipeRefresh.setRefreshing(false);
        vip_recyclerView.scrollBy(0, offsetHeight);
        if (Constants.select_fragment.equals("home")) {
            dispatchMessage(new CommonMessage(VedioCmd.TITLE_CHANGE));
        }
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {

        if (!Constants.config.getVip_now().equals(Constants.GOLD)) {
            CommonAlert alert = new CommonAlert(getActivity());
            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
        } else {
            if (!PlayCountUtil.hasAuth("GOLD")) {
                Toast.makeText(MFBaseApplication.getInstance(), "您的播放次数已超过五次!", Toast.LENGTH_LONG).show();
                CommonAlert alert = new CommonAlert(GoldFragment.this.getActivity());
                alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.forum_layout);
                return;
            }
            String recourseUrl = "";
            String img = "";
            if (obj instanceof VideoData.Page.Video) {
                recourseUrl = ((VideoData.Page.Video) obj).getDyres();
                img = ((VideoData.Page.Video) obj).getDypic();
            } else if (obj instanceof VideoData.Page.Banner) {
                recourseUrl = ((VideoData.Page.Banner) obj).getDyresource();
                img = ((VideoData.Page.Banner) obj).getDypic();
            }
            Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
            intent.putExtra("path", recourseUrl);
            intent.putExtra("img", img);
            intent.putExtra("parent", "");
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (!Constants.config.getVip_now().equals(Constants.RED)) {
            CommonAlert alert = new CommonAlert(getActivity());
            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vip_layout);
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(false);
    }

    public String getVideoCount() {
        if (mModel != null && mModel.videoData != null && mModel.videoData.page != null && mModel.videoData.page.getTotalCount() > 0) {
            return "" + mModel.videoData.page.getTotalCount();
        }
        return null;
    }
}

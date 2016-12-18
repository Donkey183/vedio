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

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.listener.MessageListener;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.adaptor.RecommendAdaptor;
import com.app.video.config.Constants;
import com.app.video.config.VedioConstant;
import com.app.video.data.VideoData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VideoModel;
import com.app.video.ui.activity.VedioDetailActivity;


public class RecommendFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView my_recyclerView;
    private RecommendAdaptor mAdapter;
    private VideoModel mVideoModel;
    private SwipeRefreshLayout mSwipeRefresh;
    private int lastVisibleItem;
    private GridLayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVideoModel = new VideoModel(this);
        registerListener(getVdeioInfoFailedListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        my_recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
//        mLayoutManager.setReverseLayout(true);
        my_recyclerView.setLayoutManager(mLayoutManager);


        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mSwipeRefresh.setOnRefreshListener(this);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        my_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    mSwipeRefresh.setRefreshing(true);
                    getRecommendInfo(String.valueOf(mVideoModel.curPageNo + 1));
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }

        });

        my_recyclerView.setHasFixedSize(true);
        my_recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getRecommendInfo("" + mVideoModel.curPageNo);
    }

    private void getRecommendInfo(String pageNo) {
        if (mVideoModel.curPageNo < 0 && mVideoModel.videoData.page != null && mVideoModel.videoData.page.result != null && mVideoModel.videoData.page.result.size() > 0) {
            mSwipeRefresh.setRefreshing(false);
//            Toast.makeText(MFBaseApplication.getInstance(), "已加载全部视频!", Toast.LENGTH_SHORT).show();
            return;
        }
        CommonHttpRequest request = new CommonHttpRequest();
        request.addParam(VedioConstant.PAGE_NO, pageNo);
        request.addParam(VedioConstant.R_TYPE, VedioConstant.CHANNEL_EXPERIENCE);
        mVideoModel.sendHttpRequest(request, VideoModel.GET_VEDIO_EXPERINCE);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        mAdapter = new RecommendAdaptor(RecommendFragment.this.getActivity(), this);
        my_recyclerView.setAdapter(mAdapter);
        mAdapter.showRecommendView(mVideoModel.videoData.page.result, mVideoModel.videoData.page.list1);
        mSwipeRefresh.setRefreshing(false);
//        my_recyclerView.smoothScrollBy(0, (lastVisibleItem));
        my_recyclerView.smoothScrollToPosition(lastVisibleItem);
        if(Constants.select_fragment.equals("home")){
            dispatchMessage(new CommonMessage(VedioCmd.TITLE_CHANGE));
        }
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {

        String recourseUrl = "";
        String img = "";
        if (obj instanceof VideoData.Page.Video) {
            recourseUrl = ((VideoData.Page.Video) obj).getDyres();
            img = ((VideoData.Page.Video) obj).getDypic();
        } else if (obj instanceof VideoData.Page.Banner) {
            recourseUrl = ((VideoData.Page.Banner) obj).getDyresource();
            img = ((VideoData.Page.Banner) obj).getDypic();
        }
        Intent intent = new Intent(getActivity(), VedioDetailActivity.class);
        intent.putExtra("path", recourseUrl);
        intent.putExtra("img", img);
        intent.putExtra("CUR_AREA", "0");

        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        mSwipeRefresh.setRefreshing(false);
    }

    MessageListener getVdeioInfoFailedListener = new MessageListener(VedioCmd.GET_VIDEO_INFO_FAILED) {
        @Override
        public void onMessage(CommonMessage<?> responsedMessage) {
            mSwipeRefresh.setRefreshing(false);
        }
    };


    public String getVideoCount() {
        if (mVideoModel != null && mVideoModel.videoData != null && mVideoModel.videoData.page != null && mVideoModel.videoData.page.getTotalCount() > 0) {
            return "" + mVideoModel.videoData.page.getTotalCount();
        }
        return null;
    }

}

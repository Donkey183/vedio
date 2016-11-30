package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VideoDetailData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VideoDetailResponse;

import retrofit2.Response;

public class VideoDetailModel extends MFBaseModel {

    public VideoDetailModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public VideoDetailData videoDetailData = new VideoDetailData();

    public static final int GET_VEDIO_DERAIL = 0x340001;


    @Override
    public void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VideoDetailResponse> call = HttpRequestService.createService(VedioNetService.class).getVideoDetailInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VideoDetailResponse>() {
            @Override
            public void onResponse(VideoDetailResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success || entity.list == null || entity.list == null || entity.list1 == null || entity.list2 == null) {
                    disPatchNetErrorMessage(-1, entity == null ? null : "" + entity.msg);
                    return;
                }
                videoDetailData.videoCommentList = entity.list;
                videoDetailData.pageCommentList = entity.list1;
                videoDetailData.recommendVideoList = entity.list2;
                disPatchRequestSuccessMessage(GET_VEDIO_DERAIL);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

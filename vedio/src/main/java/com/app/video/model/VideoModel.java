package com.app.video.model;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.base.MFBaseFragmentModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VideoData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VedioResponse;

import retrofit2.Response;


public class VideoModel extends MFBaseFragmentModel {

    public VideoModel(MFBaseFragment activityContext) {
        super(activityContext);
    }

    public VideoData videoData = new VideoData();

    public static final int GET_VEDIO_EXPERINCE = 0x60001;
    public static final int GET_VEDIO_COMMON = 0x60002;
    public static final int GET_XXX_INFO = 0x60003;
    public static final int GET_VEDIO_GLOD = 0x60004;
    public static final int GET_VEDIO_DIAMOND = 0x60006;
    public static final int GET_VEDIO_BLACK_GLOD = 0x60005;
    public static final int GET_VEDIO_CROWN = 0x60007;

    @Override
    public void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VedioResponse> call = HttpRequestService.createService(VedioNetService.class).getVideoResources(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VedioResponse>() {
            @Override
            public void onResponse(VedioResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success || entity.page == null || entity.page.result == null || entity.page.list1 == null) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                videoData.page = entity.page;
                disPatchRequestSuccessMessage(GET_XXX_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

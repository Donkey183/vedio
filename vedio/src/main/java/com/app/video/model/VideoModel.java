package com.app.video.model;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.base.MFBaseFragmentModel;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VideoData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VedioResponse;

import java.util.ArrayList;

import retrofit2.Response;


public class VideoModel extends MFBaseFragmentModel {

    public VideoModel(MFBaseFragment activityContext) {
        super(activityContext);
    }

    public VideoData videoData = new VideoData();
    public int curPageNo = 1;

    private int code;

    public static final int GET_VEDIO_EXPERINCE = 0;
    public static final int GET_VEDIO_COMMON = 1;
    public static final int GET_XXX_INFO = 2;
    public static final int GET_VEDIO_GLOD = 3;
    public static final int GET_VEDIO_DIAMOND = 4;
    public static final int GET_VEDIO_BLACK_GLOD = 5;
    public static final int GET_VEDIO_PURPLE = 6;
    public static final int GET_VEDIO_BLUE = 7;
    public static final int GET_VEDIO_CROWN = 8;

    @Override
    public void sendHttpRequest(final CommonHttpRequest request, final int requestCode) {
        code = requestCode;
        MFCall<VedioResponse> call = HttpRequestService.createService(VedioNetService.class).getVideoResources(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VedioResponse>() {
            @Override
            public void onResponse(VedioResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success || entity.page == null || entity.page.result == null || entity.page.list1 == null) {
                    MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<Object>(VedioCmd.GET_VIDEO_INFO_FAILED));
                    return;
                }
                if (requestCode == GET_VEDIO_EXPERINCE) {
                    if (videoData.page == null) {
                        videoData.page = new VideoData.Page();
                        videoData.page.result = new ArrayList<VideoData.Page.Video>();
                    }
                    videoData.page.setTotalCount(entity.page.getTotalCount());
                    videoData.page.result.addAll(entity.page.result);
                    videoData.page.list1 = entity.page.list1;
                    curPageNo++;
                    if (entity.page.result.size() == 0) {
                        curPageNo = -1;
                    }
                } else {
                    videoData.page = entity.page;
                }


                disPatchRequestSuccessMessage(code);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

//package com.app.video.model;
//
//import com.app.basevideo.base.MFBaseActivity;
//import com.app.basevideo.base.MFBaseModel;
//import com.app.basevideo.net.CommonHttpRequest;
//import com.app.basevideo.net.HttpRequestService;
//import com.app.basevideo.net.call.MFCall;
//import com.app.basevideo.net.callback.MFCallbackAdapter;
//import com.app.video.data.ChannelData;
//import com.app.video.net.VedioNetService;
//import com.app.video.net.response.ChannelResponse;
//
//import java.util.List;
//
//import retrofit2.Response;
//
//
//public class ChannelActivityModel extends MFBaseModel {
//
//    public ChannelActivityModel(MFBaseActivity activityContext) {
//        super(activityContext);
//    }
//
//    public List<ChannelData> channelList;
//    public static final int GET_CHANNEL_INFO = 0x10001;
//
//    @Override
//    public void sendHttpRequest(CommonHttpRequest request, int requestCode) {//"BD9F6B08527789FC", "D3BF8A21D8AE9941AD4021DB9ABCD50D", "1234"
//        MFCall<ChannelResponse> call = HttpRequestService.createService(VedioNetService.class).getChannelInfo(request.buildParams());
//        call.doRequest(new MFCallbackAdapter<ChannelResponse>() {
//            @Override
//            public void onResponse(ChannelResponse entity, Response<?> response, Throwable throwable) {
//                if (entity == null || !entity.success || entity.list == null) {
//                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
//                    return;
//                }
//                channelList = entity.list;
//                disPatchRequestSuccessMessage(GET_CHANNEL_INFO);
//            }
//        });
//    }
//
//    @Override
//    public boolean loadModelDataFromCache() {
//        return false;
//    }
//}

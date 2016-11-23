package com.app.video.net;

import com.app.basevideo.net.call.MFCall;
import com.app.video.net.response.ChannelResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

public interface VideoNetService {

    /**
     * 获取频道信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_channels.do")
    @FormUrlEncoded
    MFCall<ChannelResponse> getChannelInfo(@FieldMap Map<String, Object> params);


}

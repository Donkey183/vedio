package com.app.video.net;

import com.app.basevideo.net.call.MFCall;
import com.app.video.net.response.ChannelContentResponse;
import com.app.video.net.response.ChannelResponse;
import com.app.video.net.response.VaultContentResponse;
import com.app.video.net.response.VaultResponse;
import com.app.video.net.response.VedioResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;

public interface VideoNetService {


    /**
     * 初始化，第一次安装完App调用
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_init.do")
    @FormUrlEncoded
    MFCall<ChannelResponse> initApp(@FieldMap Map<String, Object> params);


    /**
     * 获取电影资源信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_resources.do")
    @FormUrlEncoded
    MFCall<VedioResponse> getVideoResources(@FieldMap Map<String, Object> params);

    /**
     * 获取支付信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_sysset.do")
    @FormUrlEncoded
    MFCall<ChannelResponse> getPayInfo(@FieldMap Map<String, Object> params);


    /**
     * 获取片库信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_dpics.do")
    @FormUrlEncoded
    MFCall<VaultResponse> getVaultInfo(@FieldMap Map<String, Object> params);


    /**
     * 获取片库内容信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_dpicinfos.do")
    @FormUrlEncoded
    MFCall<VaultContentResponse> getVaultContentInfo(@FieldMap Map<String, Object> params);

    /**
     * 获取电影频道信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_channels.do")
    @FormUrlEncoded
    MFCall<ChannelResponse> getChannelInfo(@FieldMap Map<String, Object> params);


    /**
     * 获取电影频道内容信息
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_channelinfos.do")
    @FormUrlEncoded
    MFCall<ChannelContentResponse> getChannelContentInfo(@FieldMap Map<String, Object> params);

    /**
     * 获取验证码
     *
     * @param params
     * @return
     */
    @GET("dyap/dy_verify.do")
    @FormUrlEncoded
    MFCall<ChannelResponse> getVerifyCode(@FieldMap Map<String, Object> params);


}

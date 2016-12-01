package com.app.video.net;

import com.app.basevideo.net.call.MFCall;
import com.app.video.net.response.ChannelContentResponse;
import com.app.video.net.response.ChannelResponse;
import com.app.video.net.response.InitAppResponse;
import com.app.video.net.response.WechatPayResponse;
import com.app.video.net.response.VaultContentResponse;
import com.app.video.net.response.VaultResponse;
import com.app.video.net.response.VedioResponse;
import com.app.video.net.response.VerifyCodeResponse;
import com.app.video.net.response.VideoDetailResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface VedioNetService {


    /**
     * 初始化，第一次安装完App调用
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_init.do")
    @FormUrlEncoded
    MFCall<InitAppResponse> initApp(@FieldMap Map<String, Object> params);


    /**
     * 获取电影资源信息
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_resources.do")
    @FormUrlEncoded
    MFCall<VedioResponse> getVideoResources(@FieldMap Map<String, Object> params);

    /**
     * 获取支付信息
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_sysset.do")
    @FormUrlEncoded
    MFCall<WechatPayResponse> getPayInfo(@FieldMap Map<String, Object> params);


    /**
     * 获取片库信息
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_dpics.do")
    @FormUrlEncoded
    MFCall<VaultResponse> getVaultInfo(@FieldMap Map<String, Object> params);


    /**
     * 获取片库内容信息
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_dpicinfos.do")
    @FormUrlEncoded
    MFCall<VaultContentResponse> getVaultContentInfo(@FieldMap Map<String, Object> params);

    /**
     * 获取电影频道信息
     *
     * @return
     */
    @POST("dyap/dy_channels.do")
    @FormUrlEncoded
    MFCall<ChannelResponse> getChannelInfo(@FieldMap Map<String, Object> params);
//@Query("token") String token, @Query("encyStr") String encyStr, @Query("timestamp") String timestamp

    /**
     * 获取电影频道内容信息
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_channelinfos.do")
    @FormUrlEncoded
    MFCall<ChannelContentResponse> getChannelContentInfo(@FieldMap Map<String, Object> params);

    /**
     * 获取验证码
     *
     * @param params
     * @return
     */
    @POST("dyap/dy_verify.do")
    @FormUrlEncoded
    MFCall<VerifyCodeResponse> getVerifyCode(@FieldMap Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    @POST("dyap/dy_view.do")
    @FormUrlEncoded
    MFCall<VideoDetailResponse> getVideoDetailInfo(@FieldMap Map<String, Object> params);


    /**
     * 微信支付
     * @param
     * @return
     */
    @POST("wxpay/getPrePayId.do")
    @FormUrlEncoded
    MFCall<WechatPayResponse> getWechatPayInfo(@FieldMap Map<String, Object> params);

}

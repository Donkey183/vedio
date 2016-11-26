package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VaultContentData;
import com.app.video.net.VideoNetService;
import com.app.video.net.response.VaultContentResponse;

import retrofit2.Response;


public class VaultContentModel extends MFBaseModel {

    public VaultContentModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public VaultContentData vaultContentData;
    public static final int GET_VAULT_CONTENT = 0x70012;

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VaultContentResponse> call = HttpRequestService.createService(VideoNetService.class).getVaultContentInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VaultContentResponse>() {
            @Override
            public void onResponse(VaultContentResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                vaultContentData = entity.list;
                disPatchRequestSuccessMessage(GET_VAULT_CONTENT);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

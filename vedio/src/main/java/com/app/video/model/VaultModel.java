package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VaultData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VaultResponse;

import retrofit2.Response;


public class VaultModel extends MFBaseModel {

    public VaultModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public VaultData vaultData;
    public static final int GET_VAULT_INFO = 0x10061;

    @Override
    protected void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VaultResponse> call = HttpRequestService.createService(VedioNetService.class).getVaultInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VaultResponse>() {
            @Override
            public void onResponse(VaultResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                vaultData = entity.list;
                disPatchRequestSuccessMessage(GET_VAULT_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

package com.app.video.model;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.base.MFBaseFragmentModel;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VaultData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VaultResponse;

import java.util.List;

import retrofit2.Response;


public class VaultModel extends MFBaseFragmentModel {

    public VaultModel(MFBaseFragment activityContext) {
        super(activityContext);
    }

    public List<VaultData> vaultList;
    public static final int GET_VAULT_INFO = 0x10061;

    @Override
    public void sendHttpRequest(CommonHttpRequest request, int requestCode) {
        MFCall<VaultResponse> call = HttpRequestService.createService(VedioNetService.class).getVaultInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VaultResponse>() {
            @Override
            public void onResponse(VaultResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success) {
                    disPatchNetErrorMessage(-1, entity == null ? null : entity.msg);
                    return;
                }
                vaultList = entity.list;
                disPatchRequestSuccessMessage(GET_VAULT_INFO);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

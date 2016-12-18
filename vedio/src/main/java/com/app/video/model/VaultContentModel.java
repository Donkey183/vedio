package com.app.video.model;

import com.app.basevideo.base.MFBaseActivity;
import com.app.basevideo.base.MFBaseModel;
import com.app.basevideo.config.VedioCmd;
import com.app.basevideo.framework.manager.MessageManager;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.HttpRequestService;
import com.app.basevideo.net.call.MFCall;
import com.app.basevideo.net.callback.MFCallbackAdapter;
import com.app.video.data.VaultContentData;
import com.app.video.net.VedioNetService;
import com.app.video.net.response.VaultContentResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class VaultContentModel extends MFBaseModel {

    public VaultContentModel(MFBaseActivity activityContext) {
        super(activityContext);
    }

    public List<VaultContentData> vaultContentDatas = new ArrayList<>();

    public static final int GET_VAULT_CONTENT = 109;


    @Override
    public void sendHttpRequest(final CommonHttpRequest request, final int requestCode) {
        MFCall<VaultContentResponse> call = HttpRequestService.createService(VedioNetService.class).getVaultContentInfo(request.buildParams());
        call.doRequest(new MFCallbackAdapter<VaultContentResponse>() {
            @Override
            public void onResponse(VaultContentResponse entity, Response<?> response, Throwable throwable) {
                if (entity == null || !entity.success || entity.list == null) {
                    MessageManager.getInstance().dispatchResponsedMessage(new CommonMessage<Object>(VedioCmd.GET_VIDEO_INFO_FAILED));
                    return;
                }
                vaultContentDatas = entity.list;
                disPatchRequestSuccessMessage(requestCode);
            }
        });
    }

    @Override
    public boolean loadModelDataFromCache() {
        return false;
    }
}

package com.app.video.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.basevideo.base.MFBaseFragment;
import com.app.basevideo.framework.message.CommonMessage;
import com.app.basevideo.net.CommonHttpRequest;
import com.app.basevideo.net.INetFinish;
import com.app.video.R;
import com.app.video.adaptor.VaultAdaptor;
import com.app.video.data.VaultData;
import com.app.video.listener.OnRecyclerViewItemClickListener;
import com.app.video.model.VaultModel;
import com.app.video.ui.activity.VaultActivity;


public class VaultFragment extends MFBaseFragment implements INetFinish, OnRecyclerViewItemClickListener {
    private RecyclerView valut_recyclerView;

    private VaultModel mVaultModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVaultModel = new VaultModel(this);
        getVaultInfo();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vault, container, false);
        valut_recyclerView = (RecyclerView) view.findViewById(R.id.vault_recycler);
        valut_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        return view;
    }

    private void getVaultInfo() {
        CommonHttpRequest request = new CommonHttpRequest();
        mVaultModel.sendHttpRequest(request, VaultModel.GET_VAULT_INFO);
    }

    @Override
    public void onHttpResponse(CommonMessage<?> responsedMessage) {
        VaultAdaptor vaultAdaptor = new VaultAdaptor(VaultFragment.this.getActivity(), VaultFragment.this);
        valut_recyclerView.setAdapter(vaultAdaptor);
        vaultAdaptor.showVaultView(mVaultModel.vaultList);
    }

    @Override
    public void onItemClick(View view, int position, Object obj) {
//        if (!Constants.config.getVip_now().equals(Constants.RED)) {
//            CommonAlert alert = new CommonAlert(getActivity());
//            alert.showAlert(Constants.config.getPay1(), Constants.config.getPay2(), Constants.config.getPay_img(), R.id.vault_layout);
//        } else {
        Intent intent = new Intent(VaultFragment.this.getActivity(), VaultActivity.class);
        final String pid = "" + ((VaultData) obj).getId();
        intent.putExtra("pid", pid);
        intent.putExtra("tittle",((VaultData) obj).getPname());
        startActivity(intent);
//        }
    }
}

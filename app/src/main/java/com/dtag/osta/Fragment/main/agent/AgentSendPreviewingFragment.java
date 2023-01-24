package com.dtag.osta.Fragment.main.agent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dtag.osta.Fragment.ViewModel.main.agent.AgentSendPreviewingViewModel;
import com.dtag.osta.R;

public class AgentSendPreviewingFragment extends Fragment {

    private AgentSendPreviewingViewModel mViewModel;

    public static AgentSendPreviewingFragment newInstance() {
        return new AgentSendPreviewingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.agent_send_previewing_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AgentSendPreviewingViewModel.class);
        // TODO: Use the ViewModel
    }

}

package cl.cutiko.soliloquio.views.main.bottomsheet;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.background.PlayerService;
import info.abdolahi.CircularMusicProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private CircularMusicProgressBar progressBar;

    public ProgressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if (PlayerService.PROGRESS.equals(intent.getAction())) {
                        float value = intent.getFloatExtra(PlayerService.CURRENT_PROGRESS, 0);
                        progressBar.setValue(value);
                    }
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(PlayerService.PROGRESS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = (CircularMusicProgressBar) view;
    }

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        super.onPause();
    }
}

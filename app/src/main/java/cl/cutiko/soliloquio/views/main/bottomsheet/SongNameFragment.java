package cl.cutiko.soliloquio.views.main.bottomsheet;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.background.PlayerService;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongNameFragment extends Fragment {

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private SongNameCallback callback;
    private TextView songName;

    public SongNameFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callback = (SongNameCallback) getParentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if (PlayerService.CURRENT_SONG.equals(intent.getAction())) {
                        String title = intent.getStringExtra(PlayerService.SONG_TITLE);
                        songName.setText(title);
                    }
                }
            }
        };
        filter = new IntentFilter();
        filter.addAction(PlayerService.CURRENT_SONG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songName = (TextView) view;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, filter);
        callback.refreshTitle();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
        super.onPause();
    }
}

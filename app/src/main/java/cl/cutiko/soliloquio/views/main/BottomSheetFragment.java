package cl.cutiko.soliloquio.views.main;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.adapters.SongsAdapter;
import cl.cutiko.soliloquio.background.PlayerService;
import cl.cutiko.soliloquio.models.Song;
import info.abdolahi.CircularMusicProgressBar;

public class BottomSheetFragment extends Fragment {

    private PlayerService playerService;
    private boolean isBound = false;
    private ServiceConnection serviceConnection;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private BottomSheetBehavior bottomSheetBehavior;
    private CircularMusicProgressBar circularPb;

    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Intent intent = new Intent(getContext(), PlayerService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                PlayerService.LocalBinder binder = (PlayerService.LocalBinder) iBinder;
                playerService = binder.getService();
                isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        setReceiver();
        setFilter();
    }

    private void setReceiver(){
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if (SongsAdapter.SONG_ACTION.equals(intent.getAction())) {
                        if (BottomSheetBehavior.STATE_COLLAPSED == bottomSheetBehavior.getState()) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        playerService.playSong(intent.getIntExtra(SongsAdapter.SONG_POSITION, 0));
                    } else if (SongsFragment.SONGS.equals(intent.getAction())) {
                        playerService.setSongs(intent.getStringArrayListExtra(SongsFragment.SONGS_LIST));
                    }
                }
            }
        };
    }

    private void setFilter(){
        intentFilter = new IntentFilter();
        intentFilter.addAction(SongsAdapter.SONG_ACTION);
        intentFilter.addAction(SongsFragment.SONGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circularPb = (CircularMusicProgressBar) view.findViewById(R.id.songArt);
        /*circularPb.setValue(40);*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LinearLayout bottomSheet = (LinearLayout) getActivity().findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
        }
        super.onDestroy();
    }
}

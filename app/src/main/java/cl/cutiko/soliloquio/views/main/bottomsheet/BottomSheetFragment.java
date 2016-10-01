package cl.cutiko.soliloquio.views.main.bottomsheet;


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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.adapters.SongsAdapter;
import cl.cutiko.soliloquio.background.PlayerService;
import cl.cutiko.soliloquio.views.main.tabs.SongsFragment;
import info.abdolahi.CircularMusicProgressBar;

public class BottomSheetFragment extends Fragment {

    private PlayerService playerService;
    private boolean isBound = false;
    private ServiceConnection serviceConnection;

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;

    private BottomSheetBehavior bottomSheetBehavior;
    private ImageButton playBtn, prevBtn, nextBtn;
    private CircularMusicProgressBar circularPb;
    private TextView songName;

    private static final int PLAYING = 1;
    private static final int STOPED = 0;
    private ScheduledFuture<?> updateHandler;

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
                playerService.setSongs();
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

    private void setReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    if (SongsAdapter.SONG_ACTION.equals(intent.getAction())) {
                        if (BottomSheetBehavior.STATE_COLLAPSED == bottomSheetBehavior.getState()) {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                        playerService.playSong(intent.getIntExtra(SongsAdapter.SONG_POSITION, 0));
                        setPlay();
                    } else if (SongsFragment.FILES.equals(intent.getAction())) {
                        playerService.setSongs(intent.getStringArrayListExtra(SongsFragment.FILE_LIST));
                    }
                }
            }
        };
    }

    private void setFilter() {
        intentFilter = new IntentFilter();
        intentFilter.addAction(SongsAdapter.SONG_ACTION);
        intentFilter.addAction(SongsFragment.FILES);
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
        prevBtn = (ImageButton) view.findViewById(R.id.prevBtn);
        playBtn = (ImageButton) view.findViewById(R.id.playBtn);
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        songName = (TextView) view.findViewById(R.id.songName);


        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerService.prevSong();
                updateProgress();
                setSongName();
            }
        });

        playBtn.setTag(STOPED);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePlayBtn();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerService.nextSong();
                updateProgress();
                setSongName();
            }
        });
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
        updateProgress();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        resetProgress();
    }

    private void updatePlayBtn() {
        if (STOPED == (int) playBtn.getTag()) {
            setPlay();
        } else {
            playBtn.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
            playerService.pauseSong();
            playBtn.setTag(STOPED);
            resetProgress();
            songName.setText(getString(R.string.app_name));
        }
    }

    private void setPlay(){
        playBtn.setImageResource(R.mipmap.ic_stop_white_24dp);
        playerService.resumeSong();
        playBtn.setTag(PLAYING);
        updateProgress();
        songName.setText(playerService.getSongName());
    }

    private void updateProgress() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                if (isBound && playerService.isPlaying()) {
                    final float percent = playerService.getCurrentPosition() * 100 / playerService.getDuration();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            circularPb.setValue(percent);
                        }
                    });
                }
            }
        };

        updateHandler = scheduler.scheduleWithFixedDelay(updater, 2, 2, TimeUnit.SECONDS);
    }

    private void resetProgress() {
        if (updateHandler != null) {
            updateHandler.cancel(true);
            updateHandler = null;
            circularPb.setValue(0);
        }
    }

    @Override
    public void onDestroy() {
        if (isBound) {
            getActivity().unbindService(serviceConnection);
            isBound = false;
        }
        super.onDestroy();
    }

    private void setSongName(){
        songName.setText(playerService.getSongName());
    }
}
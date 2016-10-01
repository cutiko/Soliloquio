package cl.cutiko.soliloquio.background;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import wseemann.media.FFmpegMediaMetadataRetriever;

public class PlayerService extends Service {

    public static final String PROGRESS = "cl.cutiko.soliloquio.background.PROGRESS";
    public static final String CURRENT_SONG = "cl.cutiko.soliloquio.background.CURRENT_SONG";
    public static final String CURRENT_PROGRESS = "cl.cutiko.soliloquio.background.CURRENT_PROGRESS";
    public static final String SONG_TITLE = "cl.cutiko.soliloquio.background.SONG_TITLE";

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private List<String> songs = new ArrayList<>();
    private Integer position;
    private ScheduledFuture<?> updateHandler;

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {

        public PlayerService getService() {
            return PlayerService.this;
        }

    }

    public void preparePlayer() {
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                if (updateHandler != null) {
                    updateHandler.cancel(true);
                }
                updateProgress();
                broadcastSongName();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (PlayerService.this.position != songs.size()-1) {
                    PlayerService.this.position++;
                    playSong(PlayerService.this.position);
                }
                updateHandler.cancel(true);
            }
        });
    }

    public void setSongs(List<String> songs) {
        this.songs.addAll(songs);
    }


    public void playSong(int position) {
        this.position = position;
        Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + songs.get(position));
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(this, uriSong);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void resumeSong() {
        if (position != null) {
            playSong(position);
        } else {
            playSong(0);
        }
    }

    public void prevSong() {
        if (position != null) {
            if (position == 0) {
                playSong(position);
            } else {
                position--;
                playSong(position);
            }
        }
    }

    public void nextSong() {
        if (position != null) {
            if (position == songs.size()-1) {
                playSong(position);
            } else {
                position++;
                playSong(position);
            }
        }
    }

    private void updateProgress() {
        final Intent intent = new Intent();
        intent.setAction(PROGRESS);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                final float percent = mediaPlayer.getCurrentPosition() * 100 / mediaPlayer.getDuration();
                intent.putExtra(CURRENT_PROGRESS, percent);
                LocalBroadcastManager.getInstance(PlayerService.this).sendBroadcast(intent);
            }
        };

        updateHandler = scheduler.scheduleWithFixedDelay(updater, 2, 2, TimeUnit.SECONDS);
    }

    public void broadcastSongName() {
        if (position != null) {
            Intent intent = new Intent();
            intent.setAction(CURRENT_SONG);
            Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + songs.get(position));
            FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
            mmr.setDataSource(this, uriSong);
            String title = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
            mmr.release();
            intent.putExtra(SONG_TITLE, title);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
    }
}

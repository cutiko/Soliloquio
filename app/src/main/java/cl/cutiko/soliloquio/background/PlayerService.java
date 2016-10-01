package cl.cutiko.soliloquio.background;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cl.cutiko.soliloquio.R;
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

    public void setSongs(List<String> songs) {
        this.songs.addAll(songs);
        for (String string : this.songs) {
            Log.d("FILES", string);
        }
    }

    public void playSong(int position) {
        this.position = position;
        Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + songs.get(position));
        mediaPlayer.reset();
        if (isPlaying()) {
            mediaPlayer.stop();
        }
        try {
            mediaPlayer.setDataSource(this, uriSong);
            mediaPlayer.prepareAsync();
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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stopSong() {
        if (isPlaying()) {
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

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
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












    public void setSongs() {
        Field[] files = R.raw.class.getFields();
        Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + R.raw.beso);
        Log.d("FILENAME", files[1].getName());

        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        /*mmr.setDataSource(this, uriSong);
        Log.d("ALBUM", String.valueOf(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM)));
        Log.d("TITLE", String.valueOf(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE)));
        Log.d("ARTIST", String.valueOf(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST)));
        Bitmap b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds*/
        byte [] artwork = mmr.getEmbeddedPicture();

        mmr.release();


        /*MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, uriSong);

        Log.d("AUTHOR", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)));
        Log.d("ARTIST", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)));
        Log.d("COMPOSER", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)));
        Log.d("DURATION", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));
        Log.d("TRACK_NUMBER", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)));
        Log.d("WRITER", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)));
        Log.d("GENRE", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)));
        Log.d("DATE", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)));
        Log.d("YEAR", String.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)));*/

        /*Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + R.raw.primera);
        Log.d("SONG", uriSong.getPath());

        Uri uri = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/");

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Uri example = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Log.d("EXAMPLE", example.toString());

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Log.d("CURRENT_SONG", cursor.getString(1));
            }
            cursor.close();
        } else {
            Log.d("CURSOR", "is null");
        }*/

        /*MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songsList.get(songIndex).get("songPath"));
        byte[] artBytes =  mmr.getEmbeddedPicture();
        if(artBytes!=null)
        {
            //     InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
            Bitmap bm = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            bSongImage.setImageBitmap(bm);
        }
        else
        {
            bSongImage.setImageDrawable(getResources().getDrawable(R.drawable.cmp));
        }*/

    }
}

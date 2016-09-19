package cl.cutiko.soliloquio.background;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import cl.cutiko.soliloquio.R;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class PlayerService extends Service {

    private final IBinder binder = new LocalBinder();
    private MediaPlayer mediaPlayer = new MediaPlayer();


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
    public void playSong(String songName) {
        Uri uriSong = Uri.parse(songName);
        mediaPlayer.reset();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        try {
            Log.d("CLICK", "play");
            mediaPlayer.setDataSource(this, uriSong);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSongs() {
        Field[] files = R.raw.class.getFields();
        Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + R.raw.pa);
        Log.d("FILENAME", files[1].getName());

        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        mmr.setDataSource(this, uriSong);
        Log.d("ALBUM", String.valueOf(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM)));
        Log.d("ARTIST", String.valueOf(mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST)));
        Bitmap b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds
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
                Log.d("SONG_NAME", cursor.getString(1));
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

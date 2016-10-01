package cl.cutiko.soliloquio.background;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.models.Song;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by cutiko on 28-09-16.
 */

public class SongList extends AsyncTask<Void, Integer, Boolean> {

    private List<String> songsName = new ArrayList<>();
    private List<String> filesName = new ArrayList<>();
    private Context context;

    public SongList(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        List<Song> songs = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        for(int i = 0; i < fields.length; i++){
            String fileName = fields[i].getName();
            Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + fileName);
            mmr.setDataSource(context, uriSong);
            String songName = mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
            songs.add(new Song(songName, fileName));
        }
        mmr.release();

        Collections.sort(songs, new Comparator<Song>() {
            @Override
            public int compare(Song song, Song t1) {
                return song.getName().compareTo(t1.getName());
            }
        });

        for (Song song : songs) {
            songsName.add(song.getName());
            filesName.add(song.getFile());
        }

        return true;
    }

    protected List<String> getSongsName() {
        return songsName;
    }

    protected List<String> getFilesName() {
        return filesName;
    }
}

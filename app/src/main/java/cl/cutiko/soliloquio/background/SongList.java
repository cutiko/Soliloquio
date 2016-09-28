package cl.cutiko.soliloquio.background;

import android.os.AsyncTask;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cl.cutiko.soliloquio.R;

/**
 * Created by cutiko on 28-09-16.
 */

public class SongList extends AsyncTask<Void, Integer, Boolean> {

    private List<String> songs = new ArrayList<>();

    @Override
    protected Boolean doInBackground(Void... voids) {
        Field[] fields = R.raw.class.getFields();
        for(int i = 0; i < fields.length; i++){
            songs.add(fields[i].getName());
        }
        return true;
    }

    protected List<String> getSongs() {
        return songs;
    }
}

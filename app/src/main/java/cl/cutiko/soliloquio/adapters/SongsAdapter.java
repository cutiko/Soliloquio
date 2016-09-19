package cl.cutiko.soliloquio.adapters;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cl.cutiko.soliloquio.R;

/**
 * Created by cutiko on 31-08-16.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    public static final String SONG_ACTION = "cl.cutiko.soliloquio.adapters.SongsAdpater.SONG_ACTION";
    public static final String SONG_EXTRA = "cl.cutiko.soliloquio.adapters.SongsAdpater.SONG_EXTRA";
    private List<String> songs = new ArrayList<>();
    private Context context;

    public SongsAdapter(Context context) {
        this.context = context;
        setList();
    }

    private void setList(){
        Field[] fields = R.raw.class.getFields();
        for(int i = 0; i < fields.length; i++){
            songs.add(fields[i].getName());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(songs.get(position));
        final int auxPosition = position;
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                broadcastSong("android.resource://cl.cutiko.soliloquio/raw/" + songs.get(auxPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.songName);
        }

    }

    private void broadcastSong(String songName){
        Intent broadcastSong = new Intent();
        broadcastSong.setAction(SONG_ACTION);
        broadcastSong.putExtra(SONG_EXTRA, songName);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastSong);

    }

}

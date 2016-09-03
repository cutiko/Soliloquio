package cl.cutiko.soliloquio.adapters;

import android.content.ContentUris;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
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

    private List<String> songs = new ArrayList<>();

    public SongsAdapter() {
        setList();
    }

    private void setList(){
        Field[] fields = R.raw.class.getFields();
        for(int i = 0; i < fields.length; i++){
            songs.add(fields[i].getName());
        }

        //Play it
        //int resourceID=fields[count].getInt(fields[count]);
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
                Log.d("CLICK", String.valueOf(auxPosition));
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.reset();
                try {
                    Log.d("CLICK", "play");
                    Uri uri = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + songs.get(auxPosition));
                    mediaPlayer.setDataSource(view.getContext(), uri);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.start();
                        }
                    });
                } catch (IOException e) {
                    Log.d("CLICK", "exception");
                    e.printStackTrace();
                }
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

}

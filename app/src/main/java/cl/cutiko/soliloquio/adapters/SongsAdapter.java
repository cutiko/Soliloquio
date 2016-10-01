package cl.cutiko.soliloquio.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cl.cutiko.soliloquio.R;

/**
 * Created by cutiko on 31-08-16.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    public static final String SONG_ACTION = "cl.cutiko.soliloquio.adapters.SongsAdpater.SONG_ACTION";
    public static final String SONG_POSITION = "cl.cutiko.soliloquio.adapters.SongsAdpater.SONG_POSITION";
    private List<String> songs = new ArrayList<>();
    private Context context;

    public SongsAdapter(Context context) {
        this.context = context;
    }

    public void setList(List<String> songs){
        this.songs.addAll(songs);
        notifyDataSetChanged();
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
                broadcastSong(auxPosition);
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

    private void broadcastSong(int songPosition){
        Intent broadcastSong = new Intent();
        broadcastSong.setAction(SONG_ACTION);
        broadcastSong.putExtra(SONG_POSITION, songPosition);
        LocalBroadcastManager.getInstance(context).sendBroadcast(broadcastSong);
    }

    public boolean listSet() {
        return (songs.size() > 0);
    }

}

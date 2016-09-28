package cl.cutiko.soliloquio.views.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.adapters.SongsAdapter;
import cl.cutiko.soliloquio.background.SongList;

public class SongsFragment extends Fragment {

    private SongsAdapter songsAdapter;
    public static final String SONGS = "cl.cutiko.soliloquio.views.background.SongList.SONGS";
    public static final String SONGS_LIST = "cl.cutiko.soliloquio.views.background.SongList.SONGS_LIST";
    private boolean areSongSet = false;

    public static SongsFragment newInstance() {
        return new SongsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.songsList);

        songsAdapter = new SongsAdapter(getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(songsAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!areSongSet) {
            new SetSongs().execute();
        }
    }

    private class SetSongs extends SongList {
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            List<String> songs = getSongs();
            songsAdapter.setList(songs);
            Intent sendSongs = new Intent();
            sendSongs.setAction(SONGS);
            sendSongs.putStringArrayListExtra(SONGS_LIST, (ArrayList<String>) songs);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(sendSongs);
            areSongSet = true;
        }
    }
}

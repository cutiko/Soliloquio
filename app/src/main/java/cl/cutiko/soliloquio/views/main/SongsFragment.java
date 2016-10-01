package cl.cutiko.soliloquio.views.main;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    public static final String FILES = "cl.cutiko.soliloquio.views.background.SongList.FILES";
    public static final String FILE_LIST = "cl.cutiko.soliloquio.views.background.SongList.FILE_LIST";

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
        if (!songsAdapter.listSet()) {
            new SetSongs(getContext()).execute();
        }
    }

    private class SetSongs extends SongList {
        public SetSongs(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            songsAdapter.setList(getSongsName());

            Intent sendSongs = new Intent();
            sendSongs.setAction(FILES);
            sendSongs.putStringArrayListExtra(FILE_LIST, (ArrayList<String>) getFilesName());
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(sendSongs);
        }
    }
}

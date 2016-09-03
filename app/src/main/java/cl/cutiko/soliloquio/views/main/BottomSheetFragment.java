package cl.cutiko.soliloquio.views.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cl.cutiko.soliloquio.R;
import info.abdolahi.CircularMusicProgressBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class BottomSheetFragment extends Fragment {


    public BottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CircularMusicProgressBar circularPb = (CircularMusicProgressBar) view.findViewById(R.id.songArt);
        circularPb.setValue(40);
    }
}

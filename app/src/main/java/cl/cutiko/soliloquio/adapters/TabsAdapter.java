package cl.cutiko.soliloquio.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.views.main.tabs.bio.BioFragment;
import cl.cutiko.soliloquio.views.main.tabs.SongsFragment;

/**
 * Created by cutiko on 31-08-16.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Context context;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SongsFragment.newInstance();
            case 1:
                return BioFragment.newInstance();
            default:
                return SongsFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.record);
            case 1:
                return context.getString(R.string.bio);
        }
        return "";
    }
}

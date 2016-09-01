package cl.cutiko.soliloquio.views.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.adapters.TabsAdapter;

public class MainActivity extends AppCompatActivity implements MaterialViewPager.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPager();
    }

    private void setPager(){
        setTitle("");

        MaterialViewPager mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        Toolbar toolbar = mViewPager.getToolbar();

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().hide();

        mViewPager.getViewPager().setAdapter(new TabsAdapter(getSupportFragmentManager(), this));

        mViewPager.setMaterialViewPagerListener(this);

        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Override
    public HeaderDesign getHeaderDesign(int page) {
        Context context = getApplicationContext();
        switch (page) {
            case 0:
                return HeaderDesign.fromColorAndDrawable(
                        ContextCompat.getColor(context, R.color.colorPrimaryDarker),
                        ContextCompat.getDrawable(context, R.drawable.cover));
            case 1:
                return HeaderDesign.fromColorAndDrawable(
                        ContextCompat.getColor(context, android.R.color.black),
                        ContextCompat.getDrawable(context, R.drawable.bio));
        }
        return null;
    }
}

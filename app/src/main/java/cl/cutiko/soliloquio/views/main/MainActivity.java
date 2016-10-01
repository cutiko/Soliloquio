package cl.cutiko.soliloquio.views.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.flaviofaria.kenburnsview.KenBurnsView;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.adapters.TabsAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setPager();

    }

    private void setPager(){
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), this);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(tabsAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        final KenBurnsView kenBurnsView = (KenBurnsView) findViewById(R.id.kenBurnsIv);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        kenBurnsView.setImageResource(R.mipmap.back_quote);
                        break;
                    default:
                        kenBurnsView.setImageResource(R.mipmap.front_cover);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });
    }

    @Override
    public void onBackPressed() {
    }

    //TODO jingle, el 2do tab, el share, el icono, las notificaciones

}

package cl.cutiko.soliloquio.views.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cl.cutiko.soliloquio.R;
import cl.cutiko.soliloquio.views.main.MainActivity;
import me.wangyuwei.particleview.ParticleView;


public class SplashActivity extends AppCompatActivity {

    private ParticleView particleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        particleView = (ParticleView) findViewById(R.id.introParticle);

        particleView.setOnParticleAnimListener(new ParticleView.ParticleAnimListener() {
            @Override
            public void onAnimationEnd() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                particleView.startAnim();
            }
        }, 200);
    }


}

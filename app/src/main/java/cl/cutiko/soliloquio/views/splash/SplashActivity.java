package cl.cutiko.soliloquio.views.splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

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

        final MediaPlayer mediaPlayer = new MediaPlayer();
        Uri uriSong = Uri.parse("android.resource://cl.cutiko.soliloquio/raw/" + R.raw.principios);
        try {
            mediaPlayer.setDataSource(this, uriSong);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if (mediaPlayer.isPlaying()) {
                   mediaPlayer.stop();
               }
               startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        }, 5000);

        particleView = (ParticleView) findViewById(R.id.introParticle);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                particleView.startAnim();
            }
        }, 200);
    }




}

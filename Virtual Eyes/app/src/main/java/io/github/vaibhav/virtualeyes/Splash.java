package io.github.vaibhav.virtualeyes;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {
    private ImageView iv ;
    MediaPlayer ss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        iv = (ImageView) findViewById(R.id.iv) ;
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.splash) ;
        iv.startAnimation(myanim);
        final Intent i = new Intent(Splash.this,MainActivity.class);
        ss= MediaPlayer.create(this,R.raw.ve);
        ss.start();
        Thread timer = new Thread(){
            public void run () {
                try {
                    sleep(3000) ;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();

    }
}

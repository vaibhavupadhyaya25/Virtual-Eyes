package io.github.vaibhav.virtualeyes;

import android.content.Intent;
import android.speech.tts.TextToSpeech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //private static int SPLASH_TIME=4000;
    Button btn;
    TextToSpeech tts;
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     /*   new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
              Intent s = new Intent(MainActivity.this , Splash.class) ;
                startActivity(s);

            }
        }, SPLASH_TIME);
        */


       // mToolbar = (Toolbar) findViewById(R.id.toolbar);

      //  setSupportActionBar(mToolbar);
      //  getSupportActionBar().setDisplayShowHomeEnabled(true);

        tts =new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status== TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.UK);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
                //tts.speak(" Hey there! Welcome to virtual eyes. This application will enable you to execute all your necessary tasks at one go.", TextToSpeech.QUEUE_FLUSH,null);
                //tts.speak(" Hey there! Welcome to virtual eyes. This application will enable you to execute all your necessary tasks at one go.", TextToSpeech.QUEUE_FLUSH,null);
                tts.speak(" Hey there! Welcome to virtual eyes. This application will enable you to execute all your necessary tasks at one go. Click anywhere to proceed! ", TextToSpeech.QUEUE_FLUSH,null);

            }


        });

        /*btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tts != null){
                    tts.shutdown();
                }
                Intent intent = new Intent(MainActivity.this, LogWelc.class);
                startActivity(intent);
            }
        });
        */


    }





    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if(tts != null){
                tts.shutdown();
            }
            Intent intent = new Intent(MainActivity.this, Blind.class);
            startActivity(intent);

            return true;
        } else {
            return false;
        }
    }

    /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.activity_main_menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}

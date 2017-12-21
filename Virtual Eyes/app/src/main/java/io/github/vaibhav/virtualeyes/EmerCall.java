package io.github.vaibhav.virtualeyes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class EmerCall extends AppCompatActivity {

    TextToSpeech tts;
    int flag=0;
    int result;
    String number="8377874767";
    private GestureDetectorCompat gDetect;
    Button call,exit,change,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emer_call);
        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status== TextToSpeech.SUCCESS) {
                    result=tts.setLanguage(Locale.UK);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
                tts.speak("Emergency Call Menu", TextToSpeech.QUEUE_FLUSH,null);
            }


        });

        Button call =(Button) findViewById(R.id.call);
        Button change =(Button) findViewById(R.id.change);
        Button back =(Button) findViewById(R.id.back);
        Button exit =(Button) findViewById(R.id.exit);

        call.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tts.speak("Calling Emergency Contact", TextToSpeech.QUEUE_FLUSH,null);
                Toast.makeText(EmerCall.this, "Calling : " + number, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
                return true;
            }
        });
        change.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tts.speak("Sending Alert", TextToSpeech.QUEUE_FLUSH,null);
                Intent i=new Intent(EmerCall.this,MapsActivity.class);
                startActivity(i);
                return true;
            }
        });
        back.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i=new Intent(EmerCall.this,Blind.class);
                startActivity(i);
                return true;
            }
        });
        exit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tts.speak("Exiting App!", TextToSpeech.QUEUE_FLUSH,null);
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
                return true;
            }
        });
        gDetect = new GestureDetectorCompat(this, new EmerCall.GestureListener());
        gDetect.setOnDoubleTapListener(new EmerCall.GestureListener());

    }

    public void work(View v)
    {
        switch(v.getId()) {
            case R.id.call:
                tts.speak("Emergency Call", TextToSpeech.QUEUE_FLUSH,null);

                break;
            case R.id.change:
                tts.speak("Send SOS Message", TextToSpeech.QUEUE_FLUSH,null);
                break;
            case R.id.back:
                tts.speak("Go back", TextToSpeech.QUEUE_FLUSH,null);
                break;
            case R.id.exit:
                tts.speak("Exit", TextToSpeech.QUEUE_FLUSH,null);
                break;
        }
    }



    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private float flingMin = 100;
        private float velocityMin = 100;



        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            if (flag == 0) {
                tts.speak("Tap at any corner to hear the feature implemented. Long press to select it.", TextToSpeech.QUEUE_FLUSH, null);
                flag++;
            }
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            boolean forward = false;
            boolean backward = false;
            boolean up = false;
            boolean down = false;
            float horizontalDiff = event2.getX() - event1.getX();
            float verticalDiff = event2.getY() - event1.getY();
            float absHDiff = Math.abs(horizontalDiff);
            float absVDiff = Math.abs(verticalDiff);
            float absVelocityX = Math.abs(velocityX);
            float absVelocityY = Math.abs(velocityY);

            if (absHDiff > absVDiff && absHDiff > flingMin && absVelocityX > velocityMin) {
                if (horizontalDiff > 0) backward = true;
                else forward = true;
            } else if (absVDiff > flingMin && absVelocityY > velocityMin) {
                if (verticalDiff > 0) down = true;
                else up = true;
            }
          /* if (forward) {
                //Intent i=new Intent(Blind.this,Main_Menu.class);
               // startActivity(i);
            } else if (backward) {

                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();

                else{
                   // tts.speak("....", TextToSpeech.QUEUE_FLUSH,null);
                }
            }*/
            if(down)
                tts.stop();
            if(up)
                tts.speak("Feel your device and judge the corners of the screen. Tap at any corner to hear the feature implemented. Single tap will voice the function and long press on it will select it. Double tap to enter low vision mode.", TextToSpeech.QUEUE_FLUSH, null);
            return true;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}

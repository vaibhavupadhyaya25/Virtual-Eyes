package io.github.vaibhav.virtualeyes;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Blind extends AppCompatActivity {

    TextToSpeech tts;
    int result;
    int flag=0;
    private GestureDetectorCompat gDetect;
    Button ec,exit,vc,gest;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static String EXTRA_MESSAGE = "Intent Message";
    private AudioManager mAudioManager;
    private boolean mPhoneIsSilent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blind);
        tts =new TextToSpeech(Blind.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status== TextToSpeech.SUCCESS) {
                    result=tts.setLanguage(Locale.UK);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
                tts.speak("Home Page", TextToSpeech.QUEUE_FLUSH,null);
            }


        });

        Button ec =(Button) findViewById(R.id.ec);
        Button vc =(Button) findViewById(R.id.vc);
        Button gest =(Button) findViewById(R.id.gest);
        Button exit =(Button) findViewById(R.id.exit);

        ec.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i=new Intent(Blind.this,EmerCall.class);
                startActivity(i);
                tts.stop();
                return true;
            }
        });
        vc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tts.speak("Speak Now", TextToSpeech.QUEUE_FLUSH,null);
                promptSpeechInput();
                return true;
            }
        });
        gest.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tts.speak("Quicklinks", TextToSpeech.QUEUE_FLUSH,null);
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
        gDetect = new GestureDetectorCompat(this, new GestureListener());
        gDetect.setOnDoubleTapListener(new GestureListener());

    }

    public void work(View v)
    {
        switch(v.getId()) {
            case R.id.ec:
                tts.speak("Emergency", TextToSpeech.QUEUE_FLUSH,null);

                break;
            case R.id.vc:
                tts.speak("Voice Assisstant", TextToSpeech.QUEUE_FLUSH,null);
                break;
            case R.id.gest:
                tts.speak("Quicklinks", TextToSpeech.QUEUE_FLUSH,null);
                break;
            case R.id.exit:
                tts.speak("Exit", TextToSpeech.QUEUE_FLUSH,null);
                break;
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech Prompt");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Error Occured Try again", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    //txtSpeechInput.setText(result.get(0));
                    Toast.makeText(getApplicationContext(), result.get(0), Toast.LENGTH_SHORT).show();
                    if (result.get(0).equalsIgnoreCase("silence")) {
                        if (mPhoneIsSilent) {
                            //change back to normal mode
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            mPhoneIsSilent = false;
                        } else {
                            //change to silent mode
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            mPhoneIsSilent = true;
                        }
                    }
                    else {
                        String arr[] = result.get(0).split(" ", 2);
                        String firstWord = arr[0];
                        String secondWord = arr[1];
                        switch (firstWord) {
                            //case "where":
                            //  tts.speak("Home!", TextToSpeech.QUEUE_FLUSH,null);
                            //   break;
                            case "call":
                                callPhone(secondWord);
                                break;
                            case "open":
                                Intent intent = new Intent(Intent.ACTION_MAIN, null);
                                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                                List<ResolveInfo> packageAppsList = this.getPackageManager().queryIntentActivities(intent, 0);
                                int i;

                                int counter = 0;
                                for (ResolveInfo res : packageAppsList) {
                                    if (secondWord.equalsIgnoreCase(res.loadLabel(getPackageManager()).toString())) {
                                        break;
                                    }
                                    ++counter;
                                }
                                String finalPackageName = "";
                                for (i = 1; i < packageAppsList.size(); i++) {
                                    Object obj = packageAppsList.get(i);
                                    String temp = obj.toString().split(" ")[1];
                                    String temp2 = temp.split("/")[0];
                                    if (i == counter)
                                        finalPackageName = temp2;
                                }

                                PackageManager pm = getPackageManager();
                                try {
                                    String packageName = finalPackageName;
                                    Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
                                    startActivity(launchIntent);
                                } catch (Exception e1) {
                                }

                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "Sorry couldn't catch that command. Try again", Toast.LENGTH_SHORT).show();
                                //  txtSpeechInput.setText("Sorry couldn't catch that command. Try again");
                        }
                    }
                }
            }
        }
    }

    public void callPhone(String callNumber) {
        Intent callIntent = new Intent(this, CallActivity.class);
        callIntent.putExtra(EXTRA_MESSAGE, callNumber);
        startActivity(callIntent);
    }

    private void checkIfPhoneIsSilent() {
        int ringermode = mAudioManager.getRingerMode();
        if (ringermode == AudioManager.RINGER_MODE_SILENT) {
            mPhoneIsSilent = true;
        } else {
            mPhoneIsSilent = false;
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
                tts.speak("Hello! Lets take a quick tour of virtual eyes. Feel your device and judge the corners of the screen. Tap at any corner to hear the feature implemented. Single tap will voice the function and long press on it will select it. Double tap to enter low vision mode.", TextToSpeech.QUEUE_FLUSH, null);
                flag++;
            }
            return super.onSingleTapConfirmed(event);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent i=new Intent(Blind.this,Main_Menu.class);
            startActivity(i);
            return super.onDoubleTap(e);
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
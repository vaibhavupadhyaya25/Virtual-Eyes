package io.github.vaibhav.virtualeyes;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.support.v7.widget.Toolbar;
import android.media.AudioManager;

public class Main_Menu extends AppCompatActivity {

    private Toolbar mToolbar;
    TextToSpeech tts;
    int flag=0;
    private GestureDetectorCompat gDetect;
    //private TextView txtSpeechInput;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static String EXTRA_MESSAGE = "Intent Message";
    private AudioManager mAudioManager;
    private boolean mPhoneIsSilent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
       // txtSpeechInput = (TextView) findViewById(R.id.textView);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        checkIfPhoneIsSilent();

        tts =new TextToSpeech(Main_Menu.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.UK);
                } else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (flag == 0) {
            tts.speak("Swipe left to go back to previous mode", TextToSpeech.QUEUE_FLUSH, null);
            flag++;
        }

        Button camerabutton = (Button) findViewById(R.id.cameraButton);
        Button micbutton = (Button) findViewById(R.id.microphoneButton);
        Button quickbutton = (Button) findViewById(R.id.quickButton);
        quickbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this, QuickLinks.class);
                startActivity(intent);
            }
        });

        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main_Menu.this, Ocr.class);
                startActivity(intent);
            }
        });

//        camerabutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                AlertDialog.Builder bld = new AlertDialog.Builder(Main_Menu.this);
////                bld.setTitle("Wait!");
////                bld.setMessage("Work in Progress!");
////                bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface arg0, int arg1) {
////                        Toast.makeText(getApplicationContext(), "Thank you for your cooperation", Toast.LENGTH_LONG).show();
////                    }
////                });
////                AlertDialog ad = bld.create();
////                ad.show();
//        });


        micbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AlertDialog.Builder bld = new AlertDialog.Builder(Main_Menu.this);
//                bld.setTitle("Recording");
//                bld.setMessage("Speak Up!");
//                bld.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                        Toast.makeText(getApplicationContext(), "Waiting for results", Toast.LENGTH_LONG).show();
//                    }
//                });
//                AlertDialog ad = bld.create();
//                ad.show();
                promptSpeechInput();
            }
        });

        gDetect = new GestureDetectorCompat(this, new Main_Menu.GestureListener());
        gDetect.setOnDoubleTapListener(new Main_Menu.GestureListener());

    }

    @Override
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

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private float flingMin = 100;
        private float velocityMin = 100;



        @Override
        public boolean onDown(MotionEvent event) {
            return true;
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
          if(backward) {
              Intent i = new Intent(Main_Menu.this, Blind.class);
              startActivity(i);
          }
           return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    public void activityDescribe(View v) {
        //Intent intent = new Intent(this, DescribeActivity.class);
        //startActivity(intent);
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
                            case "where":
                                tts.speak("Home!", TextToSpeech.QUEUE_FLUSH,null);
                               break;
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
}


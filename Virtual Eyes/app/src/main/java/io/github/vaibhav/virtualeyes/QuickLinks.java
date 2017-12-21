package io.github.vaibhav.virtualeyes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class QuickLinks extends AppCompatActivity{

    private Button btnSwitch;
    private Button enable;
    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Parameters params;
    ToggleButton toggle;
    private Button btn,btn2;
    private static final int CAMERA_REQUEST=10;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_links);

        toggle = (ToggleButton) findViewById(R.id.wifibttn);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleWiFi(true);
                   // toggle.setBackgroundColor(Color.parseColor("@colors/logo"));
                   // toggle.setTextColor(Color.parseColor("#ffffff"));
                  //  toggle.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_wifi_black_24dp2,0,0);
                    Toast.makeText(getApplicationContext(), "Wi-Fi Enabled!", Toast.LENGTH_LONG).show();
                } else {
                    toggleWiFi(false);
                  //  toggle.setBackgroundColor(Color.parseColor("#ffffff"));
                  //  toggle.setTextColor(Color.parseColor("#000000"));
                  //  toggle.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_wifi_black_24dp,0,0);
                    Toast.makeText(getApplicationContext(), "Wi-Fi Disabled!", Toast.LENGTH_LONG).show();
                }
            }
        });


        btn = (Button) findViewById(R.id.camerabttn);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(QuickLinks.this, "Opening App", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        });

        btn2 = (Button) findViewById(R.id.callbttn);
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Toast.makeText(QuickLinks.this, "Opening App", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0123456789"));
                startActivity(intent);
            }

        });

        btnSwitch = (Button) findViewById(R.id.flashbttn);


        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            Toast.makeText(getBaseContext(), "Device does'nt support Flashlight!", Toast.LENGTH_LONG).show();
            return;
        }

        // get the camera
        getCamera();

        // displaying button image
        //toggleButtonImage();

		/*
		 * Switch button click event to toggle flash on/off
		 */
        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isFlashOn) {
                    // turn off flash
                    turnOffFlash();
                } else {
                    // turn on flash
                    turnOnFlash();
                }
            }
        });
    }


    public void toggleWiFi(boolean status) {
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        if (status && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (!status && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }
    /*
     * Get the camera
     */
    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error: ", e.getMessage());
            }
        }
    }

    /*
     * Turning On flash
     */
    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }



            params = camera.getParameters();
            params.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
            toggleButtonImage();
        }

    }


    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
            toggleButtonImage();
        }
    }

    /*toggle switch button images changing image states to on / off
	 */
    private void toggleButtonImage() {
        if (isFlashOn) {
           //btnSwitch.setBackgroundColor(Color.parseColor("@colors/logo"));
           // btnSwitch.setTextColor(Color.parseColor("#ffffff"));
           // btnSwitch.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_lightbulb_outline_black2_24dp,0,0);
        } else {
           // btnSwitch.setBackgroundColor(Color.parseColor("#ffffff"));
          // btnSwitch.setTextColor(Color.parseColor("#000000"));
           // btnSwitch.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_lightbulb_outline_black_24dp,0,0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

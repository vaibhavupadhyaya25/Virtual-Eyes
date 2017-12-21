package io.github.vaibhav.virtualeyes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int flag=0;
    String number = "8377874767";
    TextToSpeech tts;
    Handler handler = new Handler();
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts =new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status== TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.UK);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                }

            }


        });
        //setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // mapFragment.getMapAsync(this);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);


        handler.postDelayed(new Runnable() {
            public void run() {

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if(manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {

                                if(flag==0) {
                                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                    String str = addressList.get(0).getAddressLine(0) + "," + addressList.get(0).getLocality() + "," + addressList.get(0).getCountryName();
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(number, null, "SOS MESSAGE HAS BEEN SENT TO YOU FROM THIS LOCATION : " + str, null, null);
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                    //sendIntent.putExtra("sms_body", "Current location has been sent to the emergency no");
                                    // sendIntent.setType("vnd.android-dir/mms-sms");
                                    //startActivity(sendIntent);
                                    tts.speak("Emergency Message sent to Number : "+number, TextToSpeech.QUEUE_FLUSH,null);
                                    Toast.makeText(getApplicationContext(), "Emergency Message sent to Number : "+number, Toast.LENGTH_SHORT).show();

                                    flag++;

                                    // mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.8f));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }
                else if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            LatLng latLng = new LatLng(latitude, longitude);
                            Geocoder geocoder = new Geocoder(getApplicationContext());
                            try {

                                if(flag==0) {
                                    List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                                    String str = addressList.get(0).getAddressLine(0) + "," + addressList.get(0).getLocality() + "," + addressList.get(0).getCountryName();
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(number, null, "Testing!" + str, null, null);
                                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                    //sendIntent.putExtra("sms_body", "Current location has been sent to the emergency no");
                                    //sendIntent.setType("vnd.android-dir/mms-sms");
                                    // startActivity(sendIntent);
                                    Toast.makeText(getApplicationContext(), "Emergency Message sent to Number : "+number, Toast.LENGTH_SHORT).show();
                                    flag++;

                                    // mMap.addMarker(new MarkerOptions().position(latLng).title(str));
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.8f));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle extras) {

                        }

                        @Override
                        public void onProviderEnabled(String provider) {

                        }

                        @Override
                        public void onProviderDisabled(String provider) {

                        }
                    });
                }

                finish();
            }
        }, 2000);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

}

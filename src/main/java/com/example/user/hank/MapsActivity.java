package com.example.user.hank;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MapsActivity extends AppCompatActivity  {

    private TextView textView;
    private Button button;
    private LocationManager locationManager;
    private String commadStr;
    public static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    @Override
    protected  void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        textView = (TextView)findViewById(R.id.textView);
        button = (Button)findViewById(R.id.button);
        commadStr = LocationManager.GPS_PROVIDER;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MapsActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_COARSE_LOCATION);
                    return;
                }
                locationManager.requestLocationUpdates(commadStr,1000,0,locationListener);
                Location location = locationManager.getLastKnownLocation(commadStr);
                if(location!=null)
                    textView.setText("經度"+location.getLongitude() + "\n緯度" + location.getLatitude());
                else
                    textView.setText("定位中");
            }
        });
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            textView.setText("經度:"+location.getLongitude() + "\n緯度:" + location.getLatitude());

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
    };
}


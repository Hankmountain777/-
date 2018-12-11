package com.example.user.hank;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class PopularActivity extends AppCompatActivity {

    protected ListView jsonlvobj;
    String shopno[];
    Bundle bundle;
    String userid;
    double userlat,userlng;
    private Button button;
    private LocationManager locationManager;
    private String commadStr;
    public static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular);

        Intent intent1=this.getIntent();
        bundle=intent1.getExtras();
        userid=bundle.getString("name");

        button = (Button)findViewById(R.id.button);
        commadStr = LocationManager.GPS_PROVIDER;





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if(ActivityCompat.checkSelfPermission(PopularActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(PopularActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PopularActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_COARSE_LOCATION);
                    return;
                }
                locationManager.requestLocationUpdates(commadStr,1000,0,locationListener);
                Location location = locationManager.getLastKnownLocation(commadStr);
                if(location!=null){
                    userlat=location.getLongitude();
                    userlng=location.getLatitude();
                    Func();

                }
                else
                    ;
            }
        });




    }

    /*private ListView.OnItemClickListener jsonlvobjListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            String sel=parent.getItemAtPosition(position).toString();

            //Toast.makeText(RankActivity.this,shopno[position], Toast.LENGTH_LONG).show();
           bundle.putString("name",userid);
            bundle.putString("shopInf",sel);
            //bundle.putString("shopno",shopno[position]);
            Intent intent = new Intent();
            intent.setClass(PopularActivity.this,ListviewActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            PopularActivity.this.finish();


        }
    };*/


    protected void Func() {
        jsonlvobj = (ListView) findViewById(R.id.jsonlv);
        DecimalFormat df=new DecimalFormat("#.######");

        String urlParkingArea = "http://120.110.113.104:8080/near/?lat="+df.format(userlng)+"&lng="+df.format(userlat);
       // String urlParkingArea = "http://120.110.113.104:8080/near/?lat=24.690181&lng=120.886382";

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                urlParkingArea,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        jsonbtnobj.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    parserJson(response);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );*/


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest( //JSonArray方法

                urlParkingArea,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray response) {
                      /* jsonbtnobj.setOnClickListener(new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {

                           }
                       });*/
                        try {
                            parserJsona(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, null
        );

        Volley.newRequestQueue(this).add(jsonArrayRequest);//jsonArrayRequest
        //Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    protected void parserJsona(JSONArray array) throws JSONException {
        try {
            ArrayList<String> list = new ArrayList();// Loop through the array elements
            shopno=new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                // Get current json object
                JSONObject student = array.getJSONObject(i);
                // Get the current student (json object) data
               // shopno[i]=student.getString("no");
                String firstName = student.getString("name");
                String rating = student.getString("rating");
                String addr = student.getString("formatted_address");
                // Display the formatted json data in text view
                String all = firstName + "\n" + rating + "\n" + addr + "\n";
                list.add(all);
            }
            jsonlvobj.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, list));

            //jsonlvobj.setOnItemClickListener(jsonlvobjListener);




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            userlat=location.getLongitude();
            userlng=location.getLatitude();

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







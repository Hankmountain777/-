package com.example.user.hank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ScoreActivity extends AppCompatActivity {

    protected ListView jsonlvobj;
    protected Button jsonbtnobj;
    protected EditText txt;
    String shopno[];
    Bundle bundle;
    String userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        Intent intent1=this.getIntent();
        bundle=intent1.getExtras();
        userid=bundle.getString("name");

        jsonbtnobj = (Button) findViewById(R.id.filter);
        jsonbtnobj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Func();
            }
        });

    }

    private ListView.OnItemClickListener jsonlvobjListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            String sel=parent.getItemAtPosition(position).toString();

            //Toast.makeText(RankActivity.this,shopno[position], Toast.LENGTH_LONG).show();
            bundle.putString("name",userid);
            bundle.putString("shopInf",sel);
            bundle.putString("shopno",shopno[position]);
            Intent intent = new Intent();
            intent.setClass(ScoreActivity.this,ListviewActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            ScoreActivity.this.finish();


        }
    };

    protected void Func()
    {
        jsonlvobj = (ListView) findViewById(R.id.jsonlv);
        txt =(EditText) findViewById(R.id.editText);
        String urlParkingArea = "http://120.110.113.104:8080/range/"+txt.getText().toString().trim();
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
                        try{
                            parserJsona(response);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },null
        );

        Volley.newRequestQueue(this).add(jsonArrayRequest);//jsonArrayRequest
        //Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
    protected void parserJson(JSONObject jsonObject) throws JSONException {
        ArrayList<String> list = new ArrayList();
        JSONArray data = jsonObject.getJSONObject("result").getJSONArray("results");
        for (int i = 0; i < data.length(); i++) {
            JSONObject o = data.getJSONObject(i);
            String str ="_id : "+o.getString("StartDate")+"\n"+
                    "項次 : "+o.getString("EndDate")+"\n"+
                    "停車場名稱 : "+o.getString("Title")+"\n"+
                    "經度(WGS84) : "+o.getString("Location")+"\n"+
                    "緯度(WGS84) : "+o.getString("Address");
            list.add(str);
        }
        jsonlvobj.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,list));

    }
    protected void parserJsona(JSONArray array) throws JSONException {
        try{
            ArrayList<String> list = new ArrayList();// Loop through the array elements
            shopno=new String[array.length()];
            for(int i=0;i<array.length();i++){
                // Get current json object
                JSONObject student = array.getJSONObject(i);

                // Get the current student (json object) data
                shopno[i]=student.getString("no");
                String firstName = student.getString("name");
                String rating = student.getString("rating");
                String addr = student.getString("formatted_address");
                // Display the formatted json data in text view
                String all = firstName+"\n"+rating+"\n"+addr+"\n";
                list.add(all);
            }
            jsonlvobj.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,list));
            jsonlvobj.setOnItemClickListener(jsonlvobjListener);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}



package com.example.user.hank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChooseActivity extends AppCompatActivity
{
    private static final String TAG = "ChooseActivity";
    int count=20;
    String shopno[] =new String [20];



    List<String> list;
    ListView listview;
    List<Boolean> listShow;// 這個用來記錄哪幾個 item 是被打勾的
    String userid;
    Bundle bundle;
    @Override
   public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        Intent intent1=this.getIntent();
        bundle=intent1.getExtras();
        userid=bundle.getString("name");
        Button finish = (Button)findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String selAll="";
               for(int p = 0; p<count ;p++)
               {
                   if(listview.isItemChecked(p)){
                        ratinglink(userid,shopno[p]);

                   }
               }

                bundle.putString("name",userid);
                Intent intent = new Intent();
                intent.setClass(ChooseActivity.this , Main2Activity.class);
                intent.putExtras(bundle);
                startActivity(intent);

                /* Intent intent = new Intent();
                intent.setClass(ChooseActivity.this , SpecialActivity.class);
                startActivity(intent); */
            }
        });



        listview = (ListView) findViewById(R.id.listView1);


        Func();

    }
    protected void ratinglink(String id,String no){
        StringRequest getRequest = new StringRequest("http://120.110.113.104:8080/rating/?id="+id+"&no="+no+"&rating=3",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        Volley.newRequestQueue(this).add(getRequest);



    }


    protected void Func()
    {
        String urlParkingArea = "http://120.110.113.104:8080/start";
        //jsonlvobj = (ListView) findViewById(R.id.listView1);
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

    protected void parserJsona(JSONArray array) throws JSONException {
        try{


            ArrayList<String> list = new ArrayList();// Loop through the array elements
            for(int i=0;i<array.length();i++){
                // Get current json object
                JSONObject student = array.getJSONObject(i);

                // Get the current student (json object) data
                String firstName = student.getString("name");
                shopno[i]=student.getString("no");
               // String rating = student.getString("rating");
                //String addr = student.getString("formatted_address");
                //Display the formatted json data in text view
                String all = firstName+"\n";
                list.add(all);

            }


            ArrayAdapter<String> ad = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_multiple_choice,list);

            listview.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
            listview.setAdapter(ad);




        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}

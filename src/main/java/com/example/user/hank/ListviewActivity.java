package com.example.user.hank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ListviewActivity extends AppCompatActivity {
    private RatingBar ratingone;
    public TextView txt;
    Bundle bundle;
    String shopno,userid;
    String ratingShop="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        txt=(TextView)findViewById(R.id.txt3);
        Intent intent1=this.getIntent();
        bundle=intent1.getExtras();
        String shop=bundle.getString("shopInf");
        shopno=bundle.getString("shopno");
        userid=bundle.getString("name");
       // Toast.makeText(ListviewActivity.this,userid, Toast.LENGTH_LONG).show();
        txt.setText(shop);
        ratingone = (RatingBar) findViewById(R.id.ratingone);
        ratingone.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
               ratingShop=String.valueOf(rating);
            }
        });

        Button ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratinglink(userid,shopno);
                Toast.makeText(ListviewActivity.this, "評分OK",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                bundle.putString("name",userid);
                intent.putExtras(bundle);
                intent.setClass(ListviewActivity.this , Main2Activity.class);
                startActivity(intent);


            }
        });
    }





    protected void ratinglink(String id,String no){
        StringRequest getRequest = new StringRequest("http://120.110.113.104:8080/rating/?id="+id+"&no="+no+"&rating="+ratingShop,

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
}
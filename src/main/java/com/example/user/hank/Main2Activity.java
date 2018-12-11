package com.example.user.hank;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity {
    private Button logout;
    String userid;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Intent intent1=this.getIntent();
        bundle=intent1.getExtras();
        userid=bundle.getString("name");
        bundle.putString("name",userid);






        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



        ImageButton popular = (ImageButton)findViewById(R.id.popular);
        popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this , PopularActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ImageButton rank = (ImageButton)findViewById(R.id.rank);
        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this , RankActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ImageButton score = (ImageButton)findViewById(R.id.score);
        score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this , ScoreActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        ImageButton special = (ImageButton)findViewById(R.id.special);
        special.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main2Activity.this ,SpecialActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }
}
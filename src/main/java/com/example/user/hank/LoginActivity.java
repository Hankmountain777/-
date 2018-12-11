package com.example.user.hank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

        EditText uid;
        EditText pwd;
        Button btn;
        String userid,userpwd;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            uid=(EditText) findViewById(R.id.account_edit);
            pwd=(EditText) findViewById(R.id.password_edit);
            btn=(Button)findViewById(R.id.login_button);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userid = uid.getText().toString();
                    userpwd = pwd.getText().toString();
                    ratinglink(userid,userpwd);
                }
            });

        }




        protected void ratinglink(String id,String pwd){
            StringRequest getRequest = new StringRequest("http://120.110.113.104:8080/login/?id="+id+"&pwd="+pwd,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("SUCCESS")){

                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this , Main2Activity.class);
                                Bundle bundle=new Bundle();
                                bundle.putString("name",userid);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            else if(s.equals("password error"))
                            {
                                Toast toast=Toast.makeText(LoginActivity.this,"密碼錯誤",Toast.LENGTH_LONG);
                                toast.show();
                            }
                            else if(s.equals("Never sign"))
                            {
                                Toast toast=Toast.makeText(LoginActivity.this,"此帳號未註冊",Toast.LENGTH_LONG);
                                toast.show();
                            }

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

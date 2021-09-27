package com.hanyang.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public void processRegist(View view){
        Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
        super.onActivityResult(requestCode, resultCode, Data);
        if(requestCode==0 && resultCode==RESULT_OK){
            String id=Data.getStringExtra("id");
            if(id!=null){
                EditText uidText = (EditText)  findViewById(R.id.id);
                uidText.setText(id);
            }
        }
    }

    public void processLogin(View view){
        EditText uidText = (EditText)  findViewById(R.id.id);
        String uid = uidText.getText().toString();
        EditText pwdText = (EditText)  findViewById(R.id.pwd);
        String pwd = pwdText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://new.crossplatform.co.kr:23007/Process/hanyang/member.kis?id="+uid+"&pwd="+pwd;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    try{
                        ObjectMapper mapper = new ObjectMapper();
                        HashMap data = mapper.readValue(response, HashMap.class);
                        String jsonResult = mapper.writeValueAsString(data.get("member"));
                        Member member = mapper.readValue(jsonResult, Member.class);
                        String state = (String)data.get("state");
                        String message = (String)data.get("message");
                        if(state!=null&&state.equals("loginsuccess")){
                            Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        }
                    } catch(Exception t){
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                NetworkResponse networkResponse = error.networkResponse;
                Toast.makeText(getApplicationContext(), "error"+ error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
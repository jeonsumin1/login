package com.hanyang.login;

import androidx.appcompat.app.AppCompatActivity;

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

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public void processCancel(View view){
        finish();
    }
    public void processRegist(View view){
        EditText uidText = (EditText)  findViewById(R.id.registid);
        String uid = uidText.getText().toString();
        EditText pwdText = (EditText)  findViewById(R.id.registpwd);
        String pwd = pwdText.getText().toString();
        EditText nickText = (EditText)  findViewById(R.id.registnick);
        String nick = pwdText.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://new.crossplatform.co.kr:23007/Process/hanyang/member.kis";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        try{
                            ObjectMapper mapper = new ObjectMapper();
                            HashMap data = mapper.readValue(response, HashMap.class);
                            String jsonResult = mapper.writeValueAsString(data.get("member"));
                            java.lang.reflect.Member member = mapper.readValue(jsonResult, Member.class);
                            String state = (String)data.get("state");
                            String message = (String)data.get("message");
                            if(state!=null && state.equals("success")){
                                EditText textRegistId = (EditText) findViewById(R.id.registid);
                                String id=textRegistId.getText().toString();
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("id",id);
                                setResult(RESULT_OK, intent);
                                finish();
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
        })  {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", uid);
                params.put("pwd", pwd);
                params.put("nick", nick);
                return params;
            }
        };
        queue.add(stringRequest);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }
}
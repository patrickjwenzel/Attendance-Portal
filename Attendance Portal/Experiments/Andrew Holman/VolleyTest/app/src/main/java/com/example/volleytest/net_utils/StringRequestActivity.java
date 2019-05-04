package com.example.volleytest.net_utils;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.volleytest.R;
import com.example.volleytest.app.AppController;
import com.android.volley.VolleyError;



public class StringRequestActivity extends AppCompatActivity {

    private static final String TAG = "textView2";

    private TextView myText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string_request);

        // Tag used to cancel the request
        String tag_string_req ="string_req";
        String url ="https://api.androidhive.info/volley/string_response.html";
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        StringRequest strReq =new StringRequest(Request.Method.GET,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myText = (TextView) findViewById(R.id.textView2);
                myText.setText("Response:\n" + response.toString());
                pDialog.hide();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+ error.getMessage());
                pDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



}

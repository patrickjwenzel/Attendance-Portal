package com.example.volleytest.net_utils;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.volleytest.R;
import com.example.volleytest.app.AppController;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONRequestActivity extends AppCompatActivity {

    private static final String TAG = "JSONRequestActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonrequest);
        String tag_json_obj = "json_obj_req";
        String url ="http://cs309-ad-2.misc.iastate.edu:8080/school/create";
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JSONObject demo = new JSONObject();
        try{
            demo.put("id",69);
            demo.put("name", "Joshua Loftus");

        }catch(JSONException j){
            Log.e("jsonerror", j.toString());
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,

                url, demo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                error.printStackTrace();
                pDialog.hide();
            }
        }) {
        };
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}

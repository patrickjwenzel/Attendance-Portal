package com.example.loginscreen;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.AppController;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity that allows the student to add a class by inputting a given code that is the class's id in the database.
 * It is only visible to students.
 */
public class AddClass extends AppCompatActivity {

    /**
     * Creates the page
     * @param savedInstanceState is the saved instance that holds the variables passed from StudentViewActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        System.out.println(((TextInputEditText)findViewById(R.id.input_code)).getText().toString());
        String classCode = ((TextInputEditText)findViewById(R.id.input_code)).getText().toString();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Allows the back-button at the top of the action bar function properly
     * @param item is the given item that was selected
     * @return returns true if the given items are selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Post request to the server that adds a class with the given code.
     * @param view is the view of the button that is clicked to start this method.
     */
    public void addClass(final View view){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/post/class/add/" + ((Student)getIntent().getSerializableExtra("student")).getEmail() +"/" + ((TextInputEditText)findViewById(R.id.input_code)).getText().toString() + "/" + "false";
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        String rspn = "fail";
                        try {
                            rspn = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(rspn.equals("success"))
                            rspn = "Class successfully added, restart app to see the changes";
                        else
                            rspn = "Invalid code, please try a different code to add a class";
                        ((TextView)((View)view.getParent()).findViewById(R.id.serverMessage)).setText(rspn);
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                error.printStackTrace();
                pDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}

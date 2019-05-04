package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.loginscreen.net_utils.AppController;

import static com.example.loginscreen.LoginActivity.serverResponse;

/**
 * Class used by the teacher and admin users to change the description of a course.
 */
public class ChangeCourseInfo extends AppCompatActivity {

    /**
     * The tag of the activity.
     */
    private static final String TAG = "ChangeCourseInfo";

    /**
     * Sets the text for the current course name and description.
     * @param savedInstanceState the saved bundel from the ClassInfoActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_course_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        ((TextView)findViewById(R.id.current_desc)).setText((String)intent.getSerializableExtra("course_desc"));
        ((TextView)findViewById(R.id.current_desc_header)).setText("Current description for " + (String)intent.getSerializableExtra("course_name"));

    }

    /**
     * OnClick method that executes when the new description is submitted.
     * @param view the view of the button clicked.
     */
    public void onClickNewDesc(View view)
    {
        submitNewDesc((String)getIntent().getSerializableExtra(("teacher_email")),(String)getIntent().getSerializableExtra("course_name"), ((TextView)findViewById(R.id.change_description_box)).getText().toString().replaceAll(" ", "_"));
        System.out.println(((TextView)findViewById(R.id.change_description_box)).getText().toString().replaceAll(" ", "_"));
    }

    /**
     * The actual server post method that changes the description in the server.
     * @param teacherEmail the teacher's email who is changing the description.
     * @param className the name of the course whose description is being changed.
     * @param newDesc the new description that it will be set to.
     */
    public void submitNewDesc(String teacherEmail, String className, String newDesc) {
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/teacher/post/des/add/" +teacherEmail + "/" +  className + "/" + newDesc;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((TextView)findViewById(R.id.info_change_server_message)).setText("Description has been added successfully, please reload the app to see the changes");
                Log.d("TAG", serverResponse);
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+ error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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

}

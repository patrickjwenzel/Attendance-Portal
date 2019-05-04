package com.example.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.loginscreen.net_utils.AppController;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import static com.example.loginscreen.LoginActivity.serverResponse;

/**
 * Activity that shows the class's info
 */
public class ClassInfoActivity extends AppCompatActivity {

    /**
     * The description of the class that is being displayed.
     */
    public String description = null;
    /**
     * The Course object that is being displayed
     */
    public Course course = null;
    /**
     * The tag for the activity
     */
    private static final String TAG = "TeacherHome";

    /**
     * Connects to the WebSocket used for telling students a class has stopped
     */
    public WebSocketClient wc;

    /**
     * Shows the course's name
     */
    public TextView myText = null;
    /**
     * TextView to display the course description.
     */
    public TextView courseDesc = null;
    /**
     * Course's schedule
     */
    public TextView courseSched = null;
    /**
     * Current user
     */
    public User user = null;
    /**
     * Displays who checked in
     */
    private TextView attendanceList;

    public boolean classStarted;


    /**
     * Initializes the variables, also it limits the view based on the user type.
     * @param savedInstanceState the saved bundle that gives information from the StudentView or TeacherView
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_info);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        classStarted = false;
        Intent intent = getIntent();
        initClassInfoActivity(intent);

        LinearLayout view = (LinearLayout)findViewById(R.id.activityClassInfoLayout);
        attendanceList = view.findViewById(R.id.attended_students);

        myText = (TextView) findViewById(R.id.activityClassInfoText);
        myText.setText(course.getCourseName());

        courseDesc = (TextView) findViewById(R.id.courseDescription); // Course description possible issue
        courseDesc.setText(course.getCourseDescription());


        if(user.getClass().equals(Teacher.class))
        {
            findViewById(R.id.course_id_layout).setVisibility(View.VISIBLE);
            findViewById(R.id.remove_class).setVisibility(View.GONE);
            findViewById(R.id.chat).setVisibility(View.GONE);
            findViewById(R.id.attendance_view).setVisibility(View.VISIBLE);
            findViewById(R.id.change_course_info).setVisibility(View.VISIBLE);
            findViewById(R.id.course_id_button).setVisibility(View.VISIBLE);
            findViewById(R.id.course_id_view).setVisibility(View.VISIBLE);
            if(course.isActive())
                findViewById(R.id.end_class).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.start_class).setVisibility(View.VISIBLE);
        }
        else if(intent.getSerializableExtra("taList") != null && AttendanceCard.containString(course.getCourseName(),((String[])intent.getSerializableExtra("taList"))))
        {
            findViewById(R.id.remove_class).setVisibility(View.GONE);
            findViewById(R.id.chat).setVisibility(View.GONE);
            findViewById(R.id.attendance_view).setVisibility(View.VISIBLE);
            if(course.isActive())
                findViewById(R.id.end_class).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.start_class).setVisibility(View.VISIBLE);
        }
        else if(user.getClass().equals(Admin.class))
        {
            findViewById(R.id.course_id_button).setVisibility(View.VISIBLE);
            findViewById(R.id.course_id_view).setVisibility(View.VISIBLE);
            findViewById(R.id.remove_class).setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       deleteClass(v);
                   }
               }
            );
            ((Button)findViewById(R.id.remove_class)).setText("Delete Class");
            findViewById(R.id.attendance_view).setVisibility(View.VISIBLE);
            findViewById(R.id.chat).setVisibility(View.VISIBLE);
            findViewById(R.id.change_course_info).setVisibility(View.VISIBLE);
            if(course.isActive())
                findViewById(R.id.end_class).setVisibility(View.VISIBLE);
            else
                findViewById(R.id.start_class).setVisibility(View.VISIBLE);
        }



        String cInfo = course.getCourseInfo();
        String lInfo = course.getLabInfo();
        String rInfo = course.getRecitationInfo();

        cInfo = cInfo.replaceAll("!", "");
        lInfo = lInfo.replaceAll("!", "");
        rInfo = rInfo.replaceAll("!", "");

        if(cInfo != null && cInfo.length() != 0)
        {
            findViewById(R.id.courseSchedule).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.courseSchedule)).setText("Lecture: "+ cInfo);
        }
        if(lInfo != null && lInfo.length() != 0)
        {
            findViewById(R.id.labSchedule).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.labSchedule)).setText("Lab: " + lInfo);
        }
        if(rInfo != null && rInfo.length() != 0)
        {
            findViewById(R.id.recitationSchedule).setVisibility(View.VISIBLE);
            ((TextView)findViewById(R.id.recitationSchedule)).setText("Recitation: " + rInfo);
        }

    }

    /**
     * Starts a chat activity
     * @param view Chat Button
     */
    public void chat(View view){
        Intent intent = new Intent(ClassInfoActivity.this, ChatActivity.class);
        intent.putExtra("course", course);
        intent.putExtra("user", user);
        startActivity(intent);
    }
    /**
     * Initializes the variables of the class
     * @param intent the intent that is called to receive the stored variables from the previous activity.
     */
    public void initClassInfoActivity(Intent intent)
    {
        int position = intent.getIntExtra("position",0);
        course = ((Course[])intent.getSerializableExtra("courseList"))[position];
        description = course.getCourseDescription();

        user  = (User)intent.getSerializableExtra("user");
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
     * Server call that removes a class from the users course list. App must be restarted to see the effects
     * @param view is the view of the button that is pressed.
     */
    public void removeClass(final View view){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/post/class/remove/name/" + ((Student)getIntent().getSerializableExtra("user")).getEmail() +"/" + course.getCourseName();
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
                            rspn = "Class successfully left, restart app to see the changes.";
                        else
                            rspn = "Error, failed to leave class.";
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

    /**
     * This is the onClick button for the teacher to present a class
     * @param view the view of the button
     */
    public void onClickPresent(View view)
    {
        Log.d("onClickPresent", "Presenting Class");
        present(course.getCourseName(), user, user.getLatitude(), user.getLongitude());
    }

    /**
     * The onClick method to stop a class once students have checked in.
     * @param view the view of the button pressed
     */
    public void onClickStopClass(View view)
    {
        Log.d("onClickStopClass", "Stopping Class");
        stopClass(course.getCourseName(), user);
    }

    /**
     * The actual server call to begin the class to accept students to check in.
     * @param className the name of the class
     * @param teacher the teacher object presenting the class
     * @param lat the latitude of the teacher
     * @param lon the longitude of the teacher
     */
    public void present(String className, User teacher, float lat, float lon) {
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/teacher/post/start/" + teacher.getEmail() + "/" + className + "/" + lat +
                "/" + lon ;
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("success"))
                {
                    findViewById(R.id.start_class).setVisibility(View.GONE);
                    findViewById(R.id.end_class).setVisibility(View.VISIBLE);
                    ((TextView)findViewById(R.id.serverMessage)).setText("This class is now open!");
                    initWebSockets();
                }
                else {
                    ((TextView) findViewById(R.id.serverMessage)).setText("Error, this class has already been opened today!");
                }
                serverResponse = response;
                Log.d("TAG", serverResponse);
                pDialog.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+ error.getMessage());
                pDialog.dismiss();

            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Initializes the WebSockets
     */
    public void initWebSockets(){
        Draft[] drafts = {new Draft_6455()};
        String url = "ws://cs309-ad-2.misc.iastate.edu:8080/attend/" + user.getEmail() + "/" + course.getCourseName();

        try {
            Log.d("Socket:", "Trying socket");
            wc = new WebSocketClient(new URI(url), drafts[0]) {
                /**
                 * Adds the received message to the string builder and adds it to the text view
                 * @param message Received message
                 */
                @Override
                public void onMessage(String message) {
                    Log.d(TAG, message);
                }

                /**
                 * Connected to the chat room
                 * @param handshake Server handshake
                 */
                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "connected");
                }

                /**
                 * Chat room is closed
                 * @param code Code for closing
                 * @param reason Reason for closing
                 * @param remote True or false
                 */
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                /**
                 * Error happened
                 * @param e Exception that was thrown
                 */
                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        }
        catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage());
            e.printStackTrace();
        }
        wc.connect();
    }

    public void stopClass(String className, User teacher){
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/teacher/post/stop/" + teacher.getEmail() + "/" + className;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        JsonArrayRequest strReq =new JsonArrayRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                wc.send("Closed");
                serverResponse = response.toString();
                Log.d("Stopped Class", "Stopped Class Response: "+serverResponse);
                findViewById(R.id.start_class).setVisibility(View.VISIBLE);
                findViewById(R.id.end_class).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.serverMessage)).setText(course.getCourseName()+ " has been stopped");
                Log.d("TAG", serverResponse);
                wc.close(1000, "Deliberate disconnection");
                pDialog.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("StopClass","Error: "+ error.getMessage());
                pDialog.dismiss();
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * The onClick button for the AttendanceViewer.
     * @param view the viewr of the button that is pressed.
     */
    public void onClickAttendanceViewer(View view)
    {
        Intent intent = new Intent(ClassInfoActivity.this, AttendanceViewer.class);
        intent.putExtra("className", course.getCourseName());
        startActivity(intent);
    }

    /**
     * The onClick method to change the course information
     * @param view the view of the button pressed
     */
    public void onClickChangeInfo(View view)
    {
        Intent intent = new Intent(this, ChangeCourseInfo.class);
        intent.putExtra("course_desc", course.getCourseDescription());
        intent.putExtra("course_name", course.getCourseName());
        intent.putExtra("teacher_email", user.getEmail());
        startActivity(intent);
    }


    /**
     * Server call that removes a class from the users course list. App must be restarted to see the effects
     * @param view is the view of the button that is pressed.
     */
    public void deleteClass(final View view){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/class/post/delete/" + course.getCourseName();
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
                            rspn = "Class successfully deleted, restart app to see the changes.";
                        else
                            rspn = "Error, failed to delete class.";
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
    public void onClickDisplayCode(View view)
    {
        System.out.println(getIntent().getSerializableExtra("classId"));
        ((TextView)findViewById(R.id.course_id_view)).setText((String)getIntent().getSerializableExtra("classId"));
    }

}

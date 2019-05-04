package com.example.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.AppController;
import org.json.JSONObject;

/**
 * The attendance card which shows all the attendance of the students for a given day.
 */
public class AttendanceCard extends AppCompatActivity {

    /**
     * An array of every email of attended students
     */
    String[] attendedEmails = null;
    /**
     * An array of every name of students whether they were in attendance or not.
     */
    String[] possibleNames = null;
    /**
     * An array of every email of students whether they were in attendance or not.
     */
    String[] possibleEmails = null;
    /**
     * The date that the class's attendance that is being viewed.
     */
    String date = null;
    /**
     * The name of the class currently being vieweed
     */
    String className = null;
    /**
     * The position of the student that is clicked for swap attendance.
     */
    int position = 0;

    /**
     * Creates the view and gets the information to fill all the variables. Also it initialises the grid.
     * @param savedInstanceState is the saved bundle that gives the information stored from AttendanceViewer.
     */
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        attendedEmails = (String[])intent.getSerializableExtra("attended");
        possibleNames = (String[])intent.getSerializableExtra("possible");
        possibleEmails = (String[])intent.getSerializableExtra("pEmails");
        date = (String)intent.getSerializableExtra("date");
        className = (String)intent.getSerializableExtra("className");
        position = (int)intent.getSerializableExtra("position");
        int[] timeOnPhone = (int[])intent.getSerializableExtra("timeOnPhone");



        ((TextView)findViewById(R.id.attendance_student_header)).setText("Attendance for " + className + " on " + date.substring(0,10));

        AttendanceStudentCustomGrid adapter = new AttendanceStudentCustomGrid(this, attendedEmails, possibleNames, possibleEmails, timeOnPhone);
        GridView grid = (GridView) findViewById(R.id.attendance_student_grid);
        grid.setAdapter(adapter);
    }

    /**
     * Swaps the attendance of the given student whose button was clicked
     * @param view is the view of the button that is clicked to swap the attendance.
     */
    public void onClickSwapAttendance(View view)
    {
        System.out.println(date);
        String studEmail = findEmail(((TextView)((View)view.getParent()).findViewById(R.id.student_attendance)).getText().toString());
        swapAttendance(className, studEmail, date, view);
    }

    /**
     * The actual call the the server to swap the attendance.
     * @param className the class's name
     * @param studEmail the student's email
     * @param date the date of the class meeting.
     * @param view the view of the button that was pressed.
     */
    public void swapAttendance(String className, final String studEmail, String date, final View view) {
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/teacher/post/swap/" + studEmail + "/" + className + "/" + date + "Z";
        System.out.println(url);
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        if(containString(studEmail, attendedEmails))
                        {
                            ((ImageView)((View)view.getParent()).findViewById(R.id.attendance_image)).setImageResource(R.drawable.circlebackgroundpink);
                        }
                        else
                            ((ImageView)((View)view.getParent()).findViewById(R.id.attendance_image)).setImageResource(R.drawable.circlebackgroundgreen);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }

    /**
     * Checks if a given string is in a given array
     * @param str the given string
     * @param arr the given array
     * @return true if the string is in the array and false if it is not.
     */
    public static boolean containString(String str, String[] arr)
    {
        for(int i=0; i<arr.length; i++)
        {
            if(arr[i].equals(str))
                return true;
        }
        return false;
    }

    /**
     * Gets the student's email from the string that is returned from the grid's information's card.
     * @param str is the string that is the format STUDENTNAME\nSTUDENTEMAIL
     * @return the student email.
     */
    public static String findEmail(String str)
    {
        for(int i=0; i<str.length(); i++)
        {
            if(str.charAt(i) == '\n')
            {
                return str.substring(i+1);
            }
        }
        return "fail";
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

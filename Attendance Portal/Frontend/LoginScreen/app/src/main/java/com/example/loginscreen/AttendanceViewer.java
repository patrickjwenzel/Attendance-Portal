package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.loginscreen.net_utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Is the viewer that shows a list of attendance objects for every date of a given class.
 */
public class AttendanceViewer extends AppCompatActivity {

    /**
     * The name of the class
     */
    String className = null;
    /**
     * An array of dates for every attendance object.
     */
    String[] date = null;
    /**
     * An array of ints that show the attendance numbers of each class meeting
     */
    int[] attendanceNums = null;
    /**
     * An array of ints taht show the possible attendance number of each class meeting.
     */
    int[] possibleNums = null;


    /**
     * Creates the view for the grid
     * @param savedInstanceState holds the variables from the previous activity ClassInfoActivity.
     */
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_viewer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        className = (String)intent.getSerializableExtra("className");
        getClassAttendance(className);
    }

    /**
     * The onClick method if the attendance object is clicked, it starts the AttendanceCard activity for the appropriate object.
     * @param view the view of the button clicked.
     */
    public void onClickAttendanceCard(View view)
    {
        int position = Integer.parseInt(((TextView)((View)view.getParent()).findViewById(R.id.numbered)).getText().toString());
        Intent intent = new Intent(this, AttendanceCard.class);
        getPossible(intent, position);
    }

    /**
     * The Server call to get the attendance emails of a given class. It also starts the activity of AttendanceCard here.
     * @param intent the intent that is from the previous method that starts the AttendanceCard activity.
     * @param possible the names of all students in the given class.
     * @param pEmails the emails of all students in the given class.
     * @param position the position grid which was clicked.
     */
    public void getAttendance(final Intent intent, final String[] possible, final String[] pEmails, final int position) {
        String tag_json_obj = "attendance_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/get/class/attendance/date/" + className +"/" + date[position] + "Z";
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("getAttendance", "Response" + response.toString());
                        System.out.println("The Response toString"+ response.toString());
                        String[] attendedEmails = new String[response.length()];
                        int[] timeOnPhone = new int[response.length()];
                        for(int i=0; i<attendedEmails.length; i++)
                        {
                            try {
                                attendedEmails[i] = response.getJSONObject(i).getString("userEmail");
                                timeOnPhone[i] = response.getJSONObject(i).getInt("timeOnPhone");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        intent.putExtra("timeOnPhone", timeOnPhone);
                        intent.putExtra("date", date[position]);
                        intent.putExtra("pEmails", pEmails);
                        intent.putExtra("className", className);
                        intent.putExtra("attended", attendedEmails);
                        intent.putExtra("possible", possible);
                        intent.putExtra("position", position);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: 123 " + error.getMessage());
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Gets the possible student names and emails for a given class
     * @param intent the intent that will be passed along to getAttendance
     * @param position the position of what attendance object was clicked.
     */
    public void getPossible(final Intent intent, final int position) {
        String tag_json_obj = "attendance_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/teacher/get/students/" + className;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("getPossible", "Response: " + response.toString());
                        String[] possible = new String[response.length()];
                        String[] pEmails = new String[response.length()];
                        for(int i=0; i<response.length(); i++)
                        {
                            try {
                                possible[i] = response.getJSONObject(i).getString("name");
                                pEmails[i] = response.getJSONObject(i).getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        getAttendance(intent, possible, pEmails, position);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: 123 " + error.getMessage());
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Gets the numbers to display in the grid. So the numbers are updated whenever the page is updated
     * @param className the name of the class.
     */
    public void getClassAttendance(final String className) {
        String tag_json_obj = "attendance_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/teacher/get/aInfo/" + className;
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("getClassAttendance", "Response: " + response.toString());
                        date = new String[response.length()];
                        possibleNums = new int[response.length()];
                        attendanceNums = new int[response.length()];
                        try {
                            for(int i=0; i<response.length();i++)
                            {
                                int[] twoNums = findNum(response.getString(i).substring(46));
                                date[i] = response.getString(i).substring(6, 25);
                                possibleNums[i] = twoNums[1];
                                attendanceNums[i] = twoNums[0];
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ((TextView)findViewById(R.id.attendance_header)).setText("Attendance for " + className);
                        AttendanceCustomGrid adapter = new AttendanceCustomGrid(com.example.loginscreen.AttendanceViewer.this, attendanceNums, possibleNums, date);
                        GridView grid = (GridView) findViewById(R.id.attendance_grid);
                        grid.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: 123 " + error.getMessage());
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Given a string of the format ##/## it will give the two numbers regardless the number of digits of the ints.
     * @param str the input string of format ##/##
     * @return returns an array of ints of a length 2.
     */
    public static int[] findNum(String str)
    {
        int twoNums[] = new int[2];
        String tmpStr = "";
        int i;
        for(i=0; i<str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                twoNums[0] = Integer.parseInt(tmpStr);
                break;
            }
            tmpStr += str.charAt(i);
        }
        twoNums[1] = Integer.parseInt(str.substring(i+1));
        return twoNums;
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

package com.example.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.loginscreen.LoginActivity.parseString;
import static com.example.loginscreen.LoginActivity.serverResponse;

/**
 * Shows list of whatever the admin happens to be searching for.
 */
public class AdminSearchView extends AppCompatActivity {

    /**
     * The title of the card, it is used to determined if the search view is for classes, students, or teachers.
     */
    String title = null;
    /**
     * The user that is currently viewing this.
     */
    User user = null;
    /**
     * Creates the given view of the activity.
     * @param savedInstanceState is the bundle that transfers the saved information from the AdminViewActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_view);
        Intent intent = getIntent();
        title = (String)intent.getSerializableExtra("title");
        ((TextView)findViewById(R.id.view_title)).setText(title);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        user = (Admin)intent.getSerializableExtra("user");
        getInformation("");
    }

    /**
     * OnClick method that searches through the items based on what is in the search bar
     * @param view the view of the button clicked
     */
    public void onClickAdminSearch(View view)
    {
        getInformation(((TextView)((View)view.getParent()).findViewById(R.id.search_bar)).getText().toString());
    }

    /**
     * The onClick method for the elements in the grid. It takes the admin to the relevant information based on what is being searched.
     * @param view
     */
    public void onClickViewInfo(View view)
    {

        Intent intent = null;
        String sentInfo = null;
        if(title.equals("Classes"))
        {
            intent = new Intent(this, ClassInfoActivity.class);
            sentInfo = ((TextView)((View)view.getParent()).findViewById(R.id.search_card_info)).getText().toString();
            classInfoRequest(sentInfo, intent);
        }
        else if(title.equals("Students"))
        {
            intent = new Intent(this, UserInfoActivity.class);
            sentInfo = getUserEmail(((TextView)((View)view.getParent()).findViewById(R.id.search_card_info)).getText().toString());
            studentInfoRequest(sentInfo, intent);
        }
        else if(title.equals("Teachers"))
        {
            intent = new Intent(this,  UserInfoActivity.class);
            sentInfo = getUserEmail(((TextView)((View)view.getParent()).findViewById(R.id.search_card_info)).getText().toString());
            teacherInfoRequest(sentInfo, intent);
        }
        else
        {
            intent = null;
            sentInfo = null;
            Log.d("onClickViewInfo", "Invalid Title");
        }
    }

    /**
     * Starts the parsing method to add either a class, student, or teacher.
     * @param view is the view of the button that was clicked
     */
    public void onClickAdd(View view)
    {
        Log.d("onClickAdd", "Achieved");
        Intent intent = null;
        String sentInfo = null;
        if(title.equals("Classes"))
        {
            intent = new Intent(this, AdminAddClass.class);
        }
        else if(title.equals("Students") || title.equals("Teachers"))
        {
            intent = new Intent(this, AdminAddUser.class);
        }
        else
        {
            intent = null;
            Log.d("onClickAdd", "Invalid Title");
        }
        startActivity(intent);
    }

    /**
     * Parses the user email out of a string that is formatted as STUDNAME\nSTUDEMAIL
     * @param input a string that will be parsed
     * @return the student email.
     */
    public String getUserEmail(String input)
    {
        Log.d("getUserEmail", input);
        StringBuilder stringBuilder = new StringBuilder();
        boolean startCollecting = false;
        for(int i=0; i<input.length();i++)
        {
            if(startCollecting)
            {
                stringBuilder.append(input.charAt(i));
            }
            else if(input.charAt(i)=='!' || input.charAt(i) == '\n')
            {
                startCollecting = true;
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Requests information from the server about the given class
     * @param className is the string of the class name whose information is being requested
     * @param intent the intent that is used to send the user to a new activity.
     */
    public void classInfoRequest(final String className, final Intent intent) {
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/class/get/name/" + className;
        System.out.println(url);
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                serverResponse = response.toString();
                Log.d("ClassInfoRequest", "Response: " + serverResponse);
                Course course = null;
                try {
                    course = new Course(className, 0, 0, null,null,response.getString("description")
                    , response.getString("cInfo"), response.getString("lInfo"), response.getString("rInfo"),Boolean.parseBoolean(response.getString("active")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Course[] courseList = {course};
                intent.putExtra("user", user);
                intent.putExtra("courseList",courseList);
                startActivity(intent);
                pDialog.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ClassInfoRequest","Error: "+ error.getMessage());
                pDialog.dismiss();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Requests the student information from the server and sends the user to the information activity
     * @param studEmail the email of the student being requested
     * @param intent the intent that sends the user to the user info activity.
     */
    public void studentInfoRequest(final String studEmail, final Intent intent) {
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/get/email/" + studEmail;
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = (response.getString("name"));
                    String email = (response.getString("email"));
                    String password = (response.getString("password"));
                    String[] courseNames = (parseString(response.getString("classes"), "name"));
                    String[] courseDesc = (parseString(response.getString("classes"), "description"));

                    String[] cCourseInfo = (parseString(response.getString("classes"), "cInfo"));
                    String[] lCourseInfo = (parseString(response.getString("classes"), "lInfo"));
                    String[] rCourseInfo = (parseString(response.getString("classes"), "rInfo"));

                    String[] courseActive = (parseString(response.getString("classes"), "active"));

                    //String[] attendanceArr = (parseString(response.getString("activeC"), "name"));
                    String[] taCourseNames = (parseString(response.getString("ta"), "name"));
                    String[] courseNamesTA = (parseString(response.getString("ta"), "name"));
                    String[] courseDescTA = (parseString(response.getString("ta"), "description"));

                    String[] cCourseInfoTA = (parseString(response.getString("ta"), "cInfo"));
                    String[] lCourseInfoTA = (parseString(response.getString("ta"), "lInfo"));
                    String[] rCourseInfoTA = (parseString(response.getString("ta"), "rInfo"));

                    String[] courseActiveTA = (parseString(response.getString("ta"), "active"));

                    Course[] courses = new Course[courseNames.length +taCourseNames.length];
                    boolean[] isTA = new boolean[courses.length];
                    int i;
                    for(i=0; i<courseNames.length;i++)
                    {
                        isTA[i] = false;
                        boolean active = courseActive[i].equals("true");
                        courses[i] = new Course(courseNames[i], 0, 0, null, null, courseDesc[i],cCourseInfo[i]
                                , lCourseInfo[i], rCourseInfo[i],active);
                        System.out.println("At :" + i + " Name: " + courses[i].getCourseName());
                    }
                    for(int j=i; j<taCourseNames.length+courseNames.length;j++)
                    {
                        isTA[i] = true;
                        boolean active = courseActiveTA[j-i].equals("true");
                        courses[j] = new Course(courseNamesTA[j-i], 0, 0, null, null, courseDescTA[j-i],cCourseInfoTA[j-i]
                                , lCourseInfoTA[j-i], rCourseInfoTA[j-i],active);
                        System.out.println("At :" + j + " Name: " + courses[j].getCourseName());
                    }
                    String[] courseNamesTotal = new String[courses.length];
                    for(int j=0; j<courses.length;j++)
                    {
                        courseNamesTotal[j] = courses[j].getCourseName();
                        System.out.println("Course Names: " + courseNamesTotal[j]);
                        System.out.println("Course TA: " + isTA[j]);
                    }
                    Student stud = new Student(name, email, password, courseNamesTotal, 0, 0);
                    intent.putExtra("user", stud);
                    intent.putExtra("isTA", isTA);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                serverResponse = response.toString();
                Log.d("StudentInfoRequest", "Response: " + serverResponse);
                startActivity(intent);
                pDialog.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ClassInfoRequest","Error: "+ error.getMessage());
                pDialog.dismiss();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Requests the teacher information and moves to the user info activity.
     * @param teachEmail the email of the teacher being requested
     * @param intent the intent that directs the user to the correct activity.
     */
    public void teacherInfoRequest(final String teachEmail, final Intent intent) {
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/get/email/" + teachEmail;
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.GET,
                url,null,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = (response.getString("name"));
                    String email = (response.getString("email"));
                    String password = (response.getString("password"));
                    String[] courseNames = (parseString(response.getString("classes"), "name"));
                    String[] courseDesc = (parseString(response.getString("classes"), "description"));

                    String[] cCourseInfo = (parseString(response.getString("classes"), "cInfo"));
                    String[] lCourseInfo = (parseString(response.getString("classes"), "lInfo"));
                    String[] rCourseInfo = (parseString(response.getString("classes"), "rInfo"));

                    String[] courseActive = (parseString(response.getString("classes"), "active"));

                    //String[] attendanceArr = (parseString(response.getString("activeC"), "name"));
                    String[] isTAString = (parseString(response.getString("ta"), "name"));

                    Course[] courses = new Course[courseNames.length];
                    for (int i = 0; i < courses.length; i++) {
                        boolean active = courseActive[i].equals("true");
                        courses[i] = new Course(courseNames[i], 0, 0, null, null, courseDesc[i], cCourseInfo[i]
                                , lCourseInfo[i], rCourseInfo[i], active);
                    }
                    Teacher teach = new Teacher(name, email, password, courseNames, 0, 0);
                    intent.putExtra("taList", isTAString);
                    intent.putExtra("user", teach);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                serverResponse = response.toString();
                Log.d("ClassInfoRequest", "Response: " + serverResponse);
                startActivity(intent);
                pDialog.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ClassInfoRequest","Error: "+ error.getMessage());
                pDialog.dismiss();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Gets the numbers to display in the grid. So the numbers are updated whenever the page is updated
     */
    public void getInformation(final String query) {
        String tag_json_obj = "attendance_req";
        String url = null;

        if(title.equals("Classes"))
            url = "http://cs309-ad-2.misc.iastate.edu:8080/admin/get/basic/class";
        else if(title.equals("Students"))
            url = "http://cs309-ad-2.misc.iastate.edu:8080/admin/get/basic/user";
        else if(title.equals("Teachers"))
            url = "http://cs309-ad-2.misc.iastate.edu:8080/admin/get/basic/teacher";
        else
            url = "error occurred";
        System.out.println("URL: " + url);
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d("getInformation", "Response: " + response.toString());

                        String[] info = fillStringArray(response.toString().replace('!', '\n'), query);
                        AdminSearchViewCustomGrid adapter = new AdminSearchViewCustomGrid(com.example.loginscreen.AdminSearchView.this, info);
                        GridView grid = (GridView) findViewById(R.id.search_grid);
                        grid.setAdapter(adapter);
                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: 123 " + error.getMessage());
                error.printStackTrace();
                pDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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
     * This method takes a given string from the server and puts each element into an array of strings.
     * @param str the string the server gives it in the form of \"item\",\"item\",...
     * @return an array filled with the elements in the string.
     */
    public String[] fillStringArray(String str, String query)
    {
        ArrayList<String> arr = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        boolean collecting = false;
        for(int i=0; i<str.length(); i++)
        {
            if(collecting)
            {
                if(str.charAt(i) == '"')
                {
                    collecting = false;
                    if(stringBuilder.toString().contains(query))
                        arr.add(stringBuilder.toString());
                    stringBuilder.delete(0, stringBuilder.length());
                    continue;
                }
                stringBuilder.append(str.charAt(i));
            }
            else if(str.charAt(i) == '"')
                collecting = true;
        }
        String[] strArr = new String[arr.size()];
        for(int i=0; i<strArr.length; i++)
        {
            strArr[i] = arr.get(i);
        }
        return strArr;
    }


}
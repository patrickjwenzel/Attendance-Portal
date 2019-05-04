package com.example.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.loginscreen.LoginActivity.serverResponse;


/**
 * The admin activity that allows them to view information about the user and change their classes.
 */
public class UserInfoActivity extends AppCompatActivity {
    /**
     * The user that is currently viewing the course information
     */
    public User user = null;

    /**
     * The default image ids that can be used if the classes ids are empty.
     */
    int[] defaultImageId = {
            R.drawable.circlebackgroundblue,
            R.drawable.circlebackgroundgreen,
            R.drawable.circlebackgrounddarkgreen,
            R.drawable.circlebackgroundpurple,R.drawable.circlebackgroundblue,
            R.drawable.circlebackgroundgreen,
            R.drawable.circlebackgrounddarkgreen,
            R.drawable.circlebackgroundpurple};

    /**
     * Initializes the variables, also it limits the view based on the user type.
     * @param savedInstanceState the saved bundle that gives information from the StudentView or TeacherView
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_info);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        ((TextView)findViewById(R.id.user_info_header)).setText(user.getName());
        ((TextView)findViewById(R.id.userEmail)).setText("User Email: " + user.getEmail());
        ((TextView)findViewById(R.id.userPassword)).setText("User Password: " + user.getPassword());
        if(user.getClass() == User.class)
            System.out.println("It is a user class");
        if(user.getClass() == Teacher.class)
            ((CheckBox)findViewById(R.id.teacherCheckBox)).setVisibility(View.GONE);

        UserInfoCustomGrid adapter = new UserInfoCustomGrid(this, user.getClassList(), defaultImageId, (boolean[])intent.getSerializableExtra("isTA"));
        GridView grid = (GridView) findViewById(R.id.user_info_grid);
        grid.setAdapter(adapter);
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
        if(item.getItemId()==android.R.id.home)finish();
        return super.onOptionsItemSelected(item);
    }
    /**
     * The on click method that is connected to the grid info buttons. It will send the admin to the class info page.
     * @param view the view of the button that was pressed.
     */
    public void onClickInfo(View view)
    {
        userClassInfoRequest(((TextView)((View)view.getParent()).findViewById(R.id.grid_text)).getText().toString(), new Intent(this, ClassInfoActivity.class));
    }

    /**
     * Requests information about the user's class that was clicked
     * @param className the name of the class being asked for.
     * @param intent the activity that the admin will be sent to after it runs.
     */
    public void userClassInfoRequest(final String className, final Intent intent) {
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
     * The on click method for the grid remove buttons. It removes the class from the users list of classes
     * @param view the button's view
     */
    public void onClickRemove(View view)
    {
        removeClass(view, ((TextView)((View)view.getParent()).findViewById(R.id.grid_text)).getText().toString());
    }
    /**
     * Server call that removes a class from the users course list. App must be restarted to see the effects
     * @param view is the view of the button that is pressed.
     */
    public void removeClass(final View view, String className){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/post/class/remove/name/" + user.getEmail() + "/" + className;
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
                            rspn = "Class has been successfully removed, restart app to see the changes";
                        else
                            rspn = "Class is already removed, restart app to see the changes";
                        ((TextView)findViewById(R.id.serverMessage)).setText(rspn);
                        Log.d("Remove Class", response.toString());
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
     * The on click method to add a class for a user.
     * @param view the view of the floating action button that was clicked.
     */
    public void onClickAddClass(View view)
    {
        addClass(((View)view.getParent()), ((TextView)findViewById(R.id.userAddClassBox)).getText().toString(),((CheckBox)findViewById(R.id.teacherCheckBox)).isChecked());
    }
    /**
     * Post request to the server that adds a class with the given code.
     * @param view is the view of the button that is clicked to start this method.
     */
    public void addClass(final View view, String className, Boolean teacher){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/admin/add/user/class/" + user.getEmail() +"/" + className + "/" + teacher;
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
                            rspn = "Invalid class, please try a different class name to add a class";
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
     * Server call that removes a class from the users course list. App must be restarted to see the effects
     * @param view is the view of the button that is pressed.
     */
    public void deleteUser(final View view){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/post/delete/" +user.getEmail();
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
                            rspn = "User successfully deleted, restart app to see the changes.";
                        else
                            rspn = "Error, failed to delete user.";
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

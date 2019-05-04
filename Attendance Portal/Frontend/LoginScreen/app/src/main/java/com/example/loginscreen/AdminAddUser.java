package com.example.loginscreen;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
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
public class AdminAddUser extends AppCompatActivity {
    /**
     * Is the text view for the student name.
     */
    private EditText userNameBox;
    /**
     * Is the text view for the user emailed.
     */
    private EditText userEmailBox;
    /**
     * Is the text view for the user password.
     */
    private EditText userPasswordBox;
    /**
     * The radio button that decides the user type.
     */
    private RadioGroup userTypeBox;
    /**
     * The user type of the user being created
     */
    private String userType;
    /**
     * The view that focuses the error.
     */
    private static View focusView;
    /**
     * Creates the page
     *
     * @param savedInstanceState is the saved instance that holds the variables passed from StudentViewActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_user);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        userNameBox = findViewById(R.id.user_name_input);
        userEmailBox = findViewById(R.id.user_email_input);
        userPasswordBox = findViewById(R.id.user_pass_input);
        userTypeBox = findViewById(R.id.userTypeGroup);
    }

    /**
     * The on click method for the create class button it will create the class if the necessary fields are filled.
     * @param view the view of the create class button.
     */
    public void onClickCreateStudent(View view)
    {
        final String userName = userNameBox.getText().toString();
        final String userEmail = userEmailBox.getText().toString();
        final String userPassword = userPasswordBox.getText().toString();
        final int radioType= userTypeBox.getCheckedRadioButtonId() - userTypeBox.getId();
        System.out.println(radioType);

        boolean cancel = false;
        if(TextUtils.isEmpty(userName)){
            userNameBox.setError("This field is required");
            focusView = userNameBox;
            focusView.requestFocus();
            cancel = true;
        }
        if(TextUtils.isEmpty(userEmail)){
            userEmailBox.setError("This field is required");
            focusView = userEmailBox;
            focusView.requestFocus();
            cancel = true;
        }
        if(TextUtils.isEmpty(userPassword)){
            userPasswordBox.setError("This field is required");
            focusView = userPasswordBox;
            focusView.requestFocus();
            cancel = true;
        }
        if(radioType == -103)
            userType = "student";
        else if(radioType == -102)
            userType = "teacher";
        else if(radioType == -103)
            userType = "admin";
        else{
            cancel = true;
        }
        if(!cancel)
            createUser((View)userEmailBox.getParent());
    }

    /**
     * Creates the user with the given information entered by the Admin
     * @param view the view of the button that was clicked.
     */
    public void createUser(final View view) {
        final String userName = userNameBox.getText().toString();
        final String userEmail = userEmailBox.getText().toString();
        final String userPassword = userPasswordBox.getText().toString();


        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/post/create/user/extra/" + userEmail + "/" + userPassword + "/" +
                userType + "/" + userName;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String rspn = "fail";
                        try {
                            rspn = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (rspn.equals("success"))
                            rspn = "User successfully created, restart app to see the changes";
                        else
                            rspn = "Failed, please try again";
                       // ((TextView) ((View) view.getParent()).findViewById(R.id.serverMessage)).setText(rspn);
                        ((TextView) findViewById(R.id.newUserServerMessage)).setText("User successfully created");
                        Log.d("Create User", "User Created");
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
     * Allows the back-button at the top of the action bar function properly
     *
     * @param item is the given item that was selected
     * @return returns true if the given items are selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
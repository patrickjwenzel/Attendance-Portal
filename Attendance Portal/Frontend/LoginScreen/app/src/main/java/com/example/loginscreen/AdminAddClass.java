package com.example.loginscreen;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
public class AdminAddClass extends AppCompatActivity {
    /**
     * Is the text view for the class name.
     */
    private EditText classNameBox;
    /**
     * Is the text view for the class name.
     */
    private EditText classDescBox;
    /**
     * Is the text view for the class name.
     */
    private EditText classTeacherBox;
    /**
     * Is the text view for the class name.
     */
    private EditText classLectureBox;
    /**
     * Is the text view for the class name.
     */
    private EditText classLabBox;
    /**
     * Is the text view for the class name.
     */
    private EditText classRecitationBox;
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
        setContentView(R.layout.activity_admin_add_class);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        classNameBox = findViewById(R.id.class_name_input);
        classDescBox = findViewById(R.id.class_desc_input);
        classTeacherBox = findViewById(R.id.class_teacher_input);
        classLectureBox = findViewById(R.id.class_lecture_input);
        classLabBox = findViewById(R.id.class_lab_input);
        classRecitationBox = findViewById(R.id.class_recitation_input);
    }

    /**
     * The on click method for the create class button it will create the class if the necessary fields are filled.
     * @param view the view of the create class button.
     */
    public void onClickCreateClass(View view)
    {
        final String className = classNameBox.getText().toString();
        final String classDesc = classDescBox.getText().toString();
        final String classTeacher = classTeacherBox.getText().toString();

        boolean cancel = false;
        if(TextUtils.isEmpty(className)){
            classNameBox.setError("This field is required");
            focusView = classNameBox;
            focusView.requestFocus();
            cancel = true;
        }
        if(TextUtils.isEmpty(classDesc)){
            classDescBox.setError("This field is required");
            focusView = classDescBox;
            focusView.requestFocus();
            cancel = true;
        }
        if(TextUtils.isEmpty(classTeacher)){
            classTeacherBox.setError("This field is required");
            focusView = classTeacherBox;
            focusView.requestFocus();
            cancel = true;
        }
        if(!cancel)
            createClass((View)view.getParent());
    }

    /**
     * Creates a new class based on the information in the boxes.
     * @param view the view of the create class button.
     */
    public void createClass(final View view) {
        final String className = classNameBox.getText().toString();
        final String classDesc = classDescBox.getText().toString().replaceAll(" ", "_");
        final String classTeacher = classTeacherBox.getText().toString();
        final String classLecture = "_" + classLectureBox.getText().toString().replaceAll(" ", "_");
        final String classLab = "_" + classLabBox.getText().toString().replaceAll(" ", "_");
        final String classRecitation = "_" + classRecitationBox.getText().toString().replaceAll(" ", "_");

        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/class/post/create/extra/" + className + "/" + classDesc + "/" +
                classTeacher + "/" + classLecture + "/" + classLab + "/" + classRecitation;
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("createClass", response.toString());
                        String rspn = "fail";
                        try {
                            rspn = response.getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (rspn.equals("success"))
                            rspn = "Class successfully created, restart app to see the changes";
                        else
                            rspn = "Failed, please try again";
                        ((TextView) ((View) view.getParent()).findViewById(R.id.serverMessage)).setText(rspn);
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
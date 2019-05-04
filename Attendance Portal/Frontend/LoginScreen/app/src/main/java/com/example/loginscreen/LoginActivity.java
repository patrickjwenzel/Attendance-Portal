package com.example.loginscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.AppController;
import com.example.loginscreen.net_utils.JSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * The username of the user
     */
    public static String userName;
    /**
     * The password of the user
     */
    public static String userPassword;
    /**
     * String that displays what is wrong with your password
     */
    public static StringBuilder passErr;
    /**
     * Counter that checks how many attempts you have left to get your password right
     */
    public static int passWrong;
    /**
     * JSONRequest object used for sign-in verification
     */
    public JSONRequest jsonreq;
    /**
     * The default response from the server if nothing from the server is returned
     */
    public static String serverResponse = "Default Response";
    /**
     * View for the email text line
     */
    private AutoCompleteTextView mEmailView;
    /**
     * View used for the password text line
     */
    private EditText mPasswordView;
    /**
     * Used to show the progess of logging in and getting your info verified
     */
    private View mProgressView;
    /**
     * Login view
     */
    private View mLoginFormView;
    /**
     * Button used for signing in
     */
    private Button signInBtn;
    /**
     * Used to store the current location of the user
     */
    private static Location currentLocation;
    /**
     * Used to focus on either the password or email text line if one of them is incorrect
     */
    private static View focusView;

    /**
     * Used for debugging, says which activity the error would be coming from
     */
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = findViewById(R.id.email);
        LocationUpdate l = new LocationUpdate(this, currentLocation, new Student(null, null, null, null, 0, 0));
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        jsonreq = new JSONRequest();
        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                signInBtn = findViewById(R.id.email_sign_in_button);
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        passWrong=0;
        passErr = new StringBuilder();
        passErr.append("Password ");
        jsonreq = new JSONRequest();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String userEmail = mEmailView.getText().toString();
        final String userPass = mPasswordView.getText().toString();

        //Test method that runs the admin class without needing the server.
        if(userEmail.equals("AdminTest"))
        {
            Intent intent = new Intent(this, AdminViewActivity.class);
            intent.putExtra("user",new Admin("Test Account", "Test Email", "Test Password"));
            startActivity(intent);
            return;
        }
        boolean cancel = false;
        focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(userEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(userEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Checks for a valid password
        if(TextUtils.isEmpty(userPass)){
            mPasswordView.setError("This field is required");
            focusView = mPasswordView;
            cancel=true;
        } else if(passWrong < 2 && !isPasswordValid(userPass, userName)){
            passWrong+=1;
            mPasswordView.setError(passErr.toString());
            focusView = mPasswordView;
            passErr = new StringBuilder();
            passErr.append("Password ");
            cancel=true;
        } else if(passWrong >= 2){
            focusView = mPasswordView;
            startActivity(new Intent(this, PasswordReset.class));
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else getUserResponse(userEmail, userPass);
    }

    /**
     * Attempts to log in the user and checks credentials with the server
     * @param userEmail Email the user is trying to log in with
     * @param userPassword Password the user is trying to log in with
     */
    public void getUserResponse(String userEmail, String userPassword){
        String tag_json_obj = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/get/signin/" + userEmail +"/" + userPassword;
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("onResponse has been achieved");
                        Log.d("TAG", response.toString());
                        serverResponse = response.toString();
                        boolean success = false;
                        try {
                            success = !response.getString("email").equals("failure");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(success) {
                            try {
                                String name = (response.getString("name"));
                                String email = (response.getString("email"));
                                String password = (response.getString("password"));
                                String[] classId = (parseString(response.getString("classes"), "classId"));
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
                                int i;
                                for(i=0; i<courseNames.length;i++) {
                                    boolean active = courseActive[i].equals("true");
                                    courses[i] = new Course(courseNames[i], 0, 0, null, null, courseDesc[i],cCourseInfo[i]
                                    , lCourseInfo[i], rCourseInfo[i],active);
                                    System.out.println("At :" + i + " Name: " + courses[i].getCourseName());
                                }
                                for(int j=i; j<taCourseNames.length+courseNames.length;j++) {
                                    boolean active = courseActiveTA[j-i].equals("true");
                                    courses[j] = new Course(courseNamesTA[j-i], 0, 0, null, null, courseDescTA[j-i],cCourseInfoTA[j-i]
                                            , lCourseInfoTA[j-i], rCourseInfoTA[j-i],active);
                                    System.out.println("At :" + j + " Name: " + courses[j].getCourseName());
                                }
                                String[] courseNamesTotal = new String[courses.length];
                                for(int j=0; j<courses.length;j++) {
                                    courseNamesTotal[j] = courses[j].getCourseName();
                                    System.out.println("Course Names: " + courseNamesTotal[j]);
                                }
                                User sentUser = null;
                                Intent intent = null;
                                Bundle bundle = new Bundle();
                                String type = response.getString("type");
                                if(type.equals("student")) {
                                    sentUser = new Student(name, email, password, courseNamesTotal, 0, 0);
                                    intent = new Intent(LoginActivity.this, StudentViewActivity.class);
                                    //intent.putExtra("attendance", attendanceArr);
                                    intent.putExtra("taList", taCourseNames);
                                }
                                else if(type.equals("teacher")) {
                                    sentUser = new Teacher(name, email, password, courseNamesTotal, 0, 0);
                                    intent = new Intent(LoginActivity.this, TeacherViewActivity.class);
                                    intent.putExtra("chat", "false");
                                }
                                else if(type.equals("admin")) {
                                    sentUser = new Admin(name, email, password);
                                    intent = new Intent(LoginActivity.this, AdminViewActivity.class);
                                }
                                else Toast.makeText(LoginActivity.this, "Invalid user type, please try again", Toast.LENGTH_SHORT).show();

                                intent.putExtra("user", sentUser);
                                intent.putExtra("courseList", courses);
                                intent.putExtra("classId", classId);
                                LocationUpdate update = new LocationUpdate(LoginActivity.this, currentLocation, sentUser);
                                if(!isValidUser(sentUser))
                                    Log.d(TAG, "ERROR: INVALID USER");
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                mPasswordView.setError("Server down");
                            }
                            pDialog.dismiss();

                        }
                        else {
                            mPasswordView.setError("Server down");
                            focusView = mPasswordView;
                            pDialog.dismiss();
                            return;
                        }
                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: 123 " + error.getMessage());
                error.printStackTrace();
                passWrong-= 3;
                mPasswordView.setError("Password incorrect or server Down");
                focusView = mPasswordView;
                pDialog.dismiss();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /**
     * Parses the course array to get a string array of the class names or of the course descriptions
     * @param str Class names
     * @param name Whether you want a string array of class names or class descriptions
     * @return String of the variables that you want
     */
    public static String[] parseString(String str, String name)
    {
        int addingStringIndex = 4;
        if(name.equals("classId"))
            addingStringIndex = 3;
        int index = 0;
        if(str.charAt(index)!='[')
            return new String[]{"ERROR"};
        ArrayList<String> arr = new ArrayList<>();

        while(str.contains("\"" + name + "\":"))
        {
            int i = str.indexOf("\"" + name + "\":");
            StringBuilder toAdd = new StringBuilder();
            int j;
            for(j = i+name.length()+addingStringIndex; j<str.length() && str.charAt(j) != '"' && str.charAt(j) != '}' && str.charAt(j)!=',';j++)
                toAdd.append(str.charAt(j));
            arr.add(toAdd.toString());

            str = str.substring(j);
            index++;
        }
        String[] strArr = new String[arr.size()];

        for(int i=0; i<arr.size(); i++)
        {
            strArr[i] = arr.get(i);
        }
        return strArr;
    }

    /**
     * Checks if the user is a valid user
     * @param user Given user
     * @return True if valid user, false otherwise
     */
    public boolean isValidUser(User user) {
        return user != null && user.getEmail() != null && user.getName() != null && !user.getEmail().equals("fail") && !user.getName().equals("fail");
    }

    /**
     * Checks to see if the email is a valid school or government email
     * @param email Given email you are checking
     * @return True if it is valid, false if it is not
     */
    public boolean isEmailValid(String email) {
        if((email.contains(".edu")||email.contains(".org") || email.contains(".gov")) && email.length()>0 && email.contains("@") && containsAUser(email)) return true;
        return false;
    }

    /**
     * Checks if the password contains too much of the user name
     * @param email user's email
     * @return True if the password contains too much of the user, false otherwise
     */
    public boolean containsAUser(String email){
        StringBuilder temp = new StringBuilder();
        for(int i=0; email.charAt(i) != '@';i+=1)
            temp.append(email.charAt(i));
        if(temp.length() > 0){
            userName = temp.toString();
            return true;
        }
        return false;
    }

    /**
     * Checks if the password is valid
     * @param password Given password
     * @param username Given username
     * @return True if password is valid, false otherwise
     */
    public static boolean isPasswordValid(String password, String username) {
        boolean r = true;
        int count = 0;
        if(!isLong(password)){
            passErr.append("is too short, needs "+(8-password.length())+" more characters");
            count=1;
            r=false;
        }
        if(!hasNumber(password)){
            r=false;
            if(count==0){
                passErr.append("needs a number");
                count =1;
            } else passErr.append(", needs a number");
        }
        if(!hasSpecialChar(password)){
            r=false;
            if(count==0){
                passErr.append("needs a special character");
                count =1;
            } else passErr.append(", needs a special character");
        }
        if(!hasUpperCaseChar(password)){
            r=false;
            if(count==0){
                passErr.append("needs an upper case letter");
                count =1;
            } else passErr.append(", needs an upper case letter");
        }
        if(!hasLowerCaseChar(password)){
            r=false;
            if(count==0){
                passErr.append("needs a lower case letter");
                count = 1;
            } else passErr.append(", needs an upper casee letter");
        }
        if(containsUser(password, username)){
            r=false;
            if(count==0)passErr.append("contains too much of username");
            else passErr.append(", contains too much of username");
        }
        if(!r)passErr.append(". You have "+(2-passWrong)+" attempts remaining");
        else userPassword = password;
        return r;
    }

    /**
     * Checks if the password contains too much of the username
     * @param password Given password
     * @param username Given username
     * @return True if password contains too much of username, false otherwise
     */
    public static boolean containsUser(String password, String username){
        for(int i=0; i+3 < password.length();i+=1) {
            String t = password.substring(i, i + 3);
            if (username.contains(t))return true;
        }
        return false;
    }

    /**
     * Checks if the password has a lowercase letter
     * @param p Given password
     * @return True if the password has a lowercase letter, false otherwise
     */
    public static boolean hasLowerCaseChar(String p){
        for(int i=0; i<p.length();i+=1)
            if(Character.isLetter(p.charAt(i))&& Character.isLowerCase(p.charAt(i)))return true;
        return false;
    }

    /**
     * Checks if the password is long enough
     * @param password Given password
     * @return True if password is long enough, false otherwise
     */
    public static boolean isLong(String password){
        if(password.length()>=8)return true;

        return false;
    }

    /**
     * Checks if the password contains an upper case letter
     * @param p Given Password
     * @return True if the password has an upper case letter, false otherwise
     */
    public static boolean hasUpperCaseChar(String p){
        for(int i=0; i<p.length();i+=1)
            if(Character.isLetter(p.charAt(i))&& Character.isUpperCase(p.charAt(i)))return true;
        return false;
    }

    /**
     * Checks if the password contains special character
     * @param p Given Password
     * @return True if the password has a special character, false otherwise
     */
    public static boolean hasSpecialChar(String p){
        for(int i=0; i<p.length(); i+=1)
            if(!Character.isLetter(p.charAt(i)) && !Character.isDigit(p.charAt(i)))return true;
        return false;
    }

    /**
     * Checks if the password contains a number
     * @param p Given Password
     * @return True if the password has a number, false otherwise
     */
    public static boolean hasNumber(String p){
        for(int i=0;i<p.length();i+=1)
            if(Character.isDigit(p.charAt(i)))return true;
        return false;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Launches the Password Reset activity
     * @param view Forgot Password button
     */
    public void forgotPassword(View view){
        startActivity(new Intent(this, PasswordReset.class));
    }


}



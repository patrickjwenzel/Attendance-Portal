package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.loginscreen.net_utils.AppController;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Resets the user's password
 */
public class PasswordReset extends AppCompatActivity {

    /**
     * Output text view
     */
    private static TextView passReset;

    /**
     * Checks if the user has exited the activity
     */
    private boolean back;

    /**
     * User's inputted email
     */
    private TextInputEditText email;

    /**
     * Used for debugging, says which activity the error is coming from
     */
    private static final String TAG = "PasswordResetActivity";

    /**
     * API code for RECAPTCHA SITE KEY
     */
    private static final String RECAPTCHA_API_KEY = "6LcP058UAAAAAIW5uTiEnfpIjYmOFnBhbh2aMvtJ";

    /**
     * Secret Key for RECAPTCHA
     */
    private static final String RECAPTCHA_SECRET_KEY = "6LcP058UAAAAAEWCdJcVCJfO7gnvIMftW8f7BIes";

    public String userResponseToken;

    /**
     * Response from the server
     */
    private String serverResponse;

    /**
     * User's inputted old password
     */
    private EditText password;

    /**
     * User's inputted new password
     */
    private EditText newPassword;
    
    /**
     * The username of the user
     */
    public static StringBuilder userName;

    /**
     * Returned string in test_send method
     */
    public static String r;
    
    /**
     * Counter that checks how many attempts you have left to get your password right
     */
    public static int passWrong;

    /**
     * Checks if RECAPTCHA has been passed
     */
    public boolean canChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        canChange = false;
        userName = new StringBuilder();
        passWrong = LoginActivity.passWrong;
        passReset = findViewById(R.id.passReset);
        back = false;
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        newPassword = findViewById(R.id.newPassword);
        AsteriskPasswordTransformationMethod a = new AsteriskPasswordTransformationMethod();
        password.setTransformationMethod(a);
        newPassword.setTransformationMethod(a);
    }

    /**
     * Tests sending a request to the server
     * @param oldPassword Given old password
     * @param newPass Given new password
     * @param userEmail Given user email
     * @return Returned string from the server
     */
    public String test_send(String oldPassword, String newPass, String userEmail){
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/reset/password/" + userEmail + "/" + oldPassword + "/" + newPass;
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            /**
             * Received a response from the server
             * @param response Server's response
             */
            @Override
            public void onResponse(String response) {
                r = response;
            }
        }, new Response.ErrorListener() {

            /**
             * Received an error
             * @param error Thrown error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        return r;
    }

    /**
     * Goes back to the Login Activity. Checks if you have already tried to reset your password
     * @param view Cancel button
     */
    public void cancel(View view) {
        if (passWrong > 0 && !back) passReset.setText("You must reset your password");
        else startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     * Gets the username from the email
     * @param email Given email
     * @return Username
     */
    public String getUsername(String email){
        StringBuilder u = new StringBuilder();
        for(int i=0; email.charAt(i) != '@'; i+=1){
            u.append(email.charAt(i));
        }
        userName = u;
        return userName.toString();
    }

    /**
     * Checks if the password is valid
     * @param password Given password
     * @param username Given username
     * @return True if password is valid, false otherwise
     */
    public static boolean isPasswordValid(String password, String username) {
        boolean r = true;
        if(!isLong(password))
            r=false;
        if(!hasNumber(password))
            r=false;
        if(!hasSpecialChar(password))
            r=false;
        if(!hasUpperCaseChar(password))
            r=false;
        if(containsUser(password, username))
            r=false;
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
     * Changes the letters to asterisks as you type your passwords
     */
    public class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private CharSequence mSource;
            public PasswordCharSequence(CharSequence source) {
                mSource = source; // Store char sequence
            }
            public char charAt(int index) {
                return '*'; // This is the important part
            }
            public int length() {
                return mSource.length(); // Return default
            }
            public CharSequence subSequence(int start, int end) {
                return mSource.subSequence(start, end); // Return default
            }
        }
    }

    /**
     * Checking the RECHAPTCHA check box
     * @param v RECAPTCHA
     */
    public void reset(View v) {
        SafetyNet.getClient(PasswordReset.this).verifyWithRecaptcha(RECAPTCHA_API_KEY)
                .addOnSuccessListener(PasswordReset.this,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                // Indicates communication with reCAPTCHA service was
                                // successful.
                                userResponseToken = response.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    Log.d(TAG, "RECAPTCHA success");
                                    sendRequest();
                                }
                            }
                        })
                .addOnFailureListener(PasswordReset.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ApiException) {
                            // An error occurred when communicating with the
                            // reCAPTCHA service. Refer to the status code to
                            // handle the error appropriately.
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();
                            Log.d(TAG, "Error: " + CommonStatusCodes
                                    .getStatusCodeString(statusCode));
                        } else {
                            // A different, unknown type of error occurred.
                            Log.d(TAG, "Error: " + e.getMessage());
                        }
                    }
                });
    }

    /**
     * Sends request to reset the user's password, first checks if RECAPTCHA passed
     */
    public void sendRequest(){
        String url = "https://www.google.com/recaptcha/api/siteverify";

        StringRequest sr = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("success").equals("true")){
                        canChange = true;
                        if (canChange) {
                            String oldPassword = password.getEditableText().toString();
                            final String newPass = newPassword.getEditableText().toString();
                            String userEmail = email.getEditableText().toString();
                            String tag_string_req = "string_obj_req";
                            boolean cont = false;
                            if (newPass.equals("") || oldPassword.equals("") || userEmail.equals("")) {
                                passReset.setText("Either Email, Old Password, and/or New Password Field is Empty");
                            } else if (!isPasswordValid(newPass, getUsername(userEmail))) {
                                passReset.setText("Invalid New Password. Must have 8 characters, special character, number, lower/uppercase letter");
                            } else if (newPass.equals(oldPassword))
                                passReset.setText("New Password is the Same as Old Password");
                            else cont = true;
                            if (cont) {
                                String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/reset/password/" + userEmail + "/" + oldPassword + "/" + newPass;
                                StringRequest strReq = new StringRequest(Request.Method.POST,
                                        url, new Response.Listener<String>() {

                                    /**
                                     * Received a response from the server
                                     * @param response Server's response
                                     */
                                    @Override
                                    public void onResponse(String response) {
                                        serverResponse = response;
                                        if (serverResponse.equals("Success")) {
                                            passReset.setText("Your password has been reset to: " + newPass);
                                            back = true;
                                        }
                                        Log.d("TAG", serverResponse);
                                    }
                                }, new Response.ErrorListener() {

                                    /**
                                     * Received an error
                                     * @param error Thrown error
                                     */
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        passReset.setText("Email or Password is incorrect");
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                    }
                                });
                                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PasswordReset.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("secret", RECAPTCHA_SECRET_KEY);
                params.put("response", userResponseToken);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(sr);
    }
}

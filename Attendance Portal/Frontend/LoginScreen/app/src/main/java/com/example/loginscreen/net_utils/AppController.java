package com.example.loginscreen.net_utils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.loginscreen.net_utils.LruBitmapCache;
import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to send requests to the server
 */
public class AppController extends Application {
    public static final String TAG = AppController.class
            .getSimpleName();
    private RequestQueue mRequestQueue;
    private static AppController mInstance;
    private static final String prefixURL  ="http://cs309-ad-2.misc.iastate.edu:8080/user/get/signin/";

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    /**
     * Gets the instance of the AppController
     * @return The AppController
     */
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    /**
     * Gets the request queue
     * @return The request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * Adds a request to the queue
     * @param req The request
     * @param tag Tag that checks if the request is empty
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
// set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Adds the request to the queue
     * @param req The request
     */
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests
     * @param tag Object that says what you want to cancel
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /**
     * Logs in the user
     * @param studEmail
     * @param studPass
     * @param listener
     */
    public void loginUser(String studEmail, String studPass, final CustomListener<String> listener) {
        String url = prefixURL + studEmail +"/" + studPass;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        listener.getResult(response.toString());

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
                params.put("email", "abc@androidhive.info");
                params.put("password", "password123");
                return params;
            }
        };
    }
}

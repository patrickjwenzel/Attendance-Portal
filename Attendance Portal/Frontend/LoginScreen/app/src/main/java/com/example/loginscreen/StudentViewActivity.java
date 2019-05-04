package com.example.loginscreen;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.loginscreen.net_utils.AppController;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.loginscreen.LoginActivity.serverResponse;

/**
 * Student's version of the app
 */
public class StudentViewActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    /**
     * The current location of the student.
     */
    public Location currentLocation;
    /**
     * The location update object for the student.
     */
    public LocationUpdate locationUpdate;
    /**
     * The student object that contains the user's information.
     */
    public static Student student;
    /**
     * An array of course names that the user is in.
     */
    String[] courseArr = null;
    /**
     * An array to tell which classes should be displayed as active.
     */
    boolean[] eligibleClasses;
    /**
     * An array of courses, this holds the course information that will be passed to the various activities.
     */
    Course[] courseList = null;
    /**
     * Array for checking if a user is a TA
     */
    String[] isTA = null;
    /**
     * Shows where you are for debugging
     */
    public static final String TAG = "Student Home Fragment";

    /**
     * Time student has spent on phone
     */
    public static double timeOnPhone;

    /**
     * Context of this activity
     */
    public static Context c;

    /**
     * WebSocket used for checking if a class has started and ended
     */
    public static WebSocketClient wc;

    /**
     * Checks if the class is running
     */
    public static boolean classRunning;

    /**
     * Number of minutes as an int on phone
     */
    public static int minutes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classRunning = false;
        setContentView(R.layout.activity_studentview);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        initSVA(getIntent());
        locationUpdate = new LocationUpdate(this, currentLocation, student);
        timeOnPhone = 0;
        c = this.getApplicationContext();
        Date now = new Date();
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        Log.d(TAG, simpleDateformat.format(now));
        checkEligibleClasses();
    }

    /**
     * Initalizes Variables
     * @param intent Intent with the information used to intialize vairables
     */
    public void initSVA(Intent intent) {
        student  = (Student)intent.getSerializableExtra("user");
        courseArr = student.getClassList();
        for(int i=0; i<courseArr.length; i++) {
            System.out.println(courseArr[i]);
        }
        courseList = (Course[]) intent.getSerializableExtra("courseList");
        isTA = (String[]) intent.getSerializableExtra("taList");
        eligibleClasses = new boolean[courseList.length];
        for(int i=0; i<courseList.length;i++) {
            System.out.println(i+ ": " + courseList[i].isActive());
            if(courseList[i].isActive())
                eligibleClasses[i] = true;
            else
                eligibleClasses[i] = false;
        }
    }

    /**
     * Loads the given fragment and packages the relevant information.
     * @param fragment the fragment that should be started.
     * @return true if it is successful.
     */
    private boolean loadFragment(Fragment fragment) {
        if(fragment != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",student);
            bundle.putSerializable("eligibleClasses", eligibleClasses);
            bundle.putSerializable("courseArray", courseArr);
            bundle.putSerializable("courseList", courseList);
            bundle.putSerializable("taList", isTA);
            bundle.putSerializable("classId", getIntent().getSerializableExtra("classId"));
            fragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return true;
    }

    /**
     * Called when the user moves between fragments and it updates the location every time.
     * @param item the item that it is switching to.
     * @return true if it is a success.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch(item.getItemId()){

            case R.id.navigation_home:
                locationUpdate.updateLocation();
                fragment = new HomeFragment();
                break;
            case R.id.navigation_dashboard:
                locationUpdate.updateLocation();
                fragment = new DashboardFragment();
                break;
            case R.id.navigation_notifications:
                fragment = new NotificationsFragment();
                break;

        }
        return loadFragment(fragment);
    }

    /**
     * Moves to the add class activity and packages the student object
     * @param view the view of the button that is clicked
     */
    public void addClass(View view) {
        Intent intent = new Intent(this, AddClass.class);
        intent.putExtra("student", student);
        startActivity(intent);
    }

    /**
     * The onClick for students trying to check in to a class.
     * @param view the view of the button pressed.
     */
    public void checkIn(View view) {
        String courseName = ((TextView)((View)view.getParent()).findViewById(R.id.grid_text)).getText().toString().substring(23);//Number comes from the text message in the class_grid_single xml
        float lat = student.getLatitude();
        float lon = student.getLongitude();
        String studentEmail = student.getEmail();
        checkInServer((View)view.getParent(),studentEmail, courseName, lat, lon);
    }

    /**
     * The server call to tell the server that a student has checked in, only works if the student should be able to sign in based on their location.
     * @param view the view of the button pressed.
     * @param studEmail the student email who is trying to sign in.
     * @param courseName the name of the course that the student is trying to check into.
     * @param lat the latitude of the student.
     * @param lon the longitude of the student.
     */
    public void checkInServer(final View view, String studEmail, final String courseName,float lat, float lon){
        String tag_json_obj = "checkin_button";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/student/post/attendance/" + studEmail + "/" + courseName + "/" + lat +"/"+lon;
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response)  {
                Log.d("checkin", response);
                        for(int i=0; i<eligibleClasses.length; i++)
                            if(courseArr[i].equals(courseName))
                                eligibleClasses[i] = false;
                        boolean success = false;
                            success= !response.equals("failure");
                        if(success) {
                            ((TextView) view.findViewById(R.id.attendance_status)).setText("You have been\n checked in");
                            classRunning = true;
                            checkIfOnPhone();
                            initWebSockets(courseName);
                        }
                        else ((TextView)view.findViewById(R.id.attendance_status)).setText("Check-in was\n unsuccessful");
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
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    /**
     * The onClick method to start the chat room of a class in the notification fragment.
     * @param view the view of the button pressed.
     */
    public void onClickEnterChat(View view) {
        Course course = courseList[Integer.parseInt(((TextView)((View)view.getParent()).findViewById(R.id.message_card_index)).getText().toString())];
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user", student);
        intent.putExtra("course", course);
        startActivity(intent);
    }

    /**
     * Gets the student
     * @return The student
     */
    public static Student getStudent(){ return student; }

    /**
     * Signs out
     * @param view Button
     */
    public void signOut(View view){ startActivity(new Intent(this, LoginActivity.class)); }

    /**
     * Checks if a student is on their phone once they check into a class
     */
    public void checkIfOnPhone(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();
                DisplayManager dm = (DisplayManager) c.getSystemService(c.DISPLAY_SERVICE);
                Display d = dm.getDisplay(Display.DEFAULT_DISPLAY);
                while(classRunning) {
                    if ((d.getState() != Display.STATE_OFF) && isAppIsInBackground()) {
                        if(stopwatch.isRunning()) timeOnPhone = stopwatch.getElapsedTime();
                        else{
                            timeOnPhone = stopwatch.getElapsedTime();
                            stopwatch.resume();
                        }
                    }
                    else stopwatch.stop();
                }
                stopwatch.stop();
                timeOnPhone = stopwatch.getElapsedTime();
                timeOnPhone /= 60000;
                timeOnPhone = Math.round(timeOnPhone * 10) / 10;
                minutes = (int) timeOnPhone;
                Log.d(TAG, Integer.toString(minutes));
            }
        });
    }

    /**
     * Checks if the app is in the background
     * @return True if app is in background, false otherwise
     */
    public boolean isAppIsInBackground() {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                    for (String activeProcess : processInfo.pkgList)
                        if (activeProcess.equals(c.getPackageName())) isInBackground = false;
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(c.getPackageName())) isInBackground = false;
        }
        return isInBackground;
    }

    /**
     * Initializes the WebSockets
     * @param courseName Course's name
     */
    public static void initWebSockets(final String courseName){
        Draft[] drafts = {new Draft_6455()};
        String url = "ws://cs309-ad-2.misc.iastate.edu:8080/attend/" + student.getEmail() + "/" + courseName;

        try {
            Log.d("Socket:", "Trying socket");
            wc = new WebSocketClient(new URI(url), drafts[0]) {
                /**
                 * Adds the received message to the string builder and adds it to the text view
                 * @param message Received message
                 */
                @Override
                public void onMessage(String message) {
                    if(message.equals("Closed")) {
                        classRunning = false;
                        wc.close(1000, "Deliberate disconnection");
                        sendTime(courseName, minutes, student.getEmail());
                    }
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
                public void onClose(int code, String reason, boolean remote) { Log.d("CLOSE", "onClose() returned: " + reason); }

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

    /**
     * Sends the time that the student was on their phone
     * @param courseName Course's name
     * @param minutes Amount of minutes that student spent on phone
     * @param email Student's email
     */
    public static void sendTime(final String courseName, int minutes, String email){
        String tag_string_req = "string_obj_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/user/post/time/phone/" + courseName + "/" + email + "/" + minutes;
        final ProgressDialog pDialog =new ProgressDialog(c);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Checks for eligible classes to check in to
     */
    public void checkEligibleClasses() {
        String tag_json_obj = "attendance_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/student/get/classes/active/" + student.getEmail();
        final ProgressDialog pDialog =new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        String responseStr;
                        String[] responseArr;
                        if(response == null) {
                            responseArr = new String[0];
                            responseStr = "";
                        }
                        else
                        {
                            responseStr = response.toString();
                            responseArr = (String[])LoginActivity.parseString(responseStr, "name");
                        }
                        for(int i=0; i<courseArr.length; i++)
                        {
                            eligibleClasses[i] = false;
                            for(int j= 0; j<responseArr.length; j++)
                                if(responseArr[j].equals(courseArr[i])) eligibleClasses[i] = true;
                        }
                        Log.d("checkEligibleClasses", "Response: " + response.toString());
                        loadFragment(new HomeFragment());
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
}

package com.example.loginscreen;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Teacher's side of the app
 */
public class TeacherViewActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    public Location currentLocation;
    public LocationUpdate locationUpdate;
    public static Teacher teacher;
    Course[] courseList  = null;
    String[] courseArr = null;
    public Intent intent;
    private static final String TAG = "TeacherViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_view);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        intent = getIntent();
        teacher = (Teacher)intent.getSerializableExtra("user");

        locationUpdate = new LocationUpdate(this, currentLocation, teacher);
        courseArr = teacher.getClassList();

        courseList = (Course[])intent.getSerializableExtra("courseList");
        String chat = (String)intent.getSerializableExtra("chat");
        if(chat.equals("true")) loadFragment(new NotificationsFragment());
        else loadFragment(new TeacherHome());
    }

    /**
     * Initializes the teacher view activity when using Mockito
     * @param intent The given intent
     */
    public void initTVA(Intent intent) {
        teacher  = (Teacher)intent.getSerializableExtra("user");
        courseArr = teacher.getClassList();
        String[] attendanceArr = ((String[])intent.getSerializableExtra("attendance"));
        courseList = (Course[]) intent.getSerializableExtra("courseList");
    }

    /**
     * Loads the fragment
     * @param fragment Given fragment
     * @return True if successful switch, false otherwise
     */
    private boolean loadFragment(Fragment fragment)
    {
        if(fragment != null)
        {
            Bundle bundle = new Bundle();
            bundle.putSerializable("user",teacher);
            bundle.putSerializable("courseArray", courseArr);
            bundle.putSerializable("courseList", courseList);
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
     * Checks which fragment you are trying to navigate to
     * @param item The menu item you clicked
     * @return True if the fragment is loaded, false otherwise
     */
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch(item.getItemId()){

            case R.id.navigation_home:
                locationUpdate.updateLocation();
                fragment = new TeacherHome();
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
     * Goes to the add class activity
     * @param view Button for going to this activity
     */
    public void addClass(View view) { startActivity(new Intent(this, AddClass.class)); }

    /**
     * Returns the teacher
     * @return Returns the teacher
     */
    public static Teacher getTeacher(){ return teacher; }

    /**
     * Enters the chat room for the class
     * @param view Button used to enter the chat
     */
    public void onClickEnterChat(View view){
        Course course = courseList[Integer.parseInt(((TextView)((View)view.getParent()).findViewById(R.id.message_card_index)).getText().toString())];

        Log.d(TAG, course.toString());
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user", teacher);
        intent.putExtra("course", course);
        startActivity(intent);
    }

    /**
     * Signs out
     * @param view Sign out button
     */
    public void signOut(View view){ startActivity(new Intent(this, LoginActivity.class)); }
}
package com.example.loginscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Is the homepage for the admin user. It contains a searching view for students, teachers, and classes as well as a messaging interface.
 */
public class AdminViewActivity extends AppCompatActivity {

    /**
     * The use that is viewing the activity currently.
     */
    Admin user = null;
    /**
     * Creates the view and sets the title.
     * @param savedInstanceState is the saved bundle that holds the variables transfered from LoginActivity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);
        Intent intent = getIntent();
        user = (Admin)intent.getSerializableExtra("user");
        ((TextView)findViewById(R.id.adminHomeTitle)).setText("Welcome " + user.getName());

    }

    /**
     * Parses through which button was clicked and packages the appropriate information based on the view's id. Then it starts AdminSearchView.
     * @param view the view of the button that was clicked.
     */
    public void onClickSearchView(View view)
    {
        Intent intent = new Intent(this, AdminSearchView.class);
        String buttonClicked = ((Button)view).getText().toString();
        intent.putExtra("title", buttonClicked);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    public void signOut(View view){
        startActivity(new Intent(AdminViewActivity.this, LoginActivity.class));
    }
}

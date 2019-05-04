package com.example.volleytest.net_utils;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.volleytest.R;
import com.example.volleytest.net_utils.ImageRequestActivity;
import com.example.volleytest.net_utils.JSONRequestActivity;
import com.example.volleytest.net_utils.StringRequestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void StringRequest(View view)
    {
        startActivity(new Intent(this, StringRequestActivity.class));
    }
    public void JSONRequest(View view)
    {
        startActivity(new Intent(this, JSONRequestActivity.class));
    }
    public void ImageRequest(View view)
    {
        startActivity(new Intent(this, ImageRequestActivity.class));
    }
}

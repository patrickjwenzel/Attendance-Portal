package com.example.volleytest.net_utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.volleytest.R;
import com.example.volleytest.app.AppController;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;


public class ImageRequestActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    ImageView imageView = new ImageView(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_request);

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
// If you are using normal ImageView
        imageLoader.get(Const.URL_IMAGE,new ImageLoader.ImageListener() {
            @Override
                    public void onErrorResponse(VolleyError error) {
                Log.e(TAG,"Image Load Error: "+ error.getMessage());
            }
            @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if(response.getBitmap() !=null) {
// load image into imageview
                    imageView.setImageBitmap(response.getBitmap());
                }
            }
        });
//        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
//// If you are using NetworkImageView
//        imgNetWorkView.setImageUrl(Const.URL_IMAGE, imageLoader);
//// Loading image with placeholder and error image
//        imageLoader.get(Const.URL_IMAGE, ImageLoader.getImageListener(
//                imageView, R.drawable.ico_loading, R.drawable.ico_error));
    }
}

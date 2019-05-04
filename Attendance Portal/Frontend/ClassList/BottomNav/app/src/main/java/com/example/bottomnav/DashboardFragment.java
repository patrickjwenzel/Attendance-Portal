package com.example.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


public class DashboardFragment extends Fragment implements View.OnClickListener{


    private CardView schoolCardView, restaurantCardView, hospitalCardView, libraryCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_main, container, false);


//        schoolCardView = (CardView) getView().findViewById(R.id.schoolCardViewId);
//        restaurantCardView = (CardView) getView().findViewById(R.id.restaurantCardViewId);
//        hospitalCardView = (CardView) getView().findViewById(R.id.hospitalCardViewId);
//        libraryCardView = (CardView) getView().findViewById(R.id.libraryCardViewId);
//
//        schoolCardView.setOnClickListener(this);
//        restaurantCardView.setOnClickListener(this);
//        hospitalCardView.setOnClickListener(this);
//        libraryCardView.setOnClickListener(this);

        return inflater.inflate(R.layout.fragment_dashboard, null);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch(v.getId())
        {
            case R.id.schoolCardViewId:
            {
                intent = new Intent(getActivity(), SchoolActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.libraryCardViewId:
            {
                intent = new Intent(getActivity(), LibraryActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.restaurantCardViewId:
            {
                intent = new Intent(getActivity(), RestaurantActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.hospitalCardViewId:
            {
                intent = new Intent(getActivity(), HospitalActivity.class);
                startActivity(intent);
                break;
            }

        }
    }
}

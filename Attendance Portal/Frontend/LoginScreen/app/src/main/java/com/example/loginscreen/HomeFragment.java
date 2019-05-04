package com.example.loginscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.GridView;

/**
 * The fragment that is the first thing users see. It welcomes the user and allows students to check into classes.
 */
public class HomeFragment extends Fragment {
    /**
     * The view of the grid that will display the attendance objects in a grid.
     */
    GridView grid;
    /**
     * The image ids that are used to color the cards
     */
    int[] imageId;
    /**
     * The default backgrounds that are used.
     */
    int[] defaultImageId = {
            R.drawable.circlebackgroundblue,
            R.drawable.circlebackgroundgreen,
            R.drawable.circlebackgrounddarkgreen,
            R.drawable.circlebackgroundpurple,R.drawable.circlebackgroundblue,
            R.drawable.circlebackgroundgreen,
            R.drawable.circlebackgrounddarkgreen,
            R.drawable.circlebackgroundpurple};
    /**
     * A count variable used to tell at what index the program is at.
     */
    int count = 0;

    /**
     * Creates the view of the fragment and determines which classes are active.
     * @param inflater Gives the view from the xml file
     * @param container the container that is filled from either StudentViewActivity and TeacherViewActivity
     * @param savedInstanceState holds all the stored information from the previous activity
     * @return the view that is to be displayed.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        View buttonView = inflater.inflate(R.layout.class_grid_single, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        assert getArguments() != null;
        Student student = (Student) getArguments().getSerializable("user");
        String[] courseList = (String[]) getArguments().getSerializable("courseArray");
        boolean[] eligibleClasses = (boolean[]) getArguments().getSerializable("eligibleClasses");
        TextView myText = (TextView) view.findViewById(R.id.homeTitle);
        myText.setText("Welcome " + student.getName());
        for(int i = 0; i< eligibleClasses.length; i++)
        {
            if(eligibleClasses[i])
            {
                count++;
            }
        }
        if(count==0)
            ((TextView)view.findViewById(R.id.empty_message)).setText("There are no available classes for you to check into at the moment");
        String[] eligibleClassList = new String[count];
        imageId = new int[count];
        count =0;
        for(int i = 0; i< eligibleClasses.length; i++)
        {
            if(eligibleClasses[i]) {
                eligibleClassList[count] = courseList[i];
                imageId[count] = defaultImageId[i];
                count++;
            }
        }
        ClassCustomGrid adapter = new ClassCustomGrid(getActivity(), eligibleClassList, imageId);
        grid = view.findViewById(R.id.grid);
        grid.setAdapter(adapter);
        return view;
    }
}

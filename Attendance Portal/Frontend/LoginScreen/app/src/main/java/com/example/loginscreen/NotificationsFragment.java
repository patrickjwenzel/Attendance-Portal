package com.example.loginscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

/**
 * The notification fragment of the StudentView and TeacherView activities. It contains the messages of the app.
 */
public class NotificationsFragment extends Fragment {
    /**
     * The view of the grid that will display the cards
     */
    GridView grid;
    /**
     * An array of the all the class names.
     */
    String[] web;
    /**
     * An array of all the image ids
     */
    int[] imageId;
    /**
     * The default image ids that can be used if the classes ids are empty.
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
     * Creates the view of the fragment
     * @param inflater Gives the view from the xml file
     * @param container the container that is filled from either StudentViewActivity and TeacherViewActivity
     * @param savedInstanceState holds all the stored information from the previous activity
     * @return the view that is to be displayed.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        assert getArguments() != null;
        User user = (User) getArguments().getSerializable("user");
        String[] courseList = user.getClassList();
        web = new String[courseList.length];
        imageId = new int[courseList.length];
        for(int i=0; i<courseList.length; i++)
        {
            web[i] = courseList[i];
            imageId[i] = defaultImageId[i];
        }

        MessageCustomGrid adapter = new MessageCustomGrid(getActivity(), web, imageId);
        grid = view.findViewById(R.id.message_grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Is the onClick method for the notification cards.
             * @param parent the parent of the view clicked.
             * @param view the view of hte objec that is clicked
             * @param position the position on the grid of the card that is clicked
             * @param id the id of the card that is clicked.
             */
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),ClassInfoActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("user", getArguments().getSerializable("user"));
                intent.putExtra("courseList", getArguments().getSerializable(("courseList")));
                startActivity(intent);
            }
        });
        return view;
    }
    //Does nothing, actual on click is above
    public void onClick(View v) {
    }

}

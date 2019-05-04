package com.example.loginscreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.example.loginscreen.net_utils.AppController;

import static android.view.View.GONE;

/**
 * The fragment that displays the class dashboard of the StudentViewActivity and the TeacherViewActivity.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {
    /**
     * Tag of the activity
     */
    private static final String TAG = "DashboardFragment";
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        assert getArguments() != null;
        User user = (User) getArguments().getSerializable("user");
        if(user.getClass() == Teacher.class)
        {
            view.findViewById(R.id.floatingActionButton3).setVisibility(GONE);
        }
        String[] courseList = user.getClassList();

        web = new String[courseList.length];
        imageId = new int[courseList.length];
        for(int i=0; i<courseList.length; i++)
        {
            web[i] = courseList[i];
            imageId[i] = defaultImageId[i];
        }
        CustomGrid adapter = new CustomGrid(getActivity(), web, imageId);
        grid = view.findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Is the onClick method for the dashboard cards.
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
                intent.putExtra("taList", getArguments().getSerializable("taList"));
                if(getArguments().getSerializable("user").getClass() == Teacher.class)
                    intent.putExtra("classId", ((String[])getArguments().getSerializable("classId"))[+position]);
                getClassStatis(position, (Course[])getArguments().getSerializable("courseList"), intent);
            }
        });
        return view;
    }

    //Does nothing, actual on click is above
    @Override
    public void onClick(View v) {
    }

    /**
     * A server call that gets whether the class is active or not.
     * @param position the position of the class in question
     * @param courseList an array of all courses of the user.
     * @param intent the intent that is used to direct the user to the next activity
     */
    public void getClassStatis(final int position, final Course[] courseList, final Intent intent){
        String tag_string_req = "user_req";
        String url = "http://cs309-ad-2.misc.iastate.edu:8080/student/get/activeC/" + courseList[position].getCourseName();
        final ProgressDialog pDialog =new ProgressDialog(getContext());
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                courseList[position].setActive(Boolean.parseBoolean(response.toString()));
                intent.putExtra("courseList", courseList);
                startActivity(intent);
                Log.d("Class Statis", response);
                pDialog.dismiss();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG,"Error: "+ error.getMessage());

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}

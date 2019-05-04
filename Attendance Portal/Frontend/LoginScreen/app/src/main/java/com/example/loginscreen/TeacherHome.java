package com.example.loginscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Home fragment for the teacher
 */
public class TeacherHome extends Fragment {
    private static final String TAG = "TeacherHome";
    private static Teacher teacher;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_home, null);
        super.onCreateView(inflater, container, savedInstanceState);
        teacher = (Teacher) getArguments().getSerializable("user");
        TextView welcome = view.findViewById(R.id.welcome);
        welcome.setText("Welcome " + teacher.getName() + "!");
        Log.d(TAG, teacher.getLatitude() + " " + teacher.getLongitude());
        return view;
    }
}
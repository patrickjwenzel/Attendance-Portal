package com.example.loginscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Sets up the custom grid for the attendance of every student for a given date and class.
 * This is used in the AttendanceCard activity.
 */
public class AttendanceStudentCustomGrid extends BaseAdapter{
    /**
     * Is the context of the view used, it is of AttendanceCard
     */
    private Context mContext;
    /**
     * Is the emails of every student who was in a attendance for the given day.
     */
    private final String attendedEmails[];
    /**
     * Is an array of each student's name if they are in the class for the given date.
     */
    private final String possibleStudents[];
    /**
     * Is an array of each student's email if they are in the class for the given date.
     */
    private final String possibleEmails[];
    private final int timeOnPhone[];
    private  int count;

    /**
     * Initializes all the variables for the class
     * @param c is the context of the view
     * @param attend is an array of all the student's emails who attended
     * @param possible is an array of all the student's names who are assigned to a class for that date.
     * @param pEmail is an array of all the students's emails who are assigned to a class for that date.
     */
    public AttendanceStudentCustomGrid(Context c, String attend[], String possible[], String pEmail[], int ptimeOnPhone[]) {
        mContext = c;
        this.attendedEmails = attend;
        this.possibleStudents = possible;
        this.possibleEmails = pEmail;
        this.timeOnPhone = ptimeOnPhone;
        count = 0;
    }

    /**
     * Returns the number of elements in the grid
     * @return the length of possible students if that is the larger value or attendedEmails if that is large, which would be an error if it happens.
     */
    @Override
    public int getCount() {
        if(possibleStudents.length > attendedEmails.length)
            return possibleStudents.length;
        else
            return attendedEmails.length;
    }

    /**
     * Gives the item at a given position.
     * @param position the given position
     * @return the possible student's name at a given position.
     */
    @Override
    public Object getItem(int position) {
        return possibleStudents[position];
    }

    /**
     * Gives the item ID of a given position.
     * @param position the given position
     * @return the position, this is not used.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets the view of the each grid card.
     * @param position the position of the grid card being filled.
     * @param convertView the converted view that it is starting with.
     * @param parent the parent of the convertView.
     * @return the view with all the necessary fields filled.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.attendance_student_grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.student_attendance);
            ImageView imageView = (ImageView)grid.findViewById(R.id.attendance_image);
            textView.setText(possibleStudents[position] + "\n" + possibleEmails[position]);
            if(attendedEmails != null && containString(possibleEmails[position], attendedEmails))
            {
                ((TextView)grid.findViewById(R.id.time_on_phone)).setText("Time Spent on Phone: " + timeOnPhone[count]);
                count++;
                imageView.setImageResource(R.drawable.circlebackgroundgreen);
            }
            else
                imageView.setImageResource(R.drawable.circlebackgroundpink);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

    /**
     * Checks if a given string is in a given array
     * @param str the given string
     * @param arr the given array
     * @return true if the string is in the array and false if it is not.
     */
    private boolean containString(String str, String[] arr)
    {
        for(int i=0; i<arr.length; i++)
        {
            if(arr[i].equals(str))
                return true;
        }
        return false;
    }
}

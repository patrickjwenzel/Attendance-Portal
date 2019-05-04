package com.example.loginscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Sets up the custom grid for the attendance objects of all the dates for a given class.
 * This is used in the AttendanceViewer activity.
 */
public class AttendanceCustomGrid extends BaseAdapter{
    /**
     * Is the context of the view used, it is of AttendanceViewer
     */
    private Context mContext;
    /**
     * Is a number of attended students for every class in an array.
     */
    private final int attendedStudents[];
    /**
     * Is the number of all students in a class at a given time.
     */
    private final int possibleStudents[];
    /**
     * Is the dates of each of the different attendance objects.
     */
    private final String date[];

    /**
     * Initializes the variables
     * @param c the context of the view
     * @param attend array number of the students atttended for each attendance object.
     * @param possible an array of numbers of the students in each class for a given attendance object.
     * @param tmpDate an array of dates for each attendance object.
     */
    public AttendanceCustomGrid(Context c, int attend[], int possible[], String tmpDate[]) {
        mContext = c;
        this.date = tmpDate;
        this.attendedStudents = attend;
        this.possibleStudents = possible;
    }

    /**
     * The number of attendance objects
     * @return the length of the date array.
     */
    @Override
    public int getCount() {
        return date.length;
    }

    /**
     * Gets the item at a given position
     * @param position is the given location
     * @return the date at a given position.
     */
    @Override
    public Object getItem(int position) {
        return date[position];
    }

    /**
     * Gets the item's id at a given position
     * @param position is the given position.
     * @return the position, it is not used.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets up each card in the grid view.
     * @param position is the position of the card being set.
     * @param convertView is the converted view of the original card.
     * @param parent is the parent of the grid's individual card.
     * @return returns the new view of the card once the variables are set.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.attendance_grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.attendance_detail);
            TextView dateView = (TextView)grid.findViewById(R.id.attendance_date);
            TextView numberedView = ((TextView)grid.findViewById(R.id.numbered));
            numberedView.setText(String.valueOf(position));
            textView.setText("Attendance:\n" + attendedStudents[position] + " of " + possibleStudents[position] +  " students");
            dateView.setText(date[position].substring(0,10));

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

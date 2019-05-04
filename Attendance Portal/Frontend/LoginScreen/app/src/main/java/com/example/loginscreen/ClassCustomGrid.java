package com.example.loginscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A grid manager for the attendance checkin at the home fragment of the StudentViewActivity.
 */
public class ClassCustomGrid extends BaseAdapter{
    /**
     * The context of the view of StudentViewActivity
     */
    private Context mContext;
    /**
     * The array of class names who should be displayed
     */
    private final String[] web;
    /**
     * The id for the colors of the class's images.
     */
    private final int[] id;

    /**
     * Initializes the variables.
     * @param c the given context
     * @param web the given array of class names.
     * @param imageid the given array of image ids.
     */
    public ClassCustomGrid(Context c, String[] web, int[] imageid ) {
        mContext = c;
        this.id = imageid;
        this.web = web;
    }


    /**
     * Gives the length of the grid objects
     * @return the length of the web array.
     */
    @Override
    public int getCount() {
        return web.length;
    }

    /**
     * Gives the class name at a given position
     * @param position the given position
     * @return the class name at the position.
     */
    @Override
    public Object getItem(int position) {
        return web[position];
    }

    /**
     * Gives the id of an item at a given position.
     * @param position the given position
     * @return the image id at a given position.
     */
    @Override
    public long getItemId(int position) {
        return id[position];
    }

    /**
     * Sets up the grid to be shown to the user.
     * @param position the given position of the grid card that is being filled.
     * @param convertView the current view of the card
     * @param parent the parent view of the card.
     * @return the new view that is to be displayed to the user.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.class_grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText("Check-in Available for " + web[position]);
            imageView.setImageResource(id[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

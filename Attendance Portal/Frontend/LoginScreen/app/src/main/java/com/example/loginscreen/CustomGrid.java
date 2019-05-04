package com.example.loginscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * The custom grid manager of the dashboard fragment for the classes that a user is assigned to
 */
public class CustomGrid extends BaseAdapter{
    /**
     * The context of the StudentView or TeacherView Activity.
     */
    private Context mContext;
    /**
     * The list of class names that should be displayed on the various cards.
     */
    private final String[] web;
    /**
     * The image id values for the picture that should be put on the cards.
     */
    private final int[] id;

    /**
     * Initializes the variables of the class
     * @param c the given context from on of the ViewActivities
     * @param web the array of names of classes which should be displayed.
     * @param imageid the array of image ids of classes which should be displayed.
     */
    public CustomGrid(Context c,String[] web,int[] imageid ) {
        mContext = c;
        this.id = imageid;
        this.web = web;
    }

    /**
     * Gives the number of items in the grid
     * @return returns the length of the class name array.
     */
    @Override
    public int getCount(){
        return web.length;
    }

    /**
     * Gives the item at a given position
     * @param position the given position
     * @return the name of the class at the position.
     */
    @Override
    public Object getItem(int position) {
        return web[position];
    }

    /**
     * Gives the item id of the item at a given position.
     * @param position the given position
     * @return the image id value at the position.
     */
    @Override
    public long getItemId(int position) {
        return id[position];
    }

    /**
     * Sets the view of each card in the grid.
     * @param position the position of the card that is being set.
     * @param convertView the original view of the card.
     * @param parent the view of the parent of the card
     * @return the new view with filled fields that should be displayed ot the user.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(web[position]);
            imageView.setImageResource(id[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

package com.example.loginscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Tells the grid that is displayed in the notification fragment how it is to be laid out.
 */
public class MessageCustomGrid extends BaseAdapter{
    /**
     * Context of the view
     */
    private Context mContext;
    /**
     * An array of all the class names being displayed
     */
    private final String classNames[];
    /**
     * An array of image ids that tell color the background of the card
     */
    private final int imageId[];

    /**
     * Initializes the variables
     * @param c the context of the card being created
     * @param classNames the given array of class names
     * @param imageId the given integer array of image ids
     */
    public MessageCustomGrid(Context c, String[] classNames, int[] imageId) {
        mContext = c;
        this.classNames = classNames;
        this.imageId = imageId;
    }

    /**
     * Gives the length of the grid
     * @return the number of items in the classNames array
     */
    @Override
    public int getCount() {
        return classNames.length;
    }

    /**
     * Gives the item at a given position
     * @param position the given position
     * @return the name at a given position
     */
    @Override
    public Object getItem(int position) {
        return classNames[position];
    }

    /**
     * Gives the id of an item at a given index
     * @param position the given position
     * @return the position, this method is not used.
     */
    @Override
    public long getItemId(int position){
        return position;
    }

    /**
     * Sets up the message cards.
     * @param position the given position of the card being created
     * @param convertView is the current view of the card.
     * @param parent is the parent of the current view.
     * @return the new view that should be displayed as a card to the user.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.message_grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_message_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_message_image);
            textView.setText("Enter Chat Room for " + classNames[position]);
            imageView.setImageResource(imageId[position]);
            ((TextView)grid.findViewById(R.id.message_card_index)).setText(String.valueOf(position));

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

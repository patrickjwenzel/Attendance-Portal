package com.example.loginscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Tells the grid that is displayed in AdminSearchView how it is to be laid out.
 */
public class AdminSearchViewCustomGrid extends BaseAdapter{
    /**
     * Context of the view
     */
    private Context mContext;
    /**
     * Stores all the information that we want to display on the card.
     */
    private final String info[];

    public AdminSearchViewCustomGrid(Context c, String info[]) {
        mContext = c;
        this.info = info;
    }

    /**
     * Gets the number of items in the grid.
     * @return the length of the information array.
     */
    @Override
    public int getCount() {
        return info.length;
    }

    /**
     * Gets the item at a given position
     * @param position the given position
     * @return the item at the given position
     */
    @Override
    public Object getItem(int position) {
        return info[position];
    }

    /**
     * Gives the item ID at a given position
     * @param position the given position
     * @return the position, this method is not used so it doesn't need to mean much.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets the variables of the individual grid cards.
     * @param position is the position of the individual grid card.
     * @param convertView is the view it wants the variable to be converted into.
     * @param parent is the parent of the converted view.
     * @return Returns the view of the card you clicked
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.admin_view_grid_single, null);
            ((TextView)grid.findViewById(R.id.search_card_info)).setText(info[position]);

        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}

package id.ac.ukdw.ti.yuanlukito.wifiposition.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.ac.ukdw.ti.yuanlukito.wifiposition.R;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;

/**
 * Created by Yuan Lukito on 3/26/2015.
 */
public class LocationListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Location> locations;

    public LocationListAdapter(Activity activity, ArrayList<Location> locations){
        this.activity = activity;
        this.locations = locations;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Location getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null)
            convertView = inflater.inflate(R.layout.location_list_item, null);
        TextView textViewLocationName = (TextView) convertView.findViewById(R.id.textViewLocationName);
        TextView textViewLocationId = (TextView) convertView.findViewById(R.id.textViewLocationId);
        ImageView imageViewPosition = (ImageView) convertView.findViewById(R.id.imageViewLocation);
        imageViewPosition.setImageResource(R.drawable.location);
        Location location = locations.get(position);
        textViewLocationName.setText(location.getName());
        textViewLocationId.setText("ID: " + location.getId());

        return convertView;
    }
}

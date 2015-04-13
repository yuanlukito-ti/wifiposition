package id.ac.ukdw.ti.yuanlukito.wifiposition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.ApplicationContextProvider;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.LocationListAdapter;


public class LocationDataSetActivity extends ActionBarActivity {

    private ListView listViewLocations;
    private ArrayList<Location> locations;
    LocationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_dataset);

        //list view
        listViewLocations = (ListView) findViewById(R.id.listViewLocations);

        locations = new ArrayList<>();
        //get locations list
        final DataSource dataSource = new DataSource(this);
        try {
            dataSource.open();
            locations = dataSource.getLocationList();
            adapter = new LocationListAdapter(this, locations);
            //list view adapter
            listViewLocations.setAdapter(adapter);
            dataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //list view long tap handler
        listViewLocations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Location location = locations.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(LocationDataSetActivity.this);
                builder.setMessage("Do you want to delete this location: " + location.getName() + "?")
                       .setPositiveButton("Yes, delete this location", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               try {
                                   dataSource.open();
                                   //delete this location
                                   dataSource.deleteLocation(location.getId());

                                   //notify adapter that location data has changed
                                   locations.remove(position);
                                   adapter.notifyDataSetChanged();
                                   dataSource.close();
                               } catch (SQLException e) {
                                   e.printStackTrace();
                               }
                           }
                       })
                       .setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {

                           }
                       });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_data_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

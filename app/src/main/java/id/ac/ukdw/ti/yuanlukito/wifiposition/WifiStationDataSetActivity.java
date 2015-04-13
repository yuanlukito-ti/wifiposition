package id.ac.ukdw.ti.yuanlukito.wifiposition;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.WifiStationDatasetListAdapter;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.WifiStationSortByBssid;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.WifiStationSortBySsid;

public class WifiStationDataSetActivity extends ActionBarActivity {
    private ArrayList<WifiStation> stations;
    private WifiStationDatasetListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_station_dataset);

        ListView listViewWifiStation = (ListView) findViewById(R.id.listViewWifiStationDataset);
        final DataSource dataSource = new DataSource(this);
        try{
            dataSource.open();
            stations = dataSource.getAllWifiStations();
            Collections.sort(stations, new WifiStationSortBySsid());
            adapter = new WifiStationDatasetListAdapter(this, stations);
            listViewWifiStation.setAdapter(adapter);
            dataSource.close();
        } catch (SQLException e) {

        }

        //listview long tap handler
        listViewWifiStation.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final WifiStation wifiStation = stations.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(WifiStationDataSetActivity.this);
                builder.setMessage("Do you want to delete this WiFi Station: \n SSID: " + wifiStation.getSsid() + ", Bssid: " + wifiStation.getBssid())
                       .setPositiveButton("Yes, delete this Wifi Station", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               try {
                                   dataSource.open();
                                   //delete this wifi station
                                   dataSource.deleteWifiStation(wifiStation.getBssid());
                                   //notify adapter that wifi station data has changed
                                   stations.remove(position);
                                   Collections.sort(stations, new WifiStationSortBySsid());
                                   adapter.notifyDataSetChanged();
                                   dataSource.close();
                               } catch (SQLException e) {

                               }
                           }
                       })
                       .setNegativeButton("No, thanks", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wifi_station_data_set, menu);
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

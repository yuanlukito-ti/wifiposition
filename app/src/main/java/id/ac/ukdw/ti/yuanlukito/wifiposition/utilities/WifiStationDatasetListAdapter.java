package id.ac.ukdw.ti.yuanlukito.wifiposition.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import id.ac.ukdw.ti.yuanlukito.wifiposition.R;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;

/**
 * Created by Yuan Lukito on 3/27/2015.
 */
public class WifiStationDatasetListAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<WifiStation> stations;

    public WifiStationDatasetListAdapter(Activity activity, List<WifiStation> stations){
        this.activity = activity;
        this.stations = stations;
    }

    @Override
    public int getCount() {
        return stations.size();
    }

    @Override
    public WifiStation getItem(int position) {
        return stations.get(position);
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
            convertView = inflater.inflate(R.layout.wifistationdataset_list_item, null);

        WifiStation ws = stations.get(position);
        TextView textViewBssid = (TextView) convertView.findViewById(R.id.textViewWifiStationBSSIDDataset);
        textViewBssid.setText(ws.getBssid());
        TextView textViewSsid = (TextView) convertView.findViewById(R.id.textViewWifiStationSSIDDataset);
        textViewSsid.setText(ws.getSsid());
        return convertView;
    }
}

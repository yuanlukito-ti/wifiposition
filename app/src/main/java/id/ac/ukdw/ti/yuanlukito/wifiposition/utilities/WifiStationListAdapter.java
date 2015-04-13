package id.ac.ukdw.ti.yuanlukito.wifiposition.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import id.ac.ukdw.ti.yuanlukito.wifiposition.R;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;

/**
 * Created by Yuan Lukito on 3/26/2015.
 */
public class WifiStationListAdapter extends BaseAdapter {
    private ArrayList<WifiStation> stations;
    private Activity activity;
    private LayoutInflater inflater;

    public WifiStationListAdapter(Activity activity, ArrayList<WifiStation> stations){
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
            convertView = inflater.inflate(R.layout.wifistation_list_item, null);
        WifiStation ws = stations.get(position);
        TextView textViewBssid = (TextView) convertView.findViewById(R.id.textViewWifiBssid);
        TextView textViewSsid = (TextView) convertView.findViewById(R.id.textViewWifiSsid);
        TextView textViewFrequency = (TextView) convertView.findViewById(R.id.textViewWifiFrequency);
        TextView textViewLevel = (TextView) convertView.findViewById(R.id.textViewWifiLevel);
        ProgressBar progressBarLevel = (ProgressBar) convertView.findViewById(R.id.progressBarWifiLevel);
        ImageView imageViewIndicator = (ImageView) convertView.findViewById(R.id.imageViewWifiStationIndicator);

        textViewBssid.setText(ws.getBssid());
        textViewSsid.setText(ws.getSsid());
        textViewFrequency.setText("Freq: " + ws.getFrequency());
        textViewLevel.setText(ws.getLevel() + " dBm");
        progressBarLevel.setProgress(WifiSignalStrength.levelToQuality(ws.getLevel()));

        if(ws.getIsOnDB())
            imageViewIndicator.setImageResource(R.drawable.wifi_48_ondb);
        else
            imageViewIndicator.setImageResource(R.drawable.wifi_48);

        return convertView;
    }
}

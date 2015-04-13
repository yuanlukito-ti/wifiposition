package id.ac.ukdw.ti.yuanlukito.wifiposition;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.ac.ukdw.ti.yuanlukito.wifiposition.model.DataSource;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.ApplicationContextProvider;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.WifiStationListAdapter;
import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.WifiStationSortByLevel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WifiStationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WifiStationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WifiStationsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    //UI Components
    private Spinner spinnerCurrentLocation;
    private Button buttonSave;
    private Button buttonRescan;
    private ListView listViewWifiStations;

    //Wifi scanning related
    private WifiManager wifiManager;

    //Wifi stations related
    private ArrayAdapter<String> locationsAdapter;
    private ArrayAdapter<String> wifiStationsAdapter;
    private ArrayList<String> wifiStations;
    private ArrayList<WifiStation> wifiStationsArrayList;

    private WifiStationListAdapter wifiListAdapter;

    //broadcast receiver
    BroadcastReceiver broadcastReceiver;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WifiStations.
     */
    // TODO: Rename and change types and number of parameters
    public static WifiStationsFragment newInstance(String param1, String param2) {
        WifiStationsFragment fragment = new WifiStationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WifiStationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_wifi_stations, container, false);

        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        //spinner current location
        spinnerCurrentLocation = (Spinner) rootView.findViewById(R.id.spinnerLocation);
        final DataSource dataSource = new DataSource(ApplicationContextProvider.getContext());
        try {
            dataSource.open();
            locationsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dataSource.getLocationArray());
            locationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCurrentLocation.setAdapter(locationsAdapter);
            dataSource.close();
        }
        catch (SQLException sqlEx){
            Log.e("WIPOS", sqlEx.getMessage());
        }

        wifiStations = new ArrayList<>();
        wifiStationsArrayList = new ArrayList<>();

        //list view wifi stations
        listViewWifiStations = (ListView) rootView.findViewById(R.id.listViewWifiStation);
        wifiStationsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, wifiStations);
        listViewWifiStations.setAdapter(wifiStationsAdapter);
        listViewWifiStations.setLongClickable(true);
        listViewWifiStations.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiStation ws = wifiStationsArrayList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to save this Wifi Station? \n" + ws.toString())
                       .setTitle("Save Wifi Station");
                builder.setPositiveButton("Yes, save it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //save to database
                        try{
                            dataSource.open();
                            String message = "";
                            if(dataSource.insertWifiStation(ws) != -1){
                                message = "Wifi station saved successfully";
                            }
                            else {
                                message = "This Wifi station is already on database";
                            }
                            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                        catch (SQLException sqlEx){
                            Log.e("WIPOS", sqlEx.getMessage());
                        }
                    }
                });
                builder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });


        //button rescan
        buttonRescan = (Button) rootView.findViewById(R.id.buttonRescan);
        buttonRescan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                wifiManager.startScan();
                Toast.makeText(getActivity().getApplicationContext(), "Start scanning Wifi Stations", Toast.LENGTH_SHORT).show();
            }
        });

        //button save
        buttonSave = (Button) rootView.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to database
                //0. check wifistations count
                if (wifiStationsArrayList.size() > 0) {
                    //1. get location id
                    try {
                        dataSource.open();
                        int locationId = dataSource.getLocationId((String) spinnerCurrentLocation.getSelectedItem());
                        if(locationId != -1){
                            if(dataSource.insertWifiData(locationId, wifiStationsArrayList)){
                                Toast.makeText(ApplicationContextProvider.getContext(), "Wifi data successfully saved", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ApplicationContextProvider.getContext(), "Failed to save wiFi data", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            //location unknown!
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Location is unknown or database error.  Please check your input!")
                                   .setTitle("Cannot save Wifi Data");
                            builder.setPositiveButton("OK", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                    catch (SQLException sqlEx){
                        Log.e("WIPOS", sqlEx.getMessage());
                    }
                }
            }
        });

        return rootView;
    }

    public void receiveBroadcastWifiScanningResult(List<ScanResult> wifiScanResults){
        Toast.makeText(getActivity().getApplicationContext(), "Received Wifi scan results", Toast.LENGTH_SHORT).show();
        wifiStations.clear();
        wifiStationsArrayList.clear();
        DataSource dataSource = new DataSource(getActivity());
        try {
            dataSource.open();
            for(int i=0; i<wifiScanResults.size(); i++){
                ScanResult result = wifiScanResults.get(i);
                WifiStation ws = new WifiStation(result.BSSID, result.SSID, result.frequency, result.level);
                if(dataSource.isWifiStationExist(ws))
                    ws.setIsOnDB(true);
                wifiStationsArrayList.add(ws);
            }
            dataSource.close();
            Collections.sort(wifiStationsArrayList, new WifiStationSortByLevel());
            wifiListAdapter = new WifiStationListAdapter(getActivity(), wifiStationsArrayList);
            listViewWifiStations.setAdapter(wifiListAdapter);
        } catch (SQLException e) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}

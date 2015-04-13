package id.ac.ukdw.ti.yuanlukito.wifiposition;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm.Algorithm;
import id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm.KNNAlgorithm;
import id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm.NaiveBayesAlgorithm;
import id.ac.ukdw.ti.yuanlukito.wifiposition.algorithm.RandomAlgorithm;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.Location;
import id.ac.ukdw.ti.yuanlukito.wifiposition.model.WifiStation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyPositionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyPositionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyPositionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RANDOM_ALGO = 0;
    private static final int KNN_ALGO = 1;
    private static final int NAIVEBAYES_ALGO = 2;

    private int selectedAlgorithm = 0;
    private Algorithm algorithm;

    private TextView textViewMyPositionResult;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //broadcast receiver wifi scanning
    BroadcastReceiver broadcastReceiver;
    WifiManager wifiManager;
    List<ScanResult> wifiScanResults;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPositionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPositionFragment newInstance(String param1, String param2) {
        MyPositionFragment fragment = new MyPositionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyPositionFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_my_position, container, false);

        //Wifi manager
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        //UI Components
        textViewMyPositionResult = (TextView) view.findViewById(R.id.textViewMyPositionResult);
        final RadioButton rbKNNAlgo = (RadioButton) view.findViewById(R.id.radioButtonKnnAlgo);
        final RadioButton rbNaiveBayesAlgo = (RadioButton) view.findViewById(R.id.radioButtonNaiveBayesAlgo);

        Button buttonGetMyPosition = (Button) view.findViewById(R.id.buttonGetMyPosition);
        buttonGetMyPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(rbKNNAlgo.isChecked())
                    selectedAlgorithm = KNN_ALGO;
                else if(rbNaiveBayesAlgo.isChecked())
                    selectedAlgorithm = NAIVEBAYES_ALGO;
                else
                    selectedAlgorithm = RANDOM_ALGO;
                Log.d("WIPOS", "Selected algorithm: " + selectedAlgorithm);
                switch(selectedAlgorithm){
                    case NAIVEBAYES_ALGO: algorithm = new NaiveBayesAlgorithm();
                        break;
                    case KNN_ALGO: algorithm = new KNNAlgorithm();
                        break;
                    default: algorithm = new RandomAlgorithm();
                }
                Log.d("WIPOS", "Algorithm: " + algorithm.getAlgorithmName());
                Log.d("WIPOS", "Start scanning wifi stations......");
                wifiManager.startScan();
            }
        });
        return view;
    }

    public void receiveBroadcastWifiScanningResult(List<ScanResult> wifiScanResults){
        if(algorithm != null) {
            Log.d("WIPOS", "Receiving wifi scan results");

            List<WifiStation> results = new ArrayList<>(wifiScanResults.size());
            for (ScanResult sr : wifiScanResults) {
                results.add(new WifiStation(sr.BSSID, sr.SSID, sr.frequency, sr.level));
            }

            Log.d("WIPOS", "Calculating position.....");

            //send result to algorithm
            algorithm.setUp();
            Location locationResult = algorithm.getCurrentLocation(results);
            textViewMyPositionResult.setText("Your Location is:\n\n" + locationResult.getName() + "\n");

            Log.d("WIPOS", "Your position is: " + locationResult.getName());
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

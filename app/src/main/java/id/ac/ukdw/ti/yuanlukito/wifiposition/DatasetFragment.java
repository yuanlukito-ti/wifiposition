package id.ac.ukdw.ti.yuanlukito.wifiposition;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import id.ac.ukdw.ti.yuanlukito.wifiposition.utilities.CsvWriter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatasetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DatasetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DatasetFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //UI Components
    private CheckBox cbLocation;
    private CheckBox cbWifiStation;
    private CheckBox cbWifiData;
    private CheckBox cbWifiDataDetail;
    private Button buttonExport;
    private Button buttonLocation;
    private Button buttonWifiStation;
    private Button buttonWifiData;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DatasetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DatasetFragment newInstance(String param1, String param2) {
        DatasetFragment fragment = new DatasetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DatasetFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_dataset, container, false);

        //ui components
        cbLocation = (CheckBox) rootView.findViewById(R.id.cbLocation);
        cbWifiStation = (CheckBox) rootView.findViewById(R.id.cbWifiStation);
        cbWifiData = (CheckBox) rootView.findViewById(R.id.cbWifiData);
        cbWifiDataDetail = (CheckBox) rootView.findViewById(R.id.cbWifiDataDetail);
        buttonExport = (Button) rootView.findViewById(R.id.buttonExport);
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CsvWriter writer = CsvWriter.getInstance();
                if(cbLocation.isChecked()){
                    if(writer.writeLocation()){
                        Toast.makeText(getActivity(), "Location data successfully exported", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Failed to export Location data", Toast.LENGTH_SHORT).show();
                    }
                }

                if(cbWifiStation.isChecked()) {
                    if(writer.writeWifiStation()){
                        Toast.makeText(getActivity(), "Wifi Station data successfully exported", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Failed to export Wifi Station data", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonLocation = (Button) rootView.findViewById(R.id.buttonLocation);
        buttonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call LocationDataSetActivity
                Intent intent = new Intent(getActivity(), LocationDataSetActivity.class);
                startActivity(intent);
            }
        });

        buttonWifiStation = (Button) rootView.findViewById(R.id.buttonWifiStation);
        buttonWifiStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call wifistationdataset activity
                Intent intent = new Intent(getActivity(), WifiStationDataSetActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
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

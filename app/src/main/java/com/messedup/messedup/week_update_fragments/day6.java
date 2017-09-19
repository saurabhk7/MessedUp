package com.messedup.messedup.week_update_fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.messedup.messedup.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link day6.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link day6#newInstance} factory method to
 * create an instance of this fragment.
 */
public class day6 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<HashMap> menu;


    private OnFragmentInteractionListener mListener;

    public day6() {
        // Required empty public constructor
    }

    public day6(ArrayList m) {
        // Required empty public constructor
        menu = m;
        Log.e("sf", menu.toString());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment day6.
     */
    // TODO: Rename and change types and number of parameters
    public static day6 newInstance(String param1, String param2) {
        day6 fragment = new day6();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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

        View rootView = inflater.inflate(R.layout.fragment_day6, container, false);
        if(menu.size() > 0){
            if(menu.get(0).get("Meal").equals("Lunch")){
                ListView listView6lunch = (ListView)rootView.findViewById(R.id.listview6lunch);
                HashMap m = menu.get(0);
                String menulunch[] = (String[]) m.values().toArray(new String[0]);
                ArrayList<String>  list = new ArrayList<String>();
                list.addAll(Arrays.asList(menulunch));
                list.remove("Lunch");
                list.removeAll(Collections.singleton("null"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.menu_list_view, list);
                listView6lunch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }if(menu.get(0).get("Meal").equals("Dinner")){
                ListView listView6dinner = (ListView)rootView.findViewById(R.id.listview6dinner);
                HashMap m = menu.get(0);
                String menudinner[] = (String[]) m.values().toArray(new String[0]);
                ArrayList<String>  list = new ArrayList<String>();
                list.addAll(Arrays.asList(menudinner));
                list.remove("Dinner");
                list.removeAll(Collections.singleton("null"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.menu_list_view, list);
                listView6dinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        if(menu.size() > 1){
            if(menu.get(1).get("Meal").equals("Lunch")){
                ListView listView6lunch = (ListView)rootView.findViewById(R.id.listview6lunch);
                HashMap m = menu.get(1);
                String menulunch[] = (String[]) m.values().toArray(new String[0]);
                ArrayList<String>  list = new ArrayList<String>();
                list.addAll(Arrays.asList(menulunch));
                list.remove("Lunch");
                list.removeAll(Collections.singleton("null"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.menu_list_view, list);
                listView6lunch.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }if(menu.get(1).get("Meal").equals("Dinner")){
                ListView listView6dinner = (ListView)rootView.findViewById(R.id.listview6dinner);
                HashMap m = menu.get(1);
                String menudinner[] = (String[]) m.values().toArray(new String[0]);
                ArrayList<String>  list = new ArrayList<String>();
                list.addAll(Arrays.asList(menudinner));
                list.remove("Dinner");
                list.removeAll(Collections.singleton("null"));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.menu_list_view, list);
                listView6dinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

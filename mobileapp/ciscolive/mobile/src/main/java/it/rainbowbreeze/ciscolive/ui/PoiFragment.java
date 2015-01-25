package it.rainbowbreeze.ciscolive.ui;

/**
 * Created by alfredomorresi on 24/01/15.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import it.rainbowbreeze.ciscolive.R;
import it.rainbowbreeze.ciscolive.domain.Poi;

/**
 * A placeholder fragment containing a simple view.
 */
public class PoiFragment extends Fragment {
    private PoiAdapter mPoiAdapter;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PoiFragment newInstance(int sectionNumber) {
        PoiFragment fragment = new PoiFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PoiFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_poi, container, false);

        mPoiAdapter = new PoiAdapter(getActivity());
        ListView list = (ListView) rootView.findViewById(R.id.poi_lstPoi);
        list.setAdapter(mPoiAdapter);

        Poi poi;
        poi = new Poi();
        poi.name = "Poi test 1";
        poi.url = "http://lorempixel.com/300/300/";
        mPoiAdapter.add(poi);
        poi = new Poi();
        poi.name = "Poi test 2";
        poi.url = "http://lorempixel.com/300/300/";
        mPoiAdapter.add(poi);
        mPoiAdapter.notifyDataSetChanged();

        return rootView;
    }
}
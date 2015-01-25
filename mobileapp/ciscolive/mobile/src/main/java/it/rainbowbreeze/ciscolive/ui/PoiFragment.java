package it.rainbowbreeze.ciscolive.ui;

/**
 * Created by alfredomorresi on 24/01/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

        return rootView;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Poi poi;

                final List<Poi> pois = new ArrayList<Poi>();


                poi = new Poi();
                poi.name = "Biological food";
                //poi.url = "http://lorempixel.com/300/300/";
                poi.url = "http://www.cbc.arizona.edu/sites/default/files/undergraduate/2013_poster_fair.jpg";
                poi.users = 45;
                poi.visited = false;
                poi.x = 35.55;
                poi.y = 0.58;
                poi.visited = true;
                pois.add(poi);

                poi = new Poi();
                poi.name = "Sustainable agriculture";
                poi.url = "https://www.colourbox.com/preview/8197206-milan-italy-september-28-vegan-fest-on-september-28-2013-thousands-of-people-visited-the-fair-miveg-where-were-presented-vegan-biological-products-vegan-cook-and-animal-rights-convention.jpg";
                poi.users = 76;
                poi.visited = false;
                poi.x = 74.55;
                poi.y = 0.12;
                poi.visited = false;
                pois.add(poi);

                poi = new Poi();
                poi.name = "Gluten free food";
                poi.url = "http://st.depositphotos.com/1034986/3436/i/950/depositphotos_34362801-bologna-italy---september-8-vegan-fest-on-september-8-2013.-thousands-of-people-visited-the-fair-vegan-fest-where-were-presented-vegan-biological-and-natural-products-vegan-cook-and-animal-rights-conventions..jpg";
                poi.users = 32;
                poi.visited = false;
                poi.x = 0.14;
                poi.y = 0.25;
                poi.visited = true;
                pois.add(poi);

                poi = new Poi();
                poi.name = "Vegan food";
                poi.url = "https://www.colourbox.com/preview/8197180-milan-italy-september-28-vegan-fest-on-september-28-2013-thousands-of-people-visited-the-fair-miveg-where-were-presented-vegan-biological-products-vegan-cook-and-animal-rights-convention.jpg";
                poi.users = 90;
                poi.visited = false;
                poi.x = 28.72;
                poi.y = 0.3;
                poi.visited = false;
                pois.add(poi);



                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPoiAdapter.clear();
                        mPoiAdapter.addAll(pois);
                        mPoiAdapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }
}
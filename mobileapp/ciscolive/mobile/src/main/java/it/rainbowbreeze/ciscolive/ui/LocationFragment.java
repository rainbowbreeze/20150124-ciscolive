package it.rainbowbreeze.ciscolive.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import it.rainbowbreeze.ciscolive.R;
import it.rainbowbreeze.ciscolive.common.Bag;
import it.rainbowbreeze.ciscolive.common.ILogFacility;
import it.rainbowbreeze.ciscolive.common.MyApp;
import it.rainbowbreeze.ciscolive.logic.action.ActionsManager;
import it.rainbowbreeze.ciscolive.logic.bus.CmxLocationUpdatedEvent;
import it.rainbowbreeze.ciscolive.logic.bus.FloorImageEvent;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {
    private static final String LOG_TAG = LocationFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject ActionsManager mActionsManager;
    @Inject Bus mBus;

    private ImageView mImgFloor;
    private CircleImageView mImgProfile;
    private View mLayMapContainer;
    private double mMapWidth;
    private double mMapLength;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    public LocationFragment() {
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
        View rootView = inflater.inflate(R.layout.fra_location, container, false);

        mImgFloor = (ImageView) rootView.findViewById(R.id.location_imgMap);
        mImgProfile = (CircleImageView) rootView.findViewById(R.id.location_imgProfile);
        mLayMapContainer = rootView.findViewById(R.id.location_layMapContainer);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MyApp) activity.getApplicationContext()).inject(this);

        mActionsManager.getFloorImageAction()
                .setVenueId(Bag.VENUE_ID)
                .setFloorId(Bag.FLOOR_ID)
                .executeAsync();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
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

    @Subscribe
    public void onLocationUpdatedEvent(CmxLocationUpdatedEvent event) {
        mLogFacility.v(LOG_TAG, "CMX position update: " + event.toString());
        drawPositionOnMap(event.getX(), event.getY());
    }

    @Subscribe
    public void onNewFloorImageEvent(FloorImageEvent event) {
        mLogFacility.v(LOG_TAG, "Received a new floor image");

        Picasso.with(getActivity().getApplicationContext())
                .load("https://lh5.googleusercontent.com/-hh4lXeK_WVk/AAAAAAAAAAI/AAAAAAAADPM/Ii9j3uzORfI/s120-c/photo.jpg")
                .noFade()  // Required by CircleImageView
                .into(mImgProfile);

        mImgFloor.setImageBitmap(event.bitmap);
        mMapWidth = event.width;
        mMapLength = event.length;

        //resize the imageView and the overlay
        int mapHeight = (int) (mLayMapContainer.getWidth() / mMapLength * mMapWidth);
        mImgFloor.setLayoutParams(new FrameLayout.LayoutParams(mLayMapContainer.getWidth(), mapHeight));
    }


    private void drawPositionOnMap(double x, double y) {
        if (mMapLength == 0 || mMapWidth == 0) {
            mLogFacility.v(LOG_TAG, "Cannot update user position, map is empty");
            return;
        }

        if (mImgProfile.getVisibility() != View.VISIBLE) {
            mLogFacility.v(LOG_TAG, "Showing the user picture");
            mImgProfile.setVisibility(View.VISIBLE);
            mLogFacility.v(LOG_TAG, "Profile position Top: " + mImgProfile.getTop() + " Left: " + mImgProfile.getLeft());
        }

        mLogFacility.v(LOG_TAG, "Profile position Top: " + mImgProfile.getTop() + " Left: " + mImgProfile.getLeft());
        int imgX = (int) (mImgFloor.getLeft() + mImgFloor.getWidth() / mMapLength * x);
        int imgY = (int) (mImgFloor.getTop() + mImgFloor.getHeight() / mMapWidth * y);

        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) mImgProfile.getLayoutParams();
        p.leftMargin = imgX;
        p.topMargin = imgY;
        mImgProfile.setLayoutParams(p);
        mLogFacility.v(LOG_TAG, "Moving user picture to Top: " + y + " Left: " + x);
    }

}

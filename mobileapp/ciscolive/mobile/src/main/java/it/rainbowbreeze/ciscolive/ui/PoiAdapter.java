package it.rainbowbreeze.ciscolive.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.rainbowbreeze.ciscolive.R;
import it.rainbowbreeze.ciscolive.domain.Poi;

/**
 * Created by alfredomorresi on 25/01/15.
 */
public class PoiAdapter extends ArrayAdapter<Poi> {
    LayoutInflater mLayoutInflater;
    public PoiAdapter(Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;

        if (rootView == null) {
            rootView = mLayoutInflater.inflate(R.layout.vw_poi, null);
            ViewHolder holder = new ViewHolder();
            rootView.setTag(holder);
            holder.lblName = (TextView) rootView.findViewById(R.id.poiitem_lblName);
        }
        ViewHolder holder = (ViewHolder) rootView.getTag();
        Poi poi = getItem(position);
        holder.lblName.setText(poi.name);

        return rootView;
    }

    private static class ViewHolder {
        ImageView image;
        TextView lblName;
    }
}

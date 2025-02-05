package com.project.android.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.project.android.R;
import com.project.android.model.Grievance;
import com.project.android.utility.Constants;

import java.util.List;


public class

OtherGrievancesListItemAdapter extends ArrayAdapter<Grievance>
{
    private Activity context;
    List<Grievance> grievances;
    LayoutInflater inflater;

    public OtherGrievancesListItemAdapter(Activity context, int resourceId)
    {
        super(context, resourceId);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView descriptionTV;
        TextView categoryTV;
        ImageView imageIV;
        RatingBar ratingBar;
    }

    public View getView(int position, View view, ViewGroup parent) {

        final OtherGrievancesListItemAdapter.ViewHolder holder;
        if (view == null) {
            holder = new OtherGrievancesListItemAdapter.ViewHolder();
            view = inflater.inflate(R.layout.othergrievanceslist_item, null);

            holder.descriptionTV = (TextView) view.findViewById(R.id.description);
            holder.categoryTV = (TextView) view.findViewById(R.id.category);
            holder.imageIV = (ImageView) view.findViewById(R.id.image);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.rating);
            view.setTag(holder);
        } else
        {
            holder = (OtherGrievancesListItemAdapter.ViewHolder) view.getTag();
        }

        holder.descriptionTV.setText(grievances.get(position).getDescription());
        holder.categoryTV.setText("CATEGORY: " + grievances.get(position).getCategory());
        holder.ratingBar.setProgress(grievances.get(position).getRating());
        String imagePath = grievances.get(position).getImagePath();
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        if (null != bitmap) {
            holder.imageIV.setImageBitmap(bitmap);
        }
        else
        {
            int resID = context.getResources().getIdentifier("noimage", Constants.DRAWABLE_RESOURCE, context.getPackageName());
            holder.imageIV.setImageResource(resID);
        }

        return view;
    }

    public List<Grievance> getGrievances() {
        return grievances;
    }

    public void setGrievances(List<Grievance> grievances)
    {
        this.grievances = grievances;
    }

    @Override
    public int getCount() {
        return grievances.size();
    }

    @Override
    public Grievance getItem(int position) {
        return grievances.get(position);
    }


}

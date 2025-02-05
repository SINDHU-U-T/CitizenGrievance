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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class MyGrievancesListItemAdapter extends ArrayAdapter<Grievance>
{
    private Activity context;
    List<Grievance> grievances;
    LayoutInflater inflater;

    public MyGrievancesListItemAdapter(Activity context, int resourceId)
    {
        super(context, resourceId);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView descriptionTV;
        TextView categoryTV;
        TextView statusTV;
        TextView wardTV;
        TextView dateTV;
        ImageView imageIV;
        RatingBar ratingBar;
    }

    public View getView(int position, View view, ViewGroup parent) {

        final MyGrievancesListItemAdapter.ViewHolder holder;
        if (view == null) {
            holder = new MyGrievancesListItemAdapter.ViewHolder();
            view = inflater.inflate(R.layout.mygrievanceslist_item, null);

            holder.descriptionTV = (TextView) view.findViewById(R.id.description);
            holder.categoryTV = (TextView) view.findViewById(R.id.category);
            holder.statusTV = (TextView) view.findViewById(R.id.status);
            holder.wardTV = (TextView) view.findViewById(R.id.ward);
            holder.dateTV = (TextView) view.findViewById(R.id.date);
            holder.imageIV = (ImageView) view.findViewById(R.id.image);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.rating);
            view.setTag(holder);
        } else
        {
            holder = (MyGrievancesListItemAdapter.ViewHolder) view.getTag();
        }

        holder.descriptionTV.setText(grievances.get(position).getDescription());
        holder.categoryTV.setText("CATEGORY: " + grievances.get(position).getCategory());
        holder.statusTV.setText("STATUS: " + Constants.status[grievances.get(position).getStatus()]);
        holder.wardTV.setText("WARD: " + grievances.get(position).getWard());

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String grievanceDateString = dateFormatter.format(grievances.get(position).getGrievanceDate().getTime());
        holder.dateTV.setText("Posted On: " + grievanceDateString);
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

package com.project.android.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.android.R;
import com.project.android.model.Grievance;
import com.project.android.utility.Constants;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class AllGrievancesListItemAdapter extends ArrayAdapter<Grievance>
{
    private Activity context;
    List<Grievance> grievances;
    LayoutInflater inflater;

    public AllGrievancesListItemAdapter(Activity context, int resourceId)
    {
        super(context, resourceId);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView descriptionTV;
        TextView dateTV;
        ImageView imageIV;
    }

    public View getView(int position, View view, ViewGroup parent) {

        final AllGrievancesListItemAdapter.ViewHolder holder;
        if (view == null) {
            holder = new AllGrievancesListItemAdapter.ViewHolder();
            view = inflater.inflate(R.layout.allgrievanceslist_item, null);

            holder.descriptionTV = (TextView) view.findViewById(R.id.description);
            holder.dateTV = (TextView) view.findViewById(R.id.date);
            holder.imageIV = (ImageView) view.findViewById(R.id.image);

            view.setTag(holder);
        } else
        {
            holder = (AllGrievancesListItemAdapter.ViewHolder) view.getTag();
        }

        holder.descriptionTV.setText(grievances.get(position).getDescription());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String grievanceDateString = dateFormatter.format(grievances.get(position).getGrievanceDate().getTime());
        holder.dateTV.setText("Posted On: " + grievanceDateString);

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

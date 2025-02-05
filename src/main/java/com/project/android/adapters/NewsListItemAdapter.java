package com.project.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.android.R;
import com.project.android.model.News;

import java.util.List;


public class NewsListItemAdapter extends ArrayAdapter<News>
{
    private Activity context;
    List<News> newsList;
    LayoutInflater inflater;

    public NewsListItemAdapter(Activity context, int resourceId)
    {
        super(context, resourceId);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder
    {
        TextView titleTV;
        TextView descriptionTV;
        TextView wardTV;
    }

    public View getView(int position, View view, ViewGroup parent) {

        final NewsListItemAdapter.ViewHolder holder;
        if (view == null) {
            holder = new NewsListItemAdapter.ViewHolder();
            view = inflater.inflate(R.layout.news_item, null);
            holder.titleTV = (TextView) view.findViewById(R.id.title);
            holder.descriptionTV = (TextView) view.findViewById(R.id.description);
            holder.wardTV = (TextView) view.findViewById(R.id.ward);
            view.setTag(holder);
        } else
        {
            holder = (NewsListItemAdapter.ViewHolder) view.getTag();
        }
        holder.titleTV.setText(newsList.get(position).getTitle());
        holder.descriptionTV.setText(newsList.get(position).getDescription());
        holder.wardTV.setText(newsList.get(position).getWard());

        return view;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList)
    {
        this.newsList = newsList;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public News getItem(int position) {
        return newsList.get(position);
    }


}

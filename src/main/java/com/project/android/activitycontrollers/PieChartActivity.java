package com.project.android.activitycontrollers;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.project.android.R;
import com.project.android.database.AppDatabaseHelper;
import com.project.android.utility.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;


public class PieChartActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        String ward = this.getIntent().getStringExtra(Constants.GRIEVANCE_WARD_KEY);
        String report_Type = this.getIntent().getStringExtra("Report_Type");
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(getApplicationContext());
        HashMap<String, String> results = null;
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList();
        int j = 0;

        switch (report_Type)
        {
            case "Category":
                results = databaseHelper.getCountOfGrievancesForWardCategoryWise(ward);
                for(Map.Entry<String, String> e : results.entrySet())
                {
                    System.out.println(e.getKey() + "," + e.getValue());

                    entries.add(new Entry(Float.parseFloat(e.getValue()), j++));
                    labels.add(e.getKey());
                }

                break;
            case "Status":
                results = databaseHelper.getCountOfGrievancesForWardStatusWise(ward);
                for(Map.Entry<String, String> e : results.entrySet())
                {
                    System.out.println(e.getKey() + "," + e.getValue());

                    entries.add(new Entry(Float.parseFloat(e.getValue()), j++));
                    labels.add(Constants.status[Integer.parseInt(e.getKey())]);
                }

                break;
            case "Rating":
                results = databaseHelper.getCountOfGrievancesForWardRatingWise(ward);
                for(Map.Entry<String, String> e : results.entrySet())
                {
                    System.out.println(e.getKey() + "," + e.getValue());

                    entries.add(new Entry(Float.parseFloat(e.getValue()), j++));
                    labels.add(Constants.rating[Integer.parseInt(e.getKey())-1]);
                }

                break;

        }



        PieDataSet dataset = new PieDataSet(entries, "");

        String[] colorsTxt = getApplicationContext().getResources().getStringArray(R.array.colors);
        List<Integer> colors = new ArrayList();
        for (int i = 0; i < colorsTxt.length; i++) {
            int newColor = Color.parseColor(colorsTxt[i]);
            colors.add(newColor);
        }
        dataset.setColors(colors);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(10f);
        dataset.setSliceSpace(3f);

        PieChart chart = new PieChart(getApplicationContext());

        PieData data = new PieData(labels, dataset);
        data.setValueFormatter(new PercentFormatter());

        chart.setData(data);
        chart.setDescription("");
        chart.animateY(3000);
        chart.setDrawHoleEnabled(true);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        //chart.setUsePercentValues(true);

        chart.setCenterText(ward);
        chart.setCenterTextColor(Color.BLUE);
        chart.setCenterTextSize(14);

        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(40f);

        chart.setTransparentCircleRadius(50f);

        Legend l = chart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(5);
        l.setYEntrySpace(8);
        l.setTextColor(Color.BLUE);
        l.setTextSize(16f);
        setContentView(chart);
    }
}

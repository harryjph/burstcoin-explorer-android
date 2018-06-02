package com.harrysoft.burstcoinexplorer.observe.util;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.observe.ui.ObserveNodeValueFormatter;

import java.util.ArrayList;

public class PieUtils {

    public static ArrayList<Integer> getColours() {
        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        return colors;
    }
    
    public static void setupPieChart(Context context, PieChart pieChart, ArrayList<PieEntry> pieEntryList, String dataSetLabel, float totalPeers, ArrayList<Integer> colors) {
        PieDataSet pieDataSet = new PieDataSet(pieEntryList, dataSetLabel);

        pieDataSet.setValueFormatter(new ObserveNodeValueFormatter(context, totalPeers));

        pieDataSet.setColors(colors);

        pieDataSet.setValueTextColor(Color.BLACK);
        pieChart.setEntryLabelColor(Color.BLACK);

        PieData data = new PieData(pieDataSet);

        pieChart.setData(data);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                RemovableLabelPieEntry p = (RemovableLabelPieEntry) e;
                pieChart.setCenterText(context.getString(R.string.observe_center_text, p.getActualLabel(), p.getActualValue()));
            }

            @Override
            public void onNothingSelected() {
                pieChart.setCenterText(context.getString(R.string.observe_center_text_empty));
                pieChart.invalidate();
            }
        });

        pieChart.setCenterText(context.getString(R.string.observe_center_text_empty));

        Description description = new Description();
        description.setText("");

        pieChart.setDescription(description);

        pieChart.setRotationEnabled(false);

        pieChart.getOnTouchListener().setLastHighlighted(null);
        pieChart.highlightValues(null);

        pieChart.getLegend().setEnabled(false);

        pieChart.invalidate();
    }

    public static void setupPieError(Context context, PieChart pieChart, Throwable throwable) {
        pieChart.setCenterText(context.getString(R.string.observe_load_error, throwable.getMessage()));
        pieChart.invalidate();
    }
}

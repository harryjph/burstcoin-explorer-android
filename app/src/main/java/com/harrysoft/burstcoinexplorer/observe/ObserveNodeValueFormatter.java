package com.harrysoft.burstcoinexplorer.observe;

import android.content.Context;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.harrysoft.burstcoinexplorer.R;
import com.harrysoft.burstcoinexplorer.observe.pieutils.RemovableLabelPieEntry;

import java.text.DecimalFormat;

public class ObserveNodeValueFormatter implements IValueFormatter {

    private final Context context;

    private final DecimalFormat decimalFormat;

    private final float total;

    public ObserveNodeValueFormatter(Context context, float total) {
        this.context = context;
        this.total = total;
        decimalFormat = new DecimalFormat("#");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

        RemovableLabelPieEntry p = (RemovableLabelPieEntry) entry;

        String formattedValue = context.getString(R.string.observe_node_count, decimalFormat.format(value));

        p.setActualValue(formattedValue);

        if (p.getValue() < total * 0.05f) { // if it is less than 5%
            p.setLabel("");
            return "";
        } else {
            return formattedValue;
        }
    }
}
package com.harrysoft.burstcoinexplorer.observe.util;

import android.text.TextUtils;

import com.github.mikephil.charting.data.PieEntry;

public class RemovableLabelPieEntry extends PieEntry {

    private final String actualLabel;
    private String actualValue;

    public RemovableLabelPieEntry(float value, String label) {
        super(value, label);
        this.actualLabel = label;
        this.actualValue = String.valueOf(value);
    }

    public String getActualLabel() {
        return actualLabel;
    }

    public String getActualValue() {
        return TextUtils.isEmpty(actualLabel) ? String.valueOf(getValue()) : actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }
}
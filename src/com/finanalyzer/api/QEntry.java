package com.finanalyzer.api;

import java.util.List;

import com.google.gson.JsonArray;
import com.gs.collections.impl.list.mutable.FastList;

public class QEntry
{
    private String date;
    private List<String> rows = FastList.newList();

    public QEntry(JsonArray entry) {
        this.date = entry.get(0).toString();

        for(Object eachValue : entry) {
            rows.add(eachValue == null ? "null" : eachValue.toString());
        }
    }

    public String getDate() {
        return date;
    }

    public String getFormattedDate() {
        return date.replace("\"", "");
    }
    
    public List<String> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return rows.toString();
    }
    
    public String valueAtColumn(int i)
    {
    	return this.getRows().get(i);
    }
}

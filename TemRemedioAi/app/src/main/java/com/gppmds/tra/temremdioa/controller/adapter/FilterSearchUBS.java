package com.gppmds.tra.temremdioa.controller.adapter;

import android.widget.Filter;
import android.util.Log;
import com.gppmds.tra.temremdioa.model.UBS;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchUBS extends Filter {
    CardListAdapterUBS adapter;
    List<UBS> filterList;

    public FilterSearchUBS(List<UBS> filterList, CardListAdapterUBS adapter) {

        for(int i = 0; i < filterList.length; i++) {
            Log.d("FilterSearchUBS -> constructor",
                  "FilterList["+ i +"] = " + filterList[i]);
        }

        this.adapter = adapter;
        this.filterList = filterList;
    }

    @Override
    public FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            List<UBS> filteredUBSs = new ArrayList<>();
            final int listSize = filteredUBSs.size();

            if (listSize > -1 && listSize < filterList.size()+1 ) {
                for (int i = 0; i < filterList.size(); i++) {
                    if(filterList.get(i).getUbsName().toUpperCase().contains(constraint)) {
                        filteredUBSs.add(filterList.get(i));
                    } else {
                        /* Nothing to do */
                    }
                }
                results.count = filteredUBSs.size();
                results.values = filteredUBSs;
            } else {

                Log.e("FilterSearchUBS -> filterResults",
                      "invalid size");

            }

        } else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    public void publishResults(CharSequence constraint, FilterResults results) {
        adapter.dataUBS = (List<UBS>) results.values;
         adapter.notifyDataSetChanged();
    }
}

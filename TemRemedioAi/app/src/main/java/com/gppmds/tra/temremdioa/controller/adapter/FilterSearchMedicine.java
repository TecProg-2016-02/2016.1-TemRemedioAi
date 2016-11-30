/*
 * File: CardListAdapterUBS.java
 * Purpose: Filters teh results of the search for the available Medicines.
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.widget.Filter;
import android.util.Log;

import com.gppmds.tra.temremdioa.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchMedicine extends Filter{

    CardListAdapterMedicine adapter;
    List<Medicine> filterList;

    public FilterSearchMedicine(List<Medicine> filterList, CardListAdapterMedicine adapter) {

        for(int i = 0; i < filterList.length; i++) {
            Log.d("FilterSearchMedicine -> constructor",
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
            List<Medicine> filteredMedicines = new ArrayList<>();
            final int listSize = filteredMedicines.size();

            if (listSize > -1 && listSize < filterList.size()+1 ) {
                for (int i = 0; i < filterList.size(); i++) {
                    if(filterList.get(i).getMedicineDescription().toUpperCase().contains(constraint)) {
                        filteredMedicines.add(filterList.get(i));
                    } else {
                        /* Nothing to do */
                    }
                }
                results.count = filteredMedicines.size();
                results.values = filteredMedicines;
            } else {

                Log.e("FilterSearchMedicine -> filterResults",
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
        adapter.dataMedicine = (List<Medicine>) results.values;
        adapter.notifyDataSetChanged();
    }
}

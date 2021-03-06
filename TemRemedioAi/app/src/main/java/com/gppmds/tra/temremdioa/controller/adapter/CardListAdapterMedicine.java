/*
 * File: CardListAdapterMedicine.java
 * Purpose: Shows the list of Medicines available.
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import static junit.framework.Assert.*;
import android.util.Log;

import com.gppmds.tra.temremdioa.controller.adapter.holder.ViewHolderMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.tra.gppmds.temremdioa.R;

import java.util.List;

public class CardListAdapterMedicine extends RecyclerView.Adapter<ViewHolderMedicine> implements Filterable {

    public static List<Medicine> dataMedicine  = new ArrayList<Medicine>();
    List<Medicine> filterDataMedicine  = new ArrayList<Medicine>();
    Context contextOpen;
    FilterSearchMedicine filterForMedicine;
    private Boolean showButtonUBSs;
    private Boolean showButtonInform;
    private String ubsName;

     @Override
    public FilterSearchMedicine getFilter() {

        // Check if exist a filter for medicines created
        if(filterForMedicine == null) {

            filterForMedicine = new FilterSearchMedicine(filterDataMedicine, this );

        } else {

            // Nothing to do

        }

        return filterForMedicine;

    }

    public void setShowButtonUBSs(Boolean showButtonUBSs) {

        assertFalse(showButtonUBSs == null);
        this.showButtonUBSs = (Boolean) showButtonUBSs;

    }


    public void setShowButtonInform(Boolean showButtonInform) {

        assertFalse(showButtonInform == null);
        this.showButtonInform = (Boolean) showButtonInform;

    }

    public void setUbsName(String ubsName) {

        assertEquals("", ubsName);
        assert ubsName != null;
        this.ubsName = (String) ubsName;

    }

    public void createFilter() {

        filterForMedicine = new FilterSearchMedicine(filterDataMedicine, this);
         Boolean test = getShowButtonUBSs();

    }

    private CardListAdapterMedicine(Context context, List<Medicine> dataMedicine) {

        for(int i = 0; i < dataMedicine.length; i++) {
            Log.d("CardListAdapterMedicine -> constructor",
                  "DataMedicine["+ i +"] = " + dataMedicine[i]);
        }

        this.contextOpen = context;
        this.dataMedicine = dataMedicine;
        this.filterDataMedicine = dataMedicine;

        setShowButtonUBSs(true);
        setShowButtonInform(false);
        setUbsName("");

    }

    @Override
    private ViewHolderMedicine onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.card_list_medicine,
                                                                        parent, false);

        return new ViewHolderMedicine(view);

    }

    @Override
    private void onBindViewHolder(ViewHolderMedicine holder, int position) {

        Medicine rowData = this.dataMedicine.get(position);
        holder.getTextViewMedicineName().setText(rowData.getMedicineDescription());
        holder.getTextViewMedicineUnit().setText(rowData.getUnityMedicineFormatted());
        holder.getTextViewMedicineDosage().setText(rowData.getMedicineDosage());

        // Check if the button UBS is visible
        if (!getShowButtonUBSs()) {

            holder.getButtonSelectUbs().setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                                            holder.getButtonMedicineInform().getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.getButtonMedicineInform().setLayoutParams(params);

        } else {
            // Nothing to do
        }

        // Check if the ubs name is empty
        if (!getUbsName().equalsIgnoreCase("")) {

            holder.ubsSelectedName = getUbsName();

        } else {

            holder.ubsSelectedName = "";

        }
        // Check if the button inform is visible
        if (!getShowButtonInform()) {

            holder.buttonMedicineInform.setVisibility(View.GONE);

        }

    }

    @Override
    private int getItemCount() {

        return dataMedicine.size();

    }

    private Boolean getShowButtonUBSs() {

        return this.showButtonUBSs;

    }
    
    private Boolean getShowButtonInform() {

        return this.showButtonInform;

    }

    private String getUbsName() {

        return this.ubsName;

    }


    private void showInformButtonIfThereIsACurrentUser() {

        /* Check if the User is logged in before showing the button

        if (getCurrentUser() != null){
            setShowButtonInform(true);
        }
        */
     }

}

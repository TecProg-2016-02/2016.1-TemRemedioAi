/*
 * File: CardListAdapterUBS.java
 * Purpose: Shows the list of UBSs available
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import static junit.framework.Assert.*;
import android.util.Log;

import com.gppmds.tra.temremdioa.controller.adapter.holder.ViewHolderUBS;
import com.gppmds.tra.temremdioa.model.UBS;
import com.tra.gppmds.temremdioa.R;

import java.util.List;

public class CardListAdapterUBS extends RecyclerView.Adapter<ViewHolderUBS> implements Filterable{

    public static List<UBS> dataUBS = new ArrayList<UBS>();

    List<UBS> filterDataUBS = new ArrayList<UBS>();


    FilterSearchUBS filter;

    private CardListAdapterUBS(Context context, List<UBS> dataUBS) {

        for(int i = 0; i < dataUBS.length; i++) {
            Log.d("CardListAdapterUBS -> constructor",
                  "DataUBS["+ i +"] = " + dataUBS[i]);
        }

        this.contextOpen = context;
        this.dataUBS = dataUBS;
        this.filterDataUBS = dataUBS;

        setShowButtonMedicines(true);
        setShowButtonInform(false);
        setMedicineName("");
        setMedicineDos("");
    }

    @Override
    public FilterSearchUBS getFilter() {

        // Check if exist a filterSearch for ubs created
        if(filter == null) {
            filter = new FilterSearchUBS(filterDataUBS, this);
        } else {
            // Nothing to do
        }

        return filter;
    }

    @Override
    public ViewHolderUBS onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.card_list_ubs, parent, false);
        return new ViewHolderUBS(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderUBS holder, int position) {

        UBS rowData = this.dataUBS.get(position);
        holder.getTextViewUbsName().setText(rowData.getUbsName());
        holder.getTextViewUbsNeighborhood().setText(rowData.getUbsNeighborhood());

        // Check if the button Medicine is visible
        if (!getShowButtonMedicines()) {
            holder.getButtonSelectMedicine().setVisibility(View.GONE);
        } else {
            // Nothing to do
        }

        // Check if the medicine name is empty
        if(!getMedicineName().isEmpty()){
            holder.medicineSelectedName = getMedicineName();
        } else {
            holder.medicineSelectedName = "";
        }

        // Check if the medicine dosage is empty
        if(!getMedicineDos().isEmpty()){
            holder.medicineSelectedDos = getMedicineDos();
        } else {
            holder.medicineSelectedDos = "";
        }

        // Check if the button inform is visible
        if(!getShowButtonInform()){
            holder.buttonUbsInform.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {

        return dataUBS.size();
    }

    private void setShowButtonMedicines(Boolean showButtonMedicines) {

        assertTrue(showButtonMedicines != null);
        this.showButtonMedicines = showButtonMedicines;
    }

    public void createFilter() {

        filter = new FilterSearchUBS(filterDataUBS, this);
        Boolean test = getShowButtonMedicines();
    }

    private void setShowButtonInform(boolean showButtonInform) {

        assertTrue(showButtonInform != null);
        this.showButtonInform = showButtonInform;
    }

    public boolean getShowButtonInform(){
        return this.showButtonInform;
    }

    private void setMedicineName(String medicineName) {

        assertEquals("", medicineName);
        assert medicineName != null;
        this.medicineName = medicineName;
    }

    public String getMedicineName() {

        return this.medicineName;
    }

    private void setMedicineDos(String medicineDos) {

        assertEquals("", medicineDos);
        assert medicineDos != null;
        this.medicineDos= medicineDos;
    }

    public String getMedicineDos(){

        return this.medicineDos;
    }

    private static Context contextOpen;

    private Boolean showButtonMedicines;

    private Boolean showButtonInform;

    private String medicineName;

    private String medicineDos;

    private Boolean getShowButtonMedicines() {

        return this.showButtonMedicines;
    }
}

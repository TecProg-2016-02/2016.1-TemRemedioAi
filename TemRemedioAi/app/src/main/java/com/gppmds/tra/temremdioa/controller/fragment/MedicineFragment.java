/*
 * File: MedicineFragment.java
 * Purpose: Consult the medication on parse
 */

package com.gppmds.tra.temremdioa.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tra.gppmds.temremdioa.R;

import java.util.List;


public class MedicineFragment extends Fragment {

    private RecyclerView medicineRecyclerView;
    
    public static CardListAdapterMedicine medicineAdapter;

    public MedicineFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_medicine, container, false);

        medicineAdapter = new CardListAdapterMedicine(MedicineFragment.this.getContext(),getListOfMedicines());
        medicineAdapter.setShowButtonInform(false);
        medicineAdapter.setUbsName("");

        medicineRecyclerView = (RecyclerView) rootView.findViewById(R.id.medicine_recycler_view);
        medicineRecyclerView.setHasFixedSize(true);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicineRecyclerView.setAdapter(medicineAdapter);

        return rootView;
    }

    /*
    * Get list of medicines from database
    * @params none
    * @return List of medicines found
    */
    public List<Medicine> getListOfMedicines() {

        final int limitMedicines = 1000;

        // Query medicine data from parse
        ParseQuery<Medicine> queryMedicine = Medicine.getQuery();
        queryMedicine.fromLocalDatastore();
        queryMedicine.setLimit(limitMedicines);
        List<Medicine> medicines = null;

        try {
            medicines = queryMedicine.find();
        } catch (ParseException exception) {
            exception.printStackTrace();
        }

        return medicines;
    }

    /*
    * Instance a fragment of the activity medicine for manipulation
    * @params none
    * @return an instantiated object
    */
    public static MedicineFragment newInstance() {

        return new MedicineFragment();
    }

    public static CardListAdapterMedicine getMedicineAdapter() {

        return medicineAdapter;
    }
}
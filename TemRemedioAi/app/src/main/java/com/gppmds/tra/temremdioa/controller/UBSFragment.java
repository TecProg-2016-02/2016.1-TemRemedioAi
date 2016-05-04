package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gppmds.tra.temremdioa.model.UBS;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tra.gppmds.temremdioa.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carolina on 01/05/16.
 * Updated by Guilherme on 02/05/2016.
 */
public class UBSFragment extends Fragment{

    public RecyclerView recyclerView;

    public UBSFragment(){
    }

    public static UBSFragment newInstance(){
       return new UBSFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ubs, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<String> dataUBS = new ArrayList<String>();
        dataUBS.add("Teste 1");
        dataUBS.add("Teste 2");
        dataUBS.add("Teste 3");

        ParseQuery<UBS> queryUBS = ParseQuery.getQuery(UBS.class);
        queryUBS.findInBackground(new FindCallback<UBS>() {
            @Override
            public void done(List<UBS> list, ParseException e) {
//                if (e == null) {
                    recyclerView.setAdapter(new CardListAdapterUBS(UBSFragment.this.getContext(), list));
//                } else {
//                    Log.d("Error: ", e.getMessage());
//                }
            }
        });

        return rootView;
    }
}

package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterUBS;
import com.gppmds.tra.temremdioa.model.UBS;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tra.gppmds.temremdioa.R;

import java.util.Arrays;
import java.util.List;

public class SelectUBSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_ubs);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);

        String nomeRemedio = getIntent().getStringExtra("nomeRemedio");
        String nivelAtencaoRemedio = getIntent().getStringExtra("nivelAtencao");
        String filtrosNivelAtencao[] = nivelAtencaoRemedio.split(", ");

        TextView textViewRemedioSelecionado = (TextView) findViewById(R.id.textViewRemedioSelecionado);
        textViewRemedioSelecionado.setText(nomeRemedio);

        ParseQuery<UBS> queryUBS = UBS.getQuery();
        queryUBS.whereContainedIn(UBS.getTitleNivelAt(), Arrays.asList(filtrosNivelAtencao));
        queryUBS.orderByAscending(UBS.getTitleNomEstab());
        List<UBS> ubss;
        try {
            ubss = queryUBS.find();

            CardListAdapterUBS claUbs = new CardListAdapterUBS(SelectUBSActivity.this, ubss);
            claUbs.setShowButtonRemedios(false);
            recyclerView.setAdapter(claUbs);

            TextView textViewQuantidadeItens = (TextView) findViewById(R.id.textViewQuantidadeItens);
            textViewQuantidadeItens.setText(ubss.size() + " UBSs encontradas");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

package com.gppmds.tra.temremdioa.controller.adapter.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.gppmds.tra.temremdioa.controller.Inform;
import com.gppmds.tra.temremdioa.controller.SelectUBSActivity;
import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.gppmds.tra.temremdioa.model.Notification;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tra.gppmds.temremdioa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewHolderMedicine extends RecyclerView.ViewHolder {
    private TextView textViewMedicineName;
    private TextView textViewLastInformation1;
    private TextView textViewLastInformation2;
    private TextView textViewLastInformation3;
    private TextView textViewLastInformationTitle;
    private TextView textViewWithoutNotification;
    private TextView textViewMedicineUnit;
    private TextView textViewMedicineDosage;
    private RelativeLayout headerLayout;
    private RelativeLayout expandLayout;
    private ValueAnimator mAnimator;
    private Button buttonSelectUbs;
    public Button buttonMedicineInform;
    private ImageView imageViewArrow;
    public String ubsSelectedName;
    public Boolean haveNotification;
    private PieChart pieChart;

    public ViewHolderMedicine(CardView card) {
        super(card);
        this.textViewMedicineName = (TextView) card.findViewById(R.id
                .textViewMedicineName);
        this.textViewLastInformation1 = (TextView) card.findViewById(R.id
                .textViewLastInformation1);
        this.textViewLastInformation2 = (TextView) card.findViewById(R.id
                .textViewLastInformation2);
        this.textViewLastInformation3 = (TextView) card.findViewById(R.id
                .textViewLastInformation3);
        this.textViewLastInformationTitle = (TextView) card.findViewById(R
                .id.textViewLastInformationTitle);
        this.textViewWithoutNotification = (TextView) card.findViewById(R.id
                .textViewWithoutNotification);
        this.textViewMedicineDosage = (TextView) card.findViewById(R.id
                .textViewMedicineDosage);
        this.textViewMedicineUnit = (TextView) card.findViewById(R.id
                .textViewMedicineUnit);
        this.imageViewArrow = (ImageView) card.findViewById(R.id.imageViewArrow);
        this.buttonSelectUbs = (Button) card.findViewById(R.id.buttonSelectUbs);
        this.buttonMedicineInform = (Button) card.findViewById(R.id
                .buttonInformRemedio);
        this.expandLayout = (RelativeLayout) card.findViewById(R.id.expandable);
        this.headerLayout = (RelativeLayout) card.findViewById(R.id.header);
        this.pieChart = (PieChart) card.findViewById(R.id.pie_chart_medicine);

        this.expandLayout.setVisibility(View.GONE);

        this.expandLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        expandLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        expandLayout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(sequence.ZERO.ordinal(), View
                                .MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(sequence.ZERO.ordinal(), View
                                .MeasureSpec.UNSPECIFIED);
                        expandLayout.measure(widthSpec, heightSpec);

                        mAnimator = slideAnimator(sequence.ZERO.ordinal(), expandLayout.getMeasuredHeight());
                        return true;
                    }
                });

        this.headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LOG", "onClickListener of headerLayout clicked");
                if (expandLayout.getVisibility() == View.GONE) {
                    Log.i("LOG", "Expand Click");
                    Medicine selectItem = CardListAdapterMedicine.dataMedicine
                            .get(ViewHolderMedicine.this.getAdapterPosition());

                    List<Notification> notificationList = null;
                    notificationList = getNotifications(selectItem);

                    haveNotification = false;
                    if (notificationList.size() >= sequence.ONE.ordinal()) {
                        haveNotification = true;
                        getTextViewLastInformation1().setText("1. " +
                                generateTextNotification(notificationList.get(sequence.ZERO.ordinal())));
                    } else {
                        getTextViewLastInformation1().setText("");
                    }

                    if (notificationList.size() >= sequence.TWO.ordinal()) {
                        getTextViewLastInformation2().setText("2. " +
                                generateTextNotification(notificationList.get(sequence.ONE.ordinal())));
                    } else {
                        getTextViewLastInformation2().setText("");
                    }

                    if (notificationList.size() >= sequence.THREE.ordinal()) {
                        getTextViewLastInformation3().setText("3. " +
                                generateTextNotification(notificationList.get(sequence.TWO.ordinal())));
                    } else {
                        getTextViewLastInformation3().setText("");
                    }

                    if (haveNotification) {
                        setInformationOfChart(selectItem);
                        getTextViewWithoutNotification().setVisibility(View.GONE);
                    } else {
                        getTextViewLastInformationTitle().setText("");
                        setInformationOfChartWithoutNotification();
                    }

                    ParseUser getCurrentUser = ParseUser.getCurrentUser();
                    if (getCurrentUser != null && getButtonMedicineInform()
                            .getVisibility() == View.VISIBLE) {
                        getButtonMedicineInform().setVisibility(View.VISIBLE);
                    } else {
                        getButtonMedicineInform().setVisibility(View.GONE);
                    }

                    expand();
                } else {
                    Log.i("LOG", "Collapse Click");
                    collapse();
                }
            }
        });

        this.buttonSelectUbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectUBSActivity.class);

                Medicine selectItem = CardListAdapterMedicine.dataMedicine
                        .get(ViewHolderMedicine.this.getAdapterPosition());
                intent.putExtra("nomeRemedio", selectItem.getMedicineDescription());
                intent.putExtra("nivelAtencao", selectItem.getMedicineAttentionLevel());
                intent.putExtra("medicineDos", selectItem.getMedicineDosage());

                v.getContext().startActivity(intent);
            }
        });

        this.buttonMedicineInform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Inform.class);
                Medicine selectedItem = CardListAdapterMedicine.dataMedicine
                        .get(ViewHolderMedicine.this.getAdapterPosition());
                intent.putExtra("MedicineName", selectedItem.getMedicineDescription());
                intent.putExtra("MedicineDos", selectedItem.getMedicineDosage());
                intent.putExtra("UBSName",ubsSelectedName);

                view.getContext().startActivity(intent);
            }
        });
    }

    public TextView getTextViewMedicineName(){
        return this.textViewMedicineName;
    }
    public TextView getTextViewWithoutNotification(){
        return this.textViewWithoutNotification;
    }
    public TextView getTextViewMedicineUnit() {
        return this.textViewMedicineUnit;
    }
    public TextView getTextViewLastInformationTitle() {
        return this.textViewLastInformationTitle;
    }
    public TextView getTextViewLastInformation1() {
        return this.textViewLastInformation1;
    }
    public TextView getTextViewLastInformation2() {
        return this.textViewLastInformation2;
    }
    public TextView getTextViewLastInformation3() {
        return this.textViewLastInformation3;
    }
    public TextView getTextViewMedicineDosage() {
        return this.textViewMedicineDosage;
    }
    public Button getButtonMedicineInform() {
        return this.buttonMedicineInform;
    }
    public Button getButtonSelectUbs() {
        return this.buttonSelectUbs;
    }

    public PieData getDataPie(Medicine medicine) {

        ParseQuery<Notification> queryNotificationAvailable = Notification.getQuery();
        queryNotificationAvailable.fromLocalDatastore();
        queryNotificationAvailable.whereEqualTo(Notification
                .getTitleMedicineName(), medicine.getMedicineDescription());
        queryNotificationAvailable.whereEqualTo(Notification
                .getTitleMedicineDosage(), medicine.getMedicineDosage());
        queryNotificationAvailable.whereEqualTo(Notification
                .getTitleAvailable(), true);
        
        if (ubsSelectedName != "") {
            queryNotificationAvailable.whereEqualTo(Notification
                    .getTitleUBSName(), ubsSelectedName);
        } else {
            // Nothing to do
        }

        Integer countNotificationAvailable = 0;
        try {
            countNotificationAvailable = queryNotificationAvailable.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<Notification> queryNotificationNotAvailable = Notification.getQuery();
        queryNotificationNotAvailable.fromLocalDatastore();
        queryNotificationNotAvailable.whereEqualTo(Notification
                .getTitleMedicineName(), medicine.getMedicineDescription());      
        queryNotificationNotAvailable.whereEqualTo(Notification
                .getTitleMedicineDosage(), medicine.getMedicineDosage());
        queryNotificationNotAvailable.whereEqualTo(Notification
                .getTitleAvailable(), false);
        
        if (ubsSelectedName != "") {
            queryNotificationNotAvailable.whereEqualTo(Notification
                    .getTitleUBSName(), ubsSelectedName);
        } else {
            // Nothing to do
        }

        Integer countNotificationNotAvailable = 0;
        try {
            countNotificationNotAvailable = queryNotificationNotAvailable.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<String> valuesLegend = new ArrayList<String>();
        valuesLegend.add("Sim");
        valuesLegend.add("Não");

        ArrayList<Entry> valuesAvailable = new ArrayList<Entry>();
        valuesAvailable.add(new Entry((float) countNotificationAvailable, sequence.ZERO.ordinal()));
        valuesAvailable.add(new Entry((float) countNotificationNotAvailable, sequence.ONE.ordinal()));

        PieDataSet pieDataSet = new PieDataSet(valuesAvailable, "");
        int color [] = {Color.parseColor("#00BEED"), Color.parseColor("#FFED4F")};
        pieDataSet.setColors(color);
        pieDataSet.setSliceSpace(5);
        pieDataSet.setValueTextSize(10);

        PieData pieData = null;
        pieData = new PieData(valuesLegend, pieDataSet);

        return pieData;
    }

    private String generateTextNotification(Notification notification) {

        String textOfNotification = "";
        if (notification.getAvailable()) {
            textOfNotification = "Disponível em ";
        } else {
            textOfNotification = "Indisponível em ";
        }

        Calendar dayCalendar = Calendar.getInstance(new Locale("pt", "BR"));
        dayCalendar.setTime(notification.getDateInform());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
        textOfNotification = textOfNotification + simpleDateFormat
                .format(dayCalendar.getTime());

        return textOfNotification;
    }

    private List<Notification> getNotifications(Medicine medicine) {

        ParseQuery<Notification> queryNotification = Notification.getQuery();
        queryNotification.whereEqualTo(Notification.getTitleMedicineName(),
                medicine.getMedicineDescription());
        queryNotification.whereEqualTo(Notification.getTitleMedicineDosage(),
                medicine.getMedicineDosage());
        
        if (!ubsSelectedName.isEmpty()) {
            queryNotification.whereEqualTo(Notification.getTitleUBSName(),
                    ubsSelectedName);
        }
        
        queryNotification.orderByDescending(Notification.getTitleDateInform());
        queryNotification.setLimit(sequence.THREE.ordinal());

        List<Notification> listNotification = null;
        try {
            listNotification = queryNotification.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return listNotification;
    }

    private void setInformationOfChartWithoutNotification() {
        
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(sequence.ZERO.ordinal());
        pieChart.setTransparentCircleRadius(ran.getNum().FORTY);
        pieChart.setDrawSliceText(false);
        pieChart.setRotationAngle(sequence.ZERO.ordinal());
        pieChart.setRotationEnabled(true);
        
        pieChart.animateY(ran.getNum().ONE_HUNDRED);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.getLegend().setTextSize(ran.getNum().TWELVE);

        pieChart.setNoDataTextDescription("Sem notificações encontradas.");

        ArrayList<String> valuesLegend = new ArrayList<String>();
        valuesLegend.add("Sem Notificação");

        ArrayList<Entry> valuesAvailable = new ArrayList<Entry>();
        valuesAvailable.add(new Entry((float) sequence.ONE.ordinal(), sequence.ZERO.ordinal()));

        PieDataSet pieDataSet = new PieDataSet(valuesAvailable, "");
        int color [] = {Color.parseColor("#F0F0F0")};
        pieDataSet.setColors(color);
        pieDataSet.setSliceSpace(5);

        PieData pieData = new PieData(valuesLegend, pieDataSet);
        for (IDataSet<?> set : pieData.getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        pieChart.setData(pieData);
    }

    private void setInformationOfChart(Medicine medicine) {

        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(sequence.ZERO.ordinal());
        pieChart.setTransparentCircleRadius(ran.getNum().FORTY);
        pieChart.setDrawSliceText(false);
        pieChart.setRotationAngle(sequence.ZERO.ordinal());
        pieChart.setRotationEnabled(true);

        pieChart.animateY(ran.getNum().ONE_HUNDRED);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.getLegend().setTextSize(ran.getNum().TWELVE);

        pieChart.setNoDataTextDescription("Sem notificações encontradas.");
        pieChart.setData(getDataPie(medicine));
    }

    private void expand() {
        /* set Visible */
        Log.i("LOG", "Expand enter, View.VISIBLE");

        expandLayout.setVisibility(View.VISIBLE);
        
        mAnimator.start();
        
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up);
    }

    private void collapse() {

        int finalHeight = expandLayout.getHeight();
        ValueAnimator mAnimator2 = slideAnimator(finalHeight, sequence.ONE.ordinal());
        mAnimator2.addListener(new Animator.AnimatorListener(){
            
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i("LOG", "collapse onAnimationEnd enter, View.GONE");
                expandLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
                /* Method declared empty because the override is mandatory */
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                /* Method declared empty because the override is mandatory */
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                /* Method declared empty because the override is mandatory */
            }
        });

        mAnimator2.start();
        
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down);
    }

    private ValueAnimator slideAnimator(int start, int end) {
        

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
        
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                /* Update Height */
                ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
                int value = (Integer) valueAnimator.getAnimatedValue();
                layoutParams.height = value;
                expandLayout.setLayoutParams(layoutParams);
            }
        });
        
        return animator;
    }

    private enum ran {
        FORTY(40), ONE_HUNDRED(100), TWELVE(12);

        private int num;

        test(int num){
            this.num = num;
        }

        public int getNum(){
            return num;
        }
    }

    private final enum sequence{ 
        ZERO, ONE, TWO, THREE;
    }
}
package com.gppmds.tra.temremdioa.controller;

import com.gppmds.tra.temremdioa.model.Medicine;
import com.gppmds.tra.temremdioa.model.Notification;
import com.gppmds.tra.temremdioa.model.UBS;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ParseInitializer extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();

        registerParserSubClasses();
        inicializeParser();

        loadLocalDateBaseMedicine();
        loadLocalDateBaseUBS();
        loadLocalDateBaseNotification();
    }

    public boolean registerParserSubClasses() {
        try {
            ParseObject.registerSubclass(UBS.class);
            ParseObject.registerSubclass(Medicine.class);
            ParseObject.registerSubclass(Notification.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean inicializeParser() {
        /* Establish connection with parse server */
        try {
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId("TemRemedioAi")
                    .server("http://temremedioai.herokuapp.com/temremedioai/Class")
                    .clientKey("kijasijijasiasjsiajalllkaosiajhsis")
                    .enableLocalDataStore()
                    .build()
            );

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean loadLocalDateBaseMedicine() {
        ParseQuery<Medicine> queryMedicine = Medicine.getQuery();
        queryMedicine.setLimit(1000); // Set limit of medicines to show on screen
        queryMedicine.findInBackground(new FindCallback<Medicine>() {
            @Override
            public void done(List<Medicine> list, ParseException e) {
                if (e != null) {
                    // Nothing to do
                } else {
                    // To save queried Medicines to local database
                    Medicine.pinAllInBackground(list);
                }
            }
        });
        return true;
    }

    public boolean loadLocalDateBaseUBS() {
        ParseQuery<UBS> queryUBS = UBS.getQuery();
        queryUBS.setLimit(120); // Set limit of UBS to show on screen
        queryUBS.findInBackground(new FindCallback<UBS>() {
            @Override
            public void done(List<UBS> list, ParseException e) {
                if (e != null) {
                    // Nothing to do
                } else {
                    // To save queried UBS to local database
                    UBS.pinAllInBackground(list);
                }
            }
        });
        return true;
    }

    public boolean loadLocalDateBaseNotification() {
        ParseQuery<Notification> queryNotification = Notification.getQuery();
        queryNotification.setLimit(1000); // Set limit of notifications to show on screen
        queryNotification.findInBackground(new FindCallback<Notification>() {
            @Override
            public void done(List<Notification> list, ParseException e) {
                if (e != null) {
                    // Nothing to do
                } else {
                    // To save queried notification to local database
                    Notification.pinAllInBackground(list);
                }
            }
        });

        return true;
    }
}

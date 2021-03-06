package com.gppmds.tra.temremdioa.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import static junit.framework.Assert.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tra.gppmds.temremdioa.R;

public class UbsMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double latitude; // Set UBS location
    private Double longitude; // Set UBS location
    private String ubsName;

    private static final int LAT_LONG_ZOOM = 13;
    private static final int NEUTRAL_PARAMETER = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubs_maps);

        /* Obtain the SupportMapFragment and get notified when the map is ready to be used */
        obtainSupportMapFragment();

        gettingLatitudeAndLongitude();

        /* Getting UBS descriptions from UBS Holder class */
        gettingUbsFromHolder();

        /* Ubs trajectory */
        generateTrajectory();
    }

    private void obtainSupportMapFragment () {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                                                    .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    };

    private void gettingLatitudeAndLongitude() {

        assertTrue(getIntent().getDoubleExtra("latitude", NEUTRAL_PARAMETER) != null);
        assertTrue(getIntent().getDoubleExtra("longitude", NEUTRAL_PARAMETER) != null);

        latitude = getIntent().getDoubleExtra("latitude", NEUTRAL_PARAMETER);
        longitude = getIntent().getDoubleExtra("longitude", NEUTRAL_PARAMETER);
    }

    private void gettingUbsFromHolder() {
        gettingUbsName();
        gettingUbsAddress();
        gettingUbsNeighborhood();
        gettingUbsCity();
        gettingUbsPhone();
    }

    private void gettingUbsName() {
        ubsName = getIntent().getStringExtra("nomeUBS");
        TextView editName = (TextView) findViewById(R.id.textViewUbsName);
        editName.setText(ubsName);
    }

    private void gettingUbsAddress () {
        String descUbsAddress = getIntent().getStringExtra("descEnderecoUBS");
        TextView editDscAddress = (TextView) findViewById(R.id.textViewUbsAddress);
        editDscAddress.setText(descUbsAddress);
    }

    private void gettingUbsNeighborhood() {
        String descUbsNeighborhood = getIntent().getStringExtra("descBairroUBS");
        TextView editDscNeighborhood = (TextView)
                                            findViewById(R.id.textViewUbsNeighborhood);
        editDscNeighborhood.setText(descUbsNeighborhood);
    }

    private void gettingUbsCity() {
        String descUbsCity = getIntent().getStringExtra("descCidadeUBS");
        TextView editDscCity = (TextView) findViewById(R.id.textViewCityUbs);
        editDscCity.setText(descUbsCity);
    }

    private void gettingUbsPhone() {
        String descUbsPhone = getIntent().getStringExtra("telefoneUBS");
        TextView editDscPhone = (TextView) findViewById(R.id.textViewPhoneUbs);
        editDscPhone.setText(descUbsPhone);
    }

    private void generateTrajectory() {
        FloatingActionButton generateTrajectory;
        generateTrajectory = (FloatingActionButton) findViewById(R.id.direction);
        generateTrajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMapIfNeeded();
            }
        });
    }

    private void setUpMapIfNeeded() {
        if(isGoogleMapsInstalled()) {
            /* Get latitude and longitude from ubs Holder and open with GMaps */
            String uri = "http://maps.google.com/maps?saddr="+"&daddr="+latitude+","
                                                                       +longitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                                Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps",
                                "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Maps");
            builder.setIcon(R.drawable.google_maps_icon);
            builder.setMessage("Instalar Google Maps");
            builder.setCancelable(true);
            builder.setNegativeButton("Cancelar",null);
            builder.setPositiveButton("Instalar", getGoogleMapsListener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo("com.google.android.apps.maps", NEUTRAL_PARAMETER);
            return true;
        } catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.apps.maps"));
                startActivity(intent);

                /* Finish the activity so they can't circumvent the check */
                finish();
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /* Get latitude and longitude to create a marker on map */
        LatLng latLngValues = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLngValues).title(ubsName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngValues, LAT_LONG_ZOOM));
    }
}

package com.example.opendoor.Screen;

import android.annotation.SuppressLint;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opendoor.Adapter.WeatherAdapter;
import com.example.opendoor.R;
import com.example.opendoor.Utilities.DateTimeUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView tab_hourly, tab_weekly, locationView;
    private RecyclerView view_hourly, view_weekly;
    private ViewAnimator viewAnimator;
    private ImageButton bt_local, bt_list,bt_add;
    private final List<Object> dataHourly = new ArrayList<>();
    private final List<Object> dataWeekly = new ArrayList<>();
    private WeatherAdapter adapterHourly, adapterWeekly;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tab_hourly = findViewById(R.id.tab_hourly);
        tab_weekly = findViewById(R.id.tab_weekly);
        locationView = findViewById(R.id.location);

        view_hourly = findViewById(R.id.view_hourly);
        view_weekly = findViewById(R.id.view_weekly);

        viewAnimator = findViewById(R.id.animator);

        bt_add = findViewById(R.id.bt_add);
        bt_local = findViewById(R.id.bt_local);
        bt_list = findViewById(R.id.bt_list);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        bt_local.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu quyền nếu chưa được cấp
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Quyền đã được cấp, bắt đầu lấy vị trí
                getLocation();
            }
        });

        bt_add.setOnClickListener(v->{

        });
        bt_list.setOnClickListener(v->{

        });

        view_hourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        view_weekly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        tab_hourly.setOnClickListener(v -> {
            tab_hourly.setTextColor(ContextCompat.getColor(this, R.color.color_select));
            tab_hourly.setBackgroundResource(R.drawable.view_tab_select);
            tab_weekly.setTextColor(ContextCompat.getColor(this, R.color.color_unselect));
            tab_weekly.setBackgroundResource(R.color.transparent);
            viewAnimator.setDisplayedChild(0);
            viewAnimator.setInAnimation(this,R.anim.slide_in_right);
            viewAnimator.setOutAnimation(this,R.anim.slide_out_right);

        });

        tab_weekly.setOnClickListener(v -> {
            tab_weekly.setTextColor(ContextCompat.getColor(this, R.color.color_select));
            tab_weekly.setBackgroundResource(R.drawable.view_tab_select);
            tab_hourly.setTextColor(ContextCompat.getColor(this, R.color.color_unselect));
            tab_hourly.setBackgroundResource(R.color.transparent);
            viewAnimator.setDisplayedChild(1);

            viewAnimator.setInAnimation(this,R.anim.slide_in_left);
            viewAnimator.setOutAnimation(this,R.anim.slide_out_left);
        });

        setDataHourly();
        setDataWeekly();


        adapterHourly = new WeatherAdapter(R.layout.item_weather, dataHourly);
        adapterWeekly = new WeatherAdapter(R.layout.item_weather, dataWeekly);

        view_hourly.setAdapter(adapterHourly);
        view_weekly.setAdapter(adapterWeekly);

//        int index_hourly = ((List<String>) dataHourly.get(0)).indexOf("Now");
//
//        view_hourly.post(
//                ()->view_hourly.smoothScrollToPosition(
//                        index_hourly<22?
//                                index_hourly+2:
//                                index_hourly<23?
//                                        index_hourly+1:
//                                        index_hourly
//                )
//        );
//        Log.d("tag time", DateTimeUtils.getListTimeInDay().toString());
//        Log.d("tag time", DateTimeUtils.getListDateInWeek().toString());


    }
    private void setDataHourly(){
        dataHourly.add(DateTimeUtils.getListTimeInDay());
        dataHourly.add(new ArrayList<>(Arrays.asList(
                "39%","50%","39%","50%","39%","50%","39%","50%","39%","50%","39%","50%",
                "39%","50%","39%","50%","39%","50%","39%","50%","39%","50%","39%","50%"
        )));
        dataHourly.add(new ArrayList<>(Arrays.asList(
                "19°C","20°C","19°C","20°C","19°C","20°C","19°C","20°C","19°C","20°C","19°C","20°C",
                "19°C","20°C","19°C","20°C","19°C","20°C","19°C","20°C","19°C","20°C","19°C","20°C"
        )));
        dataHourly.add(new ArrayList<>(Arrays.asList(
                R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,
                R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,
                R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,
                R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain
        )));
    }
    private void setDataWeekly(){
        dataWeekly.add(DateTimeUtils.getListDateInWeek());
        dataWeekly.add(new ArrayList<>(Arrays.asList(
                "39%","50%","39%","50%","39%","50%","39%"
        )));
        dataWeekly.add(new ArrayList<>(Arrays.asList(
                "19°C","20°C","19°C","20°C","19°C","20°C","19°C"
        )));
        dataWeekly.add(new ArrayList<>(Arrays.asList(
                R.drawable.sun_big_rain,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,R.drawable.sun,R.drawable.sun_rain,R.drawable.sun
        )));
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        updateLocationTextView(location);
                    } else {
                        Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(this, "Error getting location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateLocationTextView(Location location) {
        String locationText = "Latitude: " + location.getLatitude() + "\nLongitude: " + location.getLongitude();
        Log.d("tag location", locationText);
        getAddressFromLocation(location);
//        locationView.setText(locationText);
    }
    @SuppressLint("SetTextI18n")
    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                List<String> currentAddress = Arrays.asList(address.getAddressLine(0).split(","));
                Log.d("tag address", address.getAddressLine(0));
                locationView.setText(currentAddress.get(1)+"\n"+currentAddress.get(2));
            } else {
                Log.d("tag address", "No address found");
            }
        } catch (IOException e) {
            Log.d("tag address", "Geocoder service not available or network issues");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

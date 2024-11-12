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
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.opendoor.Adapter.WeatherAdapter;
import com.example.opendoor.Assets.ApiClient;
import com.example.opendoor.Assets.ClientService;
import com.example.opendoor.Models.ApiResponse;
import com.example.opendoor.R;
import com.example.opendoor.Utilities.DateTimeUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {
    private TextView tab_hourly, tab_weekly, locationView,temp_day,humidity,temp;
    private ImageView temp_img;
    private RecyclerView view_hourly, view_weekly;
    private ViewAnimator viewAnimator;
    private ImageButton bt_local,reload;
    private final List<Object> dataHourly = new ArrayList<>(Arrays.asList(new ArrayList<>(),new ArrayList<>(),new ArrayList<>()));
    private final List<Object> dataWeekly = new ArrayList<>(Arrays.asList(new ArrayList<>(),new ArrayList<>(),new ArrayList<>()));
    private WeatherAdapter adapterHourly, adapterWeekly;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private ProgressBar progressBar;
    private ConstraintLayout background;
    private SwipeRefreshLayout refreshLayout;
    private LottieAnimationView animationView;

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

        bt_local = findViewById(R.id.bt_local);
        reload = findViewById(R.id.reload);

        progressBar = findViewById(R.id.progressBar);
        temp_img = findViewById(R.id.img_temp);
        temp = findViewById(R.id.temp);
        temp_day = findViewById(R.id.temp_day);
        humidity = findViewById(R.id.humidity);

        background = findViewById(R.id.background);

        animationView = findViewById(R.id.animation);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        refreshLayout = findViewById(R.id.layout_refresh);
        refreshLayout.setOnRefreshListener(() -> {
            CallAPI();
            refreshLayout.setRefreshing(false);
        });

        bt_local.setOnClickListener(v->{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu quyền nếu chưa được cấp
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // Quyền đã được cấp, bắt đầu lấy vị trí
                getLocation();
            }
        });


        view_hourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        view_weekly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adapterHourly = new WeatherAdapter(R.layout.item_weather, dataHourly);
        adapterWeekly = new WeatherAdapter(R.layout.item_weather, dataWeekly);

        view_hourly.setAdapter(adapterHourly);
        view_weekly.setAdapter(adapterWeekly);

        reload.setOnClickListener(v->{
            progressBar.setVisibility(View.VISIBLE);
            CallAPI();
        });

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

        CallAPI();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setDataHourly(List<Double> listTemp, List<Double> listRainProb, List<Integer> listIcon){
        dataHourly.clear();
        dataHourly.add(DateTimeUtils.getListTimeInDay());
        dataHourly.add(listRainProb);
        dataHourly.add(listTemp);
        dataHourly.add(listIcon);
        adapterHourly.notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setDataWeekly(List<Double> listTemp, List<Double> listRainProb, List<Integer> listIcon){
        dataWeekly.clear();
        dataWeekly.add(DateTimeUtils.getListDateInWeek());
        dataWeekly.add(listRainProb);
        dataWeekly.add(listTemp);
        dataWeekly.add(listIcon);
        adapterWeekly.notifyDataSetChanged();

    }
    private void CallAPI(){
        progressBar.setVisibility(View.VISIBLE);
        reload.setVisibility(View.INVISIBLE);

        String jsonData = "{"
                + "\"hours_ahead\": 168"
                + "}";
        ApiClient.postJson(ClientService.URL_PREDICT, jsonData, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("tag onFailure", "API call failed: " + e);
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    reload.setVisibility(View.VISIBLE);
                    Toast.makeText(MainActivity.this, "API request failed", Toast.LENGTH_SHORT).show();
                });
            }

            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(responseData, ApiResponse.class);

                    if (apiResponse.isSuccess()) {
                        ApiResponse.Data data = apiResponse.getData();
                        Log.d("tag temp", "Temperature Forecast: " + data.getTempForecast().size());
                        Log.d("tag humidity", "Humidity Forecast: " + data.getHumidityForecast().size());
                        Log.d("tag rain prob", "Rain Probability: " + data.getRainProb().size());

                        runOnUiThread(() -> {
                            setDataHourly(
                                    data.getTempForecast().subList(0, 24),
                                    data.getRainProb().subList(0, 24),
                                    mapRainProbToIcons(data.getRainProb().subList(0, 24))
                            );
                            List<Double> dataProb = dataWeek(data.getRainProb().subList(1,data.getRainProb().size()));
                            setDataWeekly(
                                    dataWeek(data.getTempForecast().subList(1,data.getTempForecast().size())),
                                    dataProb,
                                    mapRainProbToIcons(dataProb)
                            );
                            // Hide the progress bar after data is loaded
                            progressBar.setVisibility(View.GONE);
                            temp.setText(String.format("%.0f°C", data.getTempForecast().get(0)));
                            Double highest = data.getTempForecast().subList(0,24-DateTimeUtils.getTimeNow()).stream().max(Double::compareTo).orElse(0.0);
                            Double lowest = data.getTempForecast().subList(0,24-DateTimeUtils.getTimeNow()).stream().min(Double::compareTo).orElse(0.0);
                            temp_day.setText(String.format("%.0f°C - %.0f°C", lowest,highest));
                            humidity.setText(String.format("Humidity: %.0f%%",data.getHumidityForecast().get(0)));
                            double prob = data.getRainProb().get(0);
                            temp_img.setImageResource(
                                    prob<=0.2?
                                            R.drawable.sun:
                                            prob>0.2&&prob<=0.5?
                                                    R.drawable.sun_rain:
                                                    R.drawable.sun_big_rain
                            );
                            background.setBackgroundResource(
                                    prob<=0.2?
                                            R.drawable.bg_light:
                                            prob>0.2&&prob<=0.5?
                                                    R.drawable.bg:
                                                    R.drawable.bg
                            );
                            temp_img.setVisibility(View.VISIBLE);
                            animationView.setAnimation(
                                    prob<=0.2? "Animation/sun.json":"Animation/rain.json"
                            );
                            Log.d("tag", String.valueOf(DateTimeUtils.getTimeNow()));
                        });
                    }
                } else {
                    runOnUiThread(() -> progressBar.setVisibility(View.GONE));
                    Log.e("tag call api fail", "POST request failed with code: " + response.code());
                }
            }
        });
    }
    private List<Double> dataWeek(List<Double> data){
        List<Double> avgTemps = new ArrayList<>();

        for (int i = 0; i < data.size(); i += 24) {
            double sum = 0;
            for (int j = i; j < i + 24; j++) {
                sum += data.get(j);
            }
            double avg = sum / 24;
            avgTemps.add(avg);
        }
        return avgTemps;
    }
    private List<Integer> mapRainProbToIcons(List<Double> rainProbList) {
        return rainProbList.stream().map(item -> {
            if (item <= 0.2) {
                return R.drawable.sun;
            } else if (item > 0.2 && item <= 0.5) {
                return R.drawable.sun_rain;
            } else {
                return R.drawable.sun_big_rain;
            }
        }).collect(Collectors.toList());
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

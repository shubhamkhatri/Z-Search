package com.example.android.zomatosearch.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.android.zomatosearch.R;
import com.example.android.zomatosearch.adapter.RestaurantAdapter;
import com.example.android.zomatosearch.model.ApiResponse;
import com.example.android.zomatosearch.model.Location;
import com.example.android.zomatosearch.model.Restaurant;
import com.example.android.zomatosearch.model.RestaurantsItem;
import com.example.android.zomatosearch.network.ZomatoApi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Restaurant> restaurantsList = new ArrayList<Restaurant>();
    private SearchView searchView;
    private ListView listView;
    private TextView noResturants;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private double latitude = 26.4552144, longitude = 80.3094175;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);
        searchView = findViewById(R.id.search_bar);
        progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        noResturants=findViewById(R.id.no_restaurant);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (ContextCompat.checkSelfPermission(
                        MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
                } else {
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    getCurrentLocation();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }


    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://developers.zomato.com/api/v2.1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private ZomatoApi getApi() {
        return getRetrofit().create(ZomatoApi.class);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(MainActivity.this, "Location Permission Denied!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            setSearch(latitude, longitude);
                        }
                    }
                }, Looper.getMainLooper());
    }

    private void setSearch(double lat, double longi) {
        restaurantsList.clear();

        getApi().getNearbyRestaurants(lat, longi, 40, 10000, searchView.getQuery().toString())
                .enqueue(new Callback<ApiResponse>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        List<RestaurantsItem> restaurants = response.body().getRestaurants();

                        for (int i = 0; i < restaurants.size(); i++) {
                            restaurantsList.add(new Restaurant(restaurants.get(i).getRestaurant().getId(),
                                    restaurants.get(i).getRestaurant().getName(),
                                    restaurants.get(i).getRestaurant().getLocation(),
                                    restaurants.get(i).getRestaurant().getCuisines(),
                                    restaurants.get(i).getRestaurant().getAverage_cost_for_two()));
                        }

                        //Grouping of cuisine
                        Map<Object, List<Restaurant>> listGrouped =
                                restaurantsList.stream().collect(Collectors.groupingBy(w -> w.getCuisines()));

                        ArrayList<Restaurant> finalList= new ArrayList<Restaurant>();
                        for (Object o : listGrouped.keySet()) {
                            finalList.add(new Restaurant("",
                                    "default",
                                    new Location("","",""),
                                    (String) o,
                                    0));
                            List<Restaurant> r=listGrouped.get(o);
                            for(Restaurant res: r){
                                finalList.add(res);
                            }
                        }
                        //test 2

                        //Setting Adapter
                        RestaurantAdapter restaurantAdapter = new RestaurantAdapter(MainActivity.this, finalList);
                        if(finalList.isEmpty()){
                            listView.setVisibility(View.GONE);
                            noResturants.setText("No Restaurants Found...");
                            noResturants.setVisibility(View.VISIBLE);
                        }else{
                        listView.setVisibility(View.VISIBLE);
                        noResturants.setVisibility(View.GONE);
                        }
                        listView.setAdapter(restaurantAdapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Couldn't find any nearby restaurants");
                        AlertDialog mDialog = builder.create();
                        mDialog.show();
                        progressDialog.dismiss();
                    }
                });

    }

}
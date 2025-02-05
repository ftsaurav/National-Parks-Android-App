package com.example.nationalparks;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.nationalparks.Adapter.CustomInfoWindowAdapter;
import com.example.nationalparks.Data.AsyncResponse;
import com.example.nationalparks.Data.Repository;
import com.example.nationalparks.Model.Park;
import com.example.nationalparks.Model.ParkViewModel;
import com.example.nationalparks.Util.util;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ParkViewModel parkViewModel;
    private List<Park> parkList;
    private CardView cardView;
    private EditText stateCodeEditText;
    private ImageButton searchButton;
    private String code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        parkViewModel= new ViewModelProvider(this).get(ParkViewModel.class);

        cardView= findViewById(R.id.cardView);
        stateCodeEditText= findViewById(R.id.floating_state_value_et);
        searchButton= findViewById(R.id.floating_search_button);

        BottomNavigationView bottomNavigationView= findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment= null;
                int id= item.getItemId();
                if(id==R.id.maps_nav_button)
                {
                    if(cardView.getVisibility()==View.INVISIBLE || cardView.getVisibility()==View.GONE)
                    {
                        cardView.setVisibility(View.VISIBLE);
                    }
                    parkList.clear();
                    mMap.clear();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
                    mapFragment.getMapAsync(MainActivity.this);
                    return true;
                }
                else if(id==R.id.parks_nav_button)
                {
                    if(cardView.getVisibility()==View.VISIBLE)
                    {
                        cardView.setVisibility(View.INVISIBLE);
                    }
                    selectedFragment= ParksFragment.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.map, selectedFragment).commit();
                    return true;
                }
                return true;
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parkList.clear();
                String stateCode= stateCodeEditText.getText().toString().trim();
                if(!TextUtils.isEmpty(stateCode))
                {
                    code = stateCode;
                    parkViewModel.selectCode(code);
                    onMapReady(mMap);
                    stateCodeEditText.setText("");
                    util.hideKeyboard(view);
                }

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
        mMap.setOnInfoWindowClickListener(this);
        parkList= new ArrayList<>();
        parkList.clear();
        // Add a marker in Sydney and move the camera
        mMap.clear();
        Repository.getParks(new AsyncResponse() {
            @Override
            public void processPark(List<Park> parks) {
                parkList= parks;
                for(Park park: parks)
                {
                    if(park.getLatitude()!=null && !park.getLatitude().isEmpty() && park.getLongitude()!=null && !park.getLongitude().isEmpty())
                    {
                        LatLng newPark = new LatLng(Double.parseDouble(park.getLatitude()), Double.parseDouble(park.getLongitude()));
                        Marker marker=mMap.addMarker(new MarkerOptions().position(newPark).title(park.getFullName()).snippet(park.getDesignation())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        marker.setTag(park);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPark, 5));
                        Log.d("Parks", "processPark: "+park.getFullName());
                    }
                }
                parkViewModel.setSelectedParks(parkList);
                Log.d("SIZE", "processPark: "+parkList.size());
            }
        }, code);
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        if(cardView.getVisibility()==View.VISIBLE)
        {
            cardView.setVisibility(View.INVISIBLE);
        }
        parkViewModel.setSelectedPark((Park)marker.getTag());
        getSupportFragmentManager().beginTransaction().replace(R.id.map, DetailsFragment.newInstance()).commit();
    }
}

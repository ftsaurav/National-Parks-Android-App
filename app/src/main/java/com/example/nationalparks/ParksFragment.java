package com.example.nationalparks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nationalparks.Adapter.OnParkClickListner;
import com.example.nationalparks.Adapter.ParkRecyclerViewAdapter;
import com.example.nationalparks.Data.AsyncResponse;
import com.example.nationalparks.Data.Repository;
import com.example.nationalparks.Model.Park;
import com.example.nationalparks.Model.ParkViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class ParksFragment extends Fragment implements OnParkClickListner {
    private RecyclerView recyclerView;
    private ParkRecyclerViewAdapter parkRecyclerViewAdapter;
    private List<Park> parkList;
    private ParkViewModel parkViewModel;

    public ParksFragment() {
        // Required empty public constructor
    }
    public static ParksFragment newInstance() {
        ParksFragment fragment = new ParksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parkList= new ArrayList<>();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parkViewModel= new ViewModelProvider(requireActivity()).get(ParkViewModel.class);
        if(parkViewModel.getParks().getValue()!=null)
        {
            parkList= parkViewModel.getParks().getValue();
            parkRecyclerViewAdapter= new ParkRecyclerViewAdapter(parkList, ParksFragment.this);
            recyclerView.setAdapter(parkRecyclerViewAdapter);
        }
//        Repository.getParks(new AsyncResponse() {
//            @Override
//            public void processPark(List<Park> parks) {
//                parkRecyclerViewAdapter= new ParkRecyclerViewAdapter(parks, ParksFragment.this);
//                recyclerView.setAdapter(parkRecyclerViewAdapter);
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_parks, container, false);

        recyclerView= view.findViewById(R.id.park_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onParkClicked(Park park) {
        Log.d("lund", "onParkClicked: "+ park.getName());
        parkViewModel.setSelectedPark(park);
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.park_fragment, DetailsFragment.newInstance())
                .commit();
    }
}
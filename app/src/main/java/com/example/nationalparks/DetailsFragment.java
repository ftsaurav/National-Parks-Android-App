package com.example.nationalparks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nationalparks.Adapter.ViewPagerAdapter;
import com.example.nationalparks.Model.Park;
import com.example.nationalparks.Model.ParkViewModel;

import org.w3c.dom.Text;

public class DetailsFragment extends Fragment {
    private ParkViewModel parkViewModel;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager2 viewPager;
    private LinearLayout dotsLayout;
    private int dotsCount;
//    private DotsIndicator  dotsIndicator;

    public DetailsFragment() {
        // Required empty public constructor
    }
    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager= view.findViewById(R.id.details_viewpager);
        dotsLayout = view.findViewById(R.id.dotsLayout);

        TextView name= view.findViewById(R.id.details_park_name);
        TextView designation= view.findViewById(R.id.details_park_designation);
        TextView description = view.getRootView().findViewById(R.id.details_description);
        TextView activities = view.getRootView().findViewById(R.id.details_activities);
        TextView entranceFees = view.getRootView().findViewById(R.id.details_entrancefees);
        TextView opHours = view.getRootView().findViewById(R.id.details_operatinghours);
        TextView detailsTopics = view.getRootView().findViewById(R.id.details_topics);
        TextView directions = view.getRootView().findViewById(R.id.details_directions);

        parkViewModel= new ViewModelProvider(requireActivity()).get(ParkViewModel.class);
        parkViewModel.getSelectedPark().observe(getViewLifecycleOwner(), new Observer<Park>() {
            @Override
            public void onChanged(Park park) {
                name.setText(park.getName());
                designation.setText(park.getDesignation());
                description.setText(park.getDescription());
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < park.getActivities().size(); i++) {
                    stringBuilder.append("-> "+park.getActivities().get(i).getName())
                            .append("\n");
                }
                activities.setText(stringBuilder);
                if (!park.getEntranceFees().isEmpty()) {
                    entranceFees.setText(String.format("Cost: $%s", park.getEntranceFees().get(0).getCost()));
                }else {
                    entranceFees.setText(R.string.info_unavailable);
                }
                StringBuilder opsString = new StringBuilder();
                opsString.append("-> Sunday        : ").append(park.getOperatingHours().get(0).getStandardHours().getSunday()).append("\n")
                         .append("-> Monday       : ").append(park.getOperatingHours().get(0).getStandardHours().getMonday()).append("\n")
                         .append("-> Tuesday       : ").append(park.getOperatingHours().get(0).getStandardHours().getTuesday()).append("\n")
                         .append("-> Wednesday : ").append(park.getOperatingHours().get(0).getStandardHours().getWednesday()).append("\n")
                         .append("-> Thursday     : ").append(park.getOperatingHours().get(0).getStandardHours().getThursday()).append("\n")
                         .append("-> Friday           : ").append(park.getOperatingHours().get(0).getStandardHours().getFriday()).append("\n")
                         .append("-> Saturday      : ").append(park.getOperatingHours().get(0).getStandardHours().getSaturday());

                opHours.setText(opsString);

                StringBuilder topicBuilder = new StringBuilder();
                for (int i = 0; i < park.getTopics().size() ; i++) {
                    topicBuilder.append("-> "+park.getTopics().get(i).getName()).append("\n");
                }
                detailsTopics.setText(topicBuilder);

                if (!TextUtils.isEmpty(park.getDirectionsInfo())) {
                    directions.setText(park.getDirectionsInfo());
                }else {
                    directions.setText(R.string.no_directions);
                }

                viewPagerAdapter= new ViewPagerAdapter(park.getImages());
                viewPager.setAdapter(viewPagerAdapter);

                setupDots(viewPagerAdapter.getCount());

                viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        updateDots(position);
                    }
                });

        }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }
    private void setupDots(int count) {
        dotsCount = count;
        for (int i = 0; i < count; i++) {
            View dot = new View(DetailsFragment.this.getContext());
            dot.setBackgroundResource(R.drawable.dot_inactive);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    new LinearLayout.LayoutParams(20, 20));
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsLayout.addView(dot);
        }
        updateDots(0); // Highlight the first dot
    }
    private void updateDots(int position) {
        for (int i = 0; i < dotsCount; i++) {
            View dot = dotsLayout.getChildAt(i);
            dot.setBackgroundResource(i == position ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }
}
package com.example.basaheroapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.basaheroapp.Utilities.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class UserReadList extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_read_list, container, false);

        TabLayout tabLayout = view.findViewById(R.id.user_tabs);
        ViewPager viewPager = view.findViewById(R.id.user_frame);
        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter vpAdapter = new ViewPageAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new WantToRead(), "Want to Read");
        vpAdapter.addFragment(new CurrentlyReading(), "Reading");
        vpAdapter.addFragment(new ReadComplete(), "Completed");
        viewPager.setAdapter(vpAdapter);

        View tabView = tabLayout.getTabAt(0).view;  // Get the View for the tab
        tabView.setBackgroundResource(R.drawable.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Update TextView when tab is selected
                // Update the TextView with tab name
                tab.view.setBackgroundResource(R.drawable.tabs);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: You can handle unselected tabs if needed
                tab.view.setBackgroundResource(R.drawable.tabs_selected);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle tab reselection if needed
            }
        });

        return view;
    }
}
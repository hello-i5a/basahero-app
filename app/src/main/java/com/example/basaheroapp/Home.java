package com.example.basaheroapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.basaheroapp.Utilities.Database;
import com.example.basaheroapp.Utilities.Item;
import com.example.basaheroapp.Utilities.RecyclerViewAdapter;
import com.example.basaheroapp.Utilities.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class Home extends Fragment {

    private String accKey;
    private TextView intro;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        intro = view.findViewById(R.id.intro);

        intro.setText("Hi, " + ((MainActivity)getActivity()).accountDetails.getName());

        RecyclerView new_rv = view.findViewById(R.id.new_recyclerView);
        new_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter new_adapter = new RecyclerViewAdapter(testItem());
        new_rv.setAdapter(new_adapter);
        SnapHelper new_snapHelper = new PagerSnapHelper();
        new_snapHelper.attachToRecyclerView(new_rv);

        RecyclerView top_rv = view.findViewById(R.id.top_recyclerView);
        top_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter top_adapter = new RecyclerViewAdapter(testItem());
        top_rv.setAdapter(top_adapter);
        SnapHelper top_snapHelper = new PagerSnapHelper();
        top_snapHelper.attachToRecyclerView(top_rv);

        RecyclerView reco_rv = view.findViewById(R.id.reco_recyclerView);
        reco_rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewAdapter reco_adapter = new RecyclerViewAdapter(testItem());
        reco_rv.setAdapter(reco_adapter);
        SnapHelper reco_snapHelper = new PagerSnapHelper();
        reco_snapHelper.attachToRecyclerView(reco_rv);

        TabLayout tabLayout = view.findViewById(R.id.home_tabs);
        ViewPager viewPager = view.findViewById(R.id.home_frame);
        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter vpAdapter = new ViewPageAdapter(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new NewArrival(), "New Arrival");
        vpAdapter.addFragment(new NewArrival(), "Top Rated");
        vpAdapter.addFragment(new NewArrival(), "Recommended");
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

    private ArrayList<Item> testItem() {
        ArrayList<Item> items = new ArrayList<>();

        String input = ((MainActivity)getActivity()).getBooks.getNewBooks();
        int startIndex = input.indexOf("[");
        int endIndex = input.indexOf("]") + 1;

        String booksData = input.substring(startIndex, endIndex);

        try {
            // Convert the input to a JSONArray
            JSONArray jsonArray = new JSONArray(booksData);

            int len = (jsonArray.length() >= 3 ) ? 3 : jsonArray.length();

            // Loop through the array and assign values to the ArrayLists
            for (int i = 0; i < len; i++) {
                JSONObject book = jsonArray.getJSONObject(i);
                items.add(new Item(book.getInt("book_id"), book.getString("title"), book.getString("author"), book.getString("pub_date"), book.getString("description"), book.getString("img_name")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
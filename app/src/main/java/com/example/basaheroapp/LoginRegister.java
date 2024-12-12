package com.example.basaheroapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.basaheroapp.Utilities.ViewPageAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class LoginRegister extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        TextView text = findViewById(R.id.bannerText);
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.frame);
        tabLayout.setupWithViewPager(viewPager);

        ViewPageAdapter vpAdapter = new ViewPageAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Login(), "Login");
        vpAdapter.addFragment(new Register(), "Register");
        viewPager.setAdapter(vpAdapter);

        text.setText("Welcome Back!");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Update TextView when tab is selected
                 // Update the TextView with tab name
                switch (tab.getPosition()) {
                    case 0:
                        text.setText("Welcome Back!");
                        break;
                    case 1:
                        text.setText("Get Started");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Optional: You can handle unselected tabs if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Optional: Handle tab reselection if needed
            }
        });


    }
}
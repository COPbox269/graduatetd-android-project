package com.codboxer.finallayouttest.ui.activity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.codboxer.finallayouttest.R;
import com.codboxer.finallayouttest.adapter.ViewPagerAdapter;
import com.codboxer.finallayouttest.ui.fragment.ControlFragment;
import com.codboxer.finallayouttest.ui.fragment.ParameterFragment;
import com.codboxer.finallayouttest.ui.fragment.TimerFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;    // select androidx
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ControlFragment controlFragment;
    private TimerFragment timerFragment;
    private ParameterFragment parameterFragment;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        controlFragment = new ControlFragment();
        timerFragment = new TimerFragment();
        parameterFragment = new ParameterFragment();

        // Associate TabLayout to ViewPager
        tabLayout.setupWithViewPager(viewPager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

        // Add Fragment to ViewPager, disable tittle by passing null string
        viewPagerAdapter.addFragment(controlFragment, "");
        viewPagerAdapter.addFragment(timerFragment, "");
        viewPagerAdapter.addFragment(parameterFragment, "");

        // Set a PagerAdapter that will supply views for this pager as needed.
        viewPager.setAdapter(viewPagerAdapter);

        // Add dynamic icon
        addIconTablayout(tabLayout);

        // Set icon first color
        setIconColor(tabLayout.getTabAt(0), "#0000FF");
        setIconColor(tabLayout.getTabAt(1), "#89CFF0");
        setIconColor(tabLayout.getTabAt(2), "#89CFF0");

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setIconColor(tab, "#0000FF");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setIconColor(tab, "#89CFF0");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    public void addIconTablayout(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_touch_app_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_access_time_filled_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_info_24);
    }

    public void setIconColor(TabLayout.Tab tab, String color_hex_code) {
        tab.getIcon().setColorFilter(Color.parseColor(color_hex_code), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
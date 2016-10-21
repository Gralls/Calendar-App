package com.springer.patryk.tas_android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.springer.patryk.tas_android.R;
import com.springer.patryk.tas_android.fragments.LoginFragment;
import com.springer.patryk.tas_android.fragments.RegisterFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HelloActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 2;
    @BindView(R.id.hello_pager)
    ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mPagerAdapter = new HelloPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    private class HelloPagerAdapter extends FragmentStatePagerAdapter {

        public HelloPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new RegisterFragment();
                    break;
                case 1:
                    fragment = new LoginFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }


}

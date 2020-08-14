package com.edgar.yurihome;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.edgar.yurihome.fragments.MainListFragment;
import com.edgar.yurihome.utils.SharedPreferenceUtil;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] classifyFilters = SharedPreferenceUtil.getClassifyFilters(MainActivity.this);

        MainListFragment mainListFragment = MainListFragment.newInstance(classifyFilters);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frag_container, mainListFragment);
        transaction.commit();

    }
}
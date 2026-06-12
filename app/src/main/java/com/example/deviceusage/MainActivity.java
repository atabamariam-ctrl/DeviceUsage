package com.example.deviceusage;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private Stack<Fragment> fragmentStack = new Stack<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setVisibility(View.GONE);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if (item.getItemId() == R.id.nav_home) {
                fragment = new DeviceListMapFragment();
            } else if (item.getItemId() == R.id.nav_add) {
                fragment = new AddDeviceFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                fragment = new SearchFragment();
            } else if (item.getItemId() == R.id.nav_favorite) {
                fragment = new FavoriteFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.framelayot, fragment)
                        .commit();
            }

            return true;
        });

        if (savedInstanceState == null) {
            gotoLoginFragment();
        }
    }

    private void gotoLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.framelayot, new LoginFragment());
        ft.commit();
    }

    public void pushFragment(Fragment fragment) {
        fragmentStack.push(fragment);
    }
}
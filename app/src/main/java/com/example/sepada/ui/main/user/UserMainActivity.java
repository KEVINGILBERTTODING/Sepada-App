package com.example.sepada.ui.main.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sepada.R;
import com.example.sepada.ui.main.user.home.UserHomeFragment;
import com.example.sepada.ui.main.user.profile.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class UserMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        bottomNavigationView = findViewById(R.id.bottom_bar);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new UserHomeFragment());
                    return true;
                }else  if (item.getItemId() == R.id.menuProfile) {
                    replace(new UserProfileFragment());
                    return true;
                }
                return false;
            }
        });
        replace(new UserHomeFragment());
    }

    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameUser, fragment)
                .commit();
    }
}
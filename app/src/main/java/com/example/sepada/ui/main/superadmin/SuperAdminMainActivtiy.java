package com.example.sepada.ui.main.superadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.sepada.R;
import com.example.sepada.ui.main.admin.home.AdminHomeFragment;
import com.example.sepada.ui.main.admin.profile.AdminProfileFragment;
import com.example.sepada.ui.main.admin.users.UsersFragment;
import com.example.sepada.ui.main.superadmin.home.SuperAdminHomeFragment;
import com.example.sepada.ui.main.superadmin.profile.SuperAdminProfileFragment;
import com.example.sepada.ui.main.superadmin.tamu.TamuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SuperAdminMainActivtiy extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_main_activtiy);
        bottomNavigationView = findViewById(R.id.bottom_bar);

        listener();

        replace(new SuperAdminHomeFragment());
    }

    private void listener() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuHome) {
                    replace(new SuperAdminHomeFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuTamu) {
                    replace(new TamuFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuUser) {
                    replace(new UsersFragment());
                    return true;
                } else if (item.getItemId() == R.id.menuProfile) {
                    replace(new SuperAdminProfileFragment());
                    return true;
                }
                return false;
            }
        });
    }


    private void replace(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameSuperAdmin, fragment)
                .commit();
    }
}
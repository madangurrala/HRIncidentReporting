package com.example.hrincidentreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout draw_layout;
    Toolbar toolbar;
    NavigationView navBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navBar = (NavigationView) findViewById(R.id.nav_view);
        setSupportActionBar(toolbar);

        draw_layout = findViewById(R.id.drawer_layout);

        navBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                        case R.id.report_id:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                               new ReportIncident() ).commit();
                        break;

                        case R.id.view_id:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ViewIncidents() ).commit();
                        break;
                }

                draw_layout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //To have a menu icon
        ActionBarDrawerToggle actionBar = new ActionBarDrawerToggle(this, draw_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        draw_layout.addDrawerListener(actionBar);
        actionBar.syncState();
    }

    //When back button is pressed dwhen Nav bar is open, we don't want user to exit form the app
    @Override
    public void onBackPressed() {

        if(draw_layout.isDrawerOpen(GravityCompat.START)){
            draw_layout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }
}

package com.example.hrincidentreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.ClipData;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    //Instantiating UI controls used in layout file
    private DrawerLayout draw_layout;
    Toolbar toolbar;
    NavigationView navBar;

    //The main method which is called as soon as the App is launched
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataBaseHandler myDb = new DataBaseHandler(this);
        //Associating UI control objects with referencing to their Ids in layout.xml file
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navBar = (NavigationView) findViewById(R.id.nav_view);

        //passing the toolbar to act as the action bar  for the main activity
        setSupportActionBar(toolbar);

        draw_layout = findViewById(R.id.drawer_layout);

        //This method keep listening to the nav bar item selection
        navBar.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //If any item on nav bar is selected it brings the fragment manager to call the respective fragment layout
                switch(item.getItemId()){
                        /*When report incident is selected on nav bar, it replaces the fragment layout of main activity with
                    report incident fragment view*/
                        case R.id.report_id:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                               new ReportIncident() ).commit();
                        break;
                    //When report incident is selected on nav bar, it replaces the fragment layout of main activity with fragment view
                        case R.id.view_id:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new ViewIncidents() ).commit();
                        break;
                }

                //It closes the navigation drawer upon selecting item
                draw_layout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //ActionBarDrawerToggle is created to sync with the state of toolbar
        ActionBarDrawerToggle actionBar = new ActionBarDrawerToggle(this, draw_layout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //this keep listenes to the layout to see if nav bar is open or close
        draw_layout.addDrawerListener(actionBar);
        //This methods syncs the state
        actionBar.syncState();

    }

    //When back button is pressed when Nav bar is open, we don't want user to exit form the app
    @Override
    public void onBackPressed() {

        if(draw_layout.isDrawerOpen(GravityCompat.START)){
            draw_layout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }


    }
}

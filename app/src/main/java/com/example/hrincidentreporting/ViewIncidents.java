package com.example.hrincidentreporting;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewIncidents extends Fragment {
    TextView display;
    View view;
    DataBaseHandler myDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment with the xml layout and stored in view object
        view =  inflater.inflate(R.layout.fragmetnt_view_incidents, container, false);
        display = (TextView) view.findViewById(R.id.displayIncidentsId);

        myDb = new DataBaseHandler(getContext());
        // Inflate the layout for this fragment
        viewIncidents();

        //returning the view object to Main activity
        return view;
    }

    public void viewIncidents(){

        //retrieving all the stored incidents
        Cursor cursor = myDb.viewRecords();
        //Displaying all the records in text view
        while (cursor.moveToNext()){
            display.append("Incident Id:" + cursor.getString(0) +"\n");
            display.append("Title:" + cursor.getString(1)+"\n");
            display.append("Incident Date:"+cursor.getString(2)+"\n");
            display.append("Employee Number:"+cursor.getString(3)+"\n");
            display.append("Employee Name:"+cursor.getString(4)+"\n");
            display.append("Gender:"+cursor.getString(5)+"\n");
            display.append("Shift:"+cursor.getString(6)+"\n");
            display.append("Department:"+cursor.getString(7)+"\n");
            display.append("Position:"+cursor.getString(8)+"\n");
            display.append("Incident Type:"+cursor.getString(9)+"\n");
            display.append("Injured Body Part:"+cursor.getString(10)+"\n");
            display.append("----------------------------------"+"\n");

        }
        cursor.close();
    }

}

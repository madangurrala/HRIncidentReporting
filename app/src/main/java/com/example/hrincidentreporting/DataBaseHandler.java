package com.example.hrincidentreporting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String databaseName = "HrRecords.db";
    public static final String tableName = "tbl_IncidentHistory";
    public static final String title = "Title";
    public static final String incidentId = "ID";
    public static final String incidentDate = "IncidentDate";
    public static final String empNumber = "EmployeeNumber";
    public static final String empName = "EmployeeName";
    public static final String gender = "Gender";
    public static final String shift = "Shift";
    public static final String department = "Department";
    public static final String position = "Position";
    public static final String incidentType = "IncidentType";
    public static final String bodyPart = "InjuredBodyPart";

    public  DataBaseHandler(Context context){

        super(context, databaseName, null, 1 );
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table if not exists " + tableName +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title TEXT, IncidentDate NUMERIC, EmployeeNumber TEXT, EmployeeName TEXT, Gender TEXT, Shift TEXT, Department TEXT,Position TEXT, IncidentType TEXT, InjuredBodyPart TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(sqLiteDatabase);

    }
}

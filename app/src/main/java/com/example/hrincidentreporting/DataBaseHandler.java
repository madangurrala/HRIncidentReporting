package com.example.hrincidentreporting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String databaseName = "HrRecords.db";
    public static final String tableName = "tbl_IncidentHistory";
    public static final String employeeTable = "tbl_Employee";
    public static final String bodyPartsTable = "tbl_BodyParts";
    public  static final String employeeId = "_id";
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

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + tableName +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title TEXT, IncidentDate NUMERIC, EmployeeNumber TEXT, EmployeeName TEXT, Gender TEXT, Shift TEXT, Department TEXT,Position TEXT, IncidentType TEXT, InjuredBodyPart TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tableName);
        onCreate(sqLiteDatabase);

    }

    //Method to insert records into database which takes FirstName, LastName and Marks
    public boolean insertRecord(String Title, String IncidentDate, String EmployeeNumber,
                                String EmployeeName, String Gender, String Shift, String Department,
                                String Position, String IncidentType, String InjuredBodyPart  ){
        //Instantiating SQLlitedatabase to get Writable database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //Instantiating Content values to add parameters passed with method to use in the query
        ContentValues cValues = new ContentValues();
        cValues.put(title, Title );
        cValues.put(incidentDate,IncidentDate );
        cValues.put(empNumber, EmployeeNumber);
        cValues.put(empName, EmployeeName);
        cValues.put(gender, Gender);
        cValues.put(shift, Shift);
        cValues.put(department, Department);
        cValues.put(position, Position);
        cValues.put(incidentType, IncidentType);
        cValues.put(bodyPart, InjuredBodyPart);


        //storing the boolean return of insert command in response variable
        long response =  sqLiteDatabase.insert(tableName,null, cValues);

        //using response variable to return either true or false
        if(response == -1){
            return  false;
        }else{
            return true;
        }
    }

    //Method to view records (Using Cursor type since raw query returns Cursor)
    public Cursor viewRecords(){
        //Instantiating SQLiteDatabase to get the writable Database
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor response = sqLiteDatabase.rawQuery("select * from "+tableName, null);
        return  response;

    }

    //Method to view one record which returns cursor
    public Cursor viewEmployeeRecord(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //SQl query to retrieve a record based on the given ID(taken through parameter)
        Cursor response = sqLiteDatabase.rawQuery("SELECT * FROM " + employeeTable +" WHERE "+ employeeId + " = " + id,null, null);
        return  response;
    }

    public Cursor selectBodyParts(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //SQl query to retrieve a record based on the given ID(taken through parameter)
        Cursor response = sqLiteDatabase.rawQuery("SELECT * FROM " + bodyPartsTable, null, null);
        return  response;
    }

}

package com.example.hrincidentreporting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/*This is a child class of SQLiteOpenHelper class which has all the methods to create database,
tables and records*/
public class DataBaseHandler extends SQLiteOpenHelper {

    //Declaring variables for DatabaseName, tables and Columns
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

    //Constructor to create database
    public  DataBaseHandler(Context context){

        super(context, databaseName, null, 1 );

    }

    /*A call back function which is invoked on Upgrade .
    This function has SQLite query to create table*/
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + tableName +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Title TEXT, IncidentDate NUMERIC, EmployeeNumber TEXT, EmployeeName TEXT, Gender TEXT, Shift TEXT, Department TEXT,Position TEXT, IncidentType TEXT, InjuredBodyPart TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + employeeTable +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Department TEXT, Position TEXT);");

        sqLiteDatabase.execSQL("CREATE TABLE " + bodyPartsTable + " ( Body_Part TEXT,_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);");

        insertIntoTables(sqLiteDatabase);

    }

    //This method inserts the values into Employee and Body Parts tables

    public  void insertIntoTables(SQLiteDatabase sqLiteDatabase){

        String query1 = " INSERT INTO " + employeeTable + "( Name, Department, Position )" +
                " VALUES ( 'Madan', 'IT', 'Student')," +
                " ( 'Bhavik', 'IT', 'Student')," +
                "( 'Thomas', 'IT', 'Professor')";

        sqLiteDatabase.execSQL(query1);

       String queryInsert = " INSERT INTO " + bodyPartsTable + "( Body_Part ) " +
                " VALUES ( 'Ankle-right' ), " +
                "('Ankle-right')," +
                "('Arm-Both')," +
                "('Arm-Left Upper')," +
                "('Arm-Right Upper')," +
                "('Back-All')," +
                "('Back-Lower')," +
                "('Back-Middle')," +
                "('Back-Upper')," +
                "('Chest')," +
                "('Ear-Both')," +
                "('Ear-left')," +
                "('Ear-Right')," +
                "('Ears-Both')," +
                "('Elbow-Left')," +
                "('Elbow-Right')," +
                "('Eye-both')," +
                "('Eye-left')," +
                "('Eye-right')," +
                "('Face')," +
                "('Feet-Both')," +
                "('Foot-left')," +
                "('Foot-right')," +
                "('Forearm-left')," +
                "('Forearm-right')," +
                "('Hand-left')," +
                "('Hand-Palm-Left')," +
                "('Hand-Palm-right')," +
                "('Hand-right')," +
                "('Hands-Both')," +
                "('Head-Rear')," +
                "('Head-Front')," +
                "('Head-Left')," +
                "('Head-Right')," +
                "('Hip-Left')," +
                "('Hip-Right')," +
                "('Index Finger-left')," +
                "('Index Finger-right')," +
                "('Knee-left')," +
                "('Knee-right')," +
                "('Leg-Both')," +
                "('Leg-Left Lower')," +
                "('Leg-Left Upper')," +
                "('Leg-Right Lower')," +
                "('Leg-Right Upper')," +
                "('Middle Finger-left')," +
                "('Middle Finger-right')," +
                "('Mouth')," +
                "('Neck')," +
                "('Nose')," +
                "('Pinky Finger-left')," +
                "('Pinky Finger-right')," +
                "('Ring Finger-left')," +
                "('Ring Finger-right')," +
                "('Shoulder-Right')," +
                "('Shoulder-Left')," +
                "('Thumb-left')," +
                "('Thumb-right')," +
                "('Wrist-Left')," +
                "('Wrist-Right')," +
                "('Other-See Notes')," +
                "('Groin')," +
                "('Abdomen')," +
                "('Multiple-See Notes')," +
                "('N/A'),"+
                "('Internal')";

        sqLiteDatabase.execSQL(queryInsert);
    }

    /*A call back function which is called when database file exists but the version number is lower than version number
    mentioned in constructor. This has SQL query which deletes the tables if exists*/
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ tableName);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ employeeTable);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ bodyPartsTable);
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

    //Method to get employee data from database based on the employee id
    public Cursor viewEmployeeRecord(String id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //SQl query to retrieve a record based on the given ID(taken through parameter)
        Cursor response = sqLiteDatabase.rawQuery("SELECT * FROM " + employeeTable +" WHERE "+ employeeId + " = " + id,null, null);
        return  response;
    }

    //Method to query body parts in the tbl_BodyParts
    public Cursor selectBodyParts(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //SQl query to retrieve a record based on the given ID(taken through parameter)
        Cursor response = sqLiteDatabase.rawQuery("SELECT * FROM " + bodyPartsTable, null, null);
        return  response;
    }

}

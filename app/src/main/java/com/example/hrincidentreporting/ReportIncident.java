package com.example.hrincidentreporting;
import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

import static android.app.Activity.RESULT_OK;

public class ReportIncident extends Fragment {

    TextView labelTitle, labelIncident, labelEmpId, labelEmpName, labelDate,
            labelGender, labelShift, labelDept, labelPosition,
            labelIncType, labelInjPart;
    EditText title, incidentId, empId, empName, date, department, position;
    RadioGroup genderGroup;
    RadioButton male, female;
    private Spinner shift, incidentType, injBodyPart;
    private Button submitButton;
    private String message;
    String bdyParts = " ";
    Uri image_uri;

     private String[] shiftArray, incidentArray, bodyParts;
     private int[] spinnerBParts;
    DataBaseHandler myDb;
    View view;
    String genderStr, shiftStr, incidentStr, injuredBdyStr, titleStr, incidentDateStr, empNumberStr,
            dateStr, empNameStr,departmentStr, positionStr;
    private static final int CAM_REQUEST = 101;
    private static final int REQUEST_CODE = 1001;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment with the xml layout and stored in view object
       view = inflater.inflate(R.layout.fragment_report_incident, container, false);

       //Instantiated database handler class
        myDb = new DataBaseHandler(getContext());

        //Associating UI controls with the ids
        labelTitle = (TextView) view.findViewById(R.id.titleLabel);
        labelIncident = (TextView) view.findViewById(R.id.labelIncidentId);
        labelEmpId = (TextView) view.findViewById(R.id.labelEmpId);
        labelEmpName = (TextView) view.findViewById(R.id.labelEmpName);
        labelDate = (TextView) view.findViewById(R.id.labelDateId);
        labelGender = (TextView) view.findViewById(R.id.labelGenderId);
        labelShift = (TextView) view.findViewById(R.id.labelShiftId);
        labelDept = (TextView) view.findViewById(R.id.labelDepartmentId);
        labelPosition = (TextView) view.findViewById(R.id.labelPositionId);
        labelIncType = (TextView)view.findViewById(R.id.labelIncTypeId);
        labelInjPart = (TextView) view.findViewById(R.id.labelInjuredPartId);

        title = (EditText) view.findViewById(R.id.titleId);
        incidentId = (EditText) view.findViewById(R.id.incidentId);
        empId = (EditText) view.findViewById(R.id.empId);
        empName = (EditText) view.findViewById(R.id.empName);
        date = (EditText) view.findViewById(R.id.dateId);
        department = (EditText) view.findViewById(R.id.departmentId);
        position = (EditText) view.findViewById(R.id.positionId);

        genderGroup = (RadioGroup) view.findViewById(R.id.genderGroup);
        male = (RadioButton) view.findViewById(R.id.maleId);
        female = (RadioButton) view.findViewById(R.id.femaleId);

        shift = (Spinner) view.findViewById(R.id.shiftId);
        incidentType = (Spinner) view.findViewById(R.id.incTypeId);
        injBodyPart = (Spinner) view.findViewById(R.id.injuredPartId);

        submitButton = (Button) view.findViewById(R.id.buttonId);

        //Creating date format which uses Java data and locale utilities to get the today's date
        String currentDate = new SimpleDateFormat("MM/dd/YYYY", Locale.getDefault()).format(new Date());

        //setting the date field with the current date
        date.setText(currentDate);

        //Arrays to populate shift and Incident type list views
        shiftArray = new String[]{"Shift A", "Shift B", "Shift C"};
        incidentArray = new String[]{"Near Miss", "First Aid", "Medical Aid"};

        //Passing string array values to array adapater to pass it to the spinner
        ArrayAdapter<String> adptShift = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, shiftArray);
        shift.setAdapter(adptShift);

        ArrayAdapter<String> adptIncident = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, incidentArray);
        incidentType.setAdapter(adptIncident);

        //This method is created to get the selected shift value which keep listening to the shift item selected
        shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                //passing the selected shift position to string variable
                shiftStr = shiftArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //This method is created to populate the employee details upon employee ID entered into the text field
        empId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //After entering the id calling the method which retrieves and populate details

                getEmployeeDetails();

            }
        });

        //This method is created to get the selected incident type value which keep listening to the spinner item selected
        incidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //passing the selected incident array position to string variable
                incidentStr = incidentArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //These two arrays are the from and to arguments in Simple course adapater

        //The following code retrieves data from database and sets it to spinner

        final String bodyParts[] = new String[] {"Body_Part"};
        final int spinnerBParts[] = new int[] {android.R.id.text1};

        //Calling method in the database handler class to get the body parts from body parts table
        final Cursor cursor = myDb.selectBodyParts();

        /*Passing the cursor values to simple course adapter which sets the cursor values named
        by column name in bodyParts string array and position by the id*/
        final SimpleCursorAdapter adapterBodyParts = new SimpleCursorAdapter(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, cursor, bodyParts, spinnerBParts);

        adapterBodyParts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        injBodyPart.setAdapter(adapterBodyParts);

        //This method is created to get the selected injured body part value which keep listening to the spinner item selected
        injBodyPart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //moving the cursor position to selected item
                cursor.moveToPosition(i);
                //passing the selected value to string variable
                injuredBdyStr =  cursor.getString(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        reportIncident();

        //Returing this View object to Main activity
        return view;
    }

    //This method validates the employee ID entered
    public boolean validateId(){
        /*This block of code checks if something is entered in ID field and then try to parses to int,
        if not parsable sets an error to ID filed.*/
        try{
            if(empId.getText().length() == 0){
                empId.setError("Enter Employee ID");
                return false;
            }else {
                empId.setError(null);
                Integer.parseInt(empId.getText().toString());
                return true;
            }
        }catch (NumberFormatException e){
            empId.setError("Enter correct Employee ID");
            return false;
        }
    }

  /*  Method to validate the gender selection and set the error if not selected which
    return boolean to the validateAllFields method*/
    public boolean genderSelection(){
        if(male.isChecked()){
            genderStr = male.getText().toString();
            labelGender.setError(null);
            return true;
        }
        else if(female.isChecked()){
            genderStr = female.getText().toString();
            labelGender.setError(null);
            return true;
        }else{
            labelGender.setError("Please select Gender");
            return false;
        }
    }

    //This method checks if any of the given 3 mothods returned false then returns false else true
    public boolean validateAllFields(){

        boolean[] methods = new boolean[3];
        methods[0] = validateId();
        methods[1] = genderSelection();
        methods[2] = getEmployeeDetails();

        for(int i=0; i<methods.length; i++){
            if(methods[i] == false){
                return false;
            }
        }
        return true;
    }

    //This method is invoked onCreate which listens to report incident button
    public void reportIncident(){

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleStr = title.getText().toString();
                incidentDateStr = date.getText().toString();
                empNumberStr = empId.getText().toString();
                empNameStr = empName.getText().toString();
                dateStr = date.getText().toString();
                departmentStr = department.getText().toString();
                positionStr = position.getText().toString();

                //If validateAllFileds method return true then it creates record in database, takes picture and sends an email
                if(validateAllFields()) {

                    //Calling insert record method in the database handler class to create record by passing all the fileds
                    boolean createdRecord =myDb.insertRecord(titleStr,dateStr,empNumberStr,
                            empNameStr, genderStr, shiftStr,departmentStr,positionStr,incidentStr,
                            injuredBdyStr );

                    //If record is created then check the sdk version of the devices to get the camera and storage permission to store picture
                    if(createdRecord){
                        //If sdk is >= 25 it gets into if loop
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                            //If the camera and storage permission is denied then the app requests for the permision if not opens camera
                            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) ==
                                    PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                    PackageManager.PERMISSION_DENIED ){

                                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE };
                                requestPermissions(permission, REQUEST_CODE);
                            }
                            else{

                                openCamera();

                            }
                        }else{
                            //If sdk is less than < 25 then the open Camera method is invoked
                            openCamera();

                        }
                        Toast.makeText(getActivity(), "Inserted Data has been added to database",
                                Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(getActivity(), "Unfortunately the data could not be entered",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    //If validate all fields returns false then toast message displayed and submit button does nothing
                    Toast.makeText(getActivity(), "Please enter all the required fields",
                            Toast.LENGTH_LONG ).show();
                    return;
                }
            }

        });
    }

    //This method opens camera, parses the picture and stores in external storage
    private void openCamera(){

        //content values to pass the title of the picture
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        //image_uri is an object of URi which parses the image and stores in external storage
        image_uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //creating camera intent
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        //Opening the camera which takes camera intent and the request code initiated
        startActivityForResult(camera, CAM_REQUEST);

    }

    //This method is invoked to the result of the app permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        switch (requestCode){
            //If camera and storage permission is granted then calling the camera method otherwise toasting the message that permission denied
            case REQUEST_CODE:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    openCamera();

                }else{
                    Toast.makeText(getActivity(), " Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    //This method is invoked on Camera activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //If result code is RESULT_OK which is -1 then retrieving last stored data in database and sending an email
        if(resultCode == RESULT_OK ){

            Cursor cursor = myDb.viewRecords();

                cursor.moveToLast();
                message += "Title:" + cursor.getString(1)+"\n";
                message = "Incident Id:" + cursor.getString(0) +"\n";
                message += "Incident Date:"+cursor.getString(2)+"\n";
                message += "Employee Number:"+cursor.getString(3)+"\n";
                message += "Employee Name:"+cursor.getString(4)+"\n";
                message += "Gender:"+cursor.getString(5)+"\n";
                message += "Shift:"+cursor.getString(6)+"\n";
                message += "Department:"+cursor.getString(7)+"\n";
                message += "Position:"+cursor.getString(8)+"\n";
                message += "Incident Type:"+cursor.getString(9)+"\n";
                message += "Injured Body Part:"+cursor.getString(10)+"\n\n\n";
                message += "Regards\nMadan Gurrala\nBhavik Patel";

            cursor.close();

            //String array which has the recipient email address
            String[] recipient = {"madangurrala@gmail.com, bhavikpatel7023@gmail.com "};

            //Creating email intent
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            //Passing the values to passed with email intent
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient );
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HR Incident Reporting" );
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            //Setting the email type
            emailIntent.setType("application/image");
            //Attaching the image which is stored in Uri variable
            emailIntent.putExtra(Intent.EXTRA_STREAM, image_uri);

            try{
                //Starting email activity
                startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
            }catch (Exception e){
                Toast.makeText(getActivity(), "Oops!! There was a problem in sending email",
                        Toast.LENGTH_LONG).show();
            }

        }else{

            Toast.makeText(getActivity(), "Unfortunately Email could not be sent",
                    Toast.LENGTH_LONG).show();

        }
    }

    //This method gets the employee details from database and sets to the text fileds
    public boolean getEmployeeDetails(){

        if(validateId()){
            Cursor response =  myDb.viewEmployeeRecord(empId.getText().toString().trim());

            //If entered employee id is not present in database then displaying alert using alert builder
            if(response.getCount() == 0){

                showData("Error", "Entered Employee ID doesn't exist");
                return false;
            }else{
                while (response.moveToNext()){
                    empName.setText(response.getString(1));
                    department.setText(response.getString(2));
                    position.setText(response.getString(3));
                }
                response.close();
                return true;
            }
        }else{

            return false;
        }
    }

    /*This method takes two string parameters and passes it to AlertDialog builder to display the data passed to buffer
      everytime when this function is called*/
    public void showData(String title, String message){
        //Instantiating AlertDialog builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        //Setting the dialog builder can be cancellable
        alertBuilder.setCancelable(true);
        //Passing taken parameters to setTitle & setMessage
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        alertBuilder.show();
    }

}

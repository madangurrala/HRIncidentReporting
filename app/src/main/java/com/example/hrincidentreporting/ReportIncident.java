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
        // Inflate the layout for this fragment
       view = inflater.inflate(R.layout.fragment_report_incident, container, false);

        myDb = new DataBaseHandler(getContext());

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

        String currentDate = new SimpleDateFormat("MM/dd/YYYY", Locale.getDefault()).format(new Date());

        date.setText(currentDate);

        shiftArray = new String[]{"Shift A", "Shift B", "Shift C"};
        incidentArray = new String[]{"Near Miss", "First Aid", "Medical Aid"};

        ArrayAdapter<String> adptShift = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, shiftArray);
        shift.setAdapter(adptShift);

        ArrayAdapter<String> adptIncident = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, incidentArray);
        incidentType.setAdapter(adptIncident);

        shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                shiftStr = shiftArray[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        empId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                getEmployeeDetails();

            }
        });

        incidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                incidentStr = incidentArray[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final String bodyParts[] = new String[] {"Body_Part"};
        final int spinnerBParts[] = new int[] {android.R.id.text1};

        Cursor cursor = myDb.selectBodyParts();

        final SimpleCursorAdapter adapterBodyParts = new SimpleCursorAdapter(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, cursor, bodyParts, spinnerBParts);

        adapterBodyParts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        injBodyPart.setAdapter(adapterBodyParts);

        injBodyPart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Issue here;


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        reportIncident();
        return view;
    }

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

                if(validateAllFields()) {

                    boolean createdRecord =myDb.insertRecord(titleStr,dateStr,empNumberStr,
                            empNameStr, genderStr, shiftStr,departmentStr,positionStr,incidentStr,
                            injuredBdyStr );

                    if(createdRecord){
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

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
                            openCamera();

                        }
                        Toast.makeText(getActivity(), "Inserted Data has been added to database",
                                Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(getActivity(), "Unfortunately the data could not be entered",
                                Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getActivity(), "Please enter all the required fields",
                            Toast.LENGTH_LONG ).show();
                    return;
                }
            }

        });
    }

    private void openCamera(){

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        image_uri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(camera, CAM_REQUEST);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    openCamera();

                }else{
                    Toast.makeText(getActivity(), " Permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }

    }


    public void getLastSavedData(){
        final Cursor cursor = myDb.viewRecords();

        while (cursor.moveToLast()){
            message = "Incident Id:" + cursor.getString(0) +"\n";
            message += "Title:" + cursor.getString(1)+"\n";
            message += "Incident Date:"+cursor.getString(2)+"\n";
            message += "Employee Number:"+cursor.getString(3)+"\n";
            message += "Employee Name:"+cursor.getString(4)+"\n";
            message += "Gender:"+cursor.getString(5)+"\n";
            message += "Shift:"+cursor.getString(6)+"\n";
            message += "Department:"+cursor.getString(7)+"\n";
            message += "Position:"+cursor.getString(8)+"\n";
            message += "Incident Type:"+cursor.getString(9)+"\n";
            message += "Injured Body Part:"+cursor.getString(10)+"\n";
        }
        cursor.close();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK ){

            Cursor cursor = myDb.viewRecords();

                cursor.moveToLast();
                message = "Incident Id:" + cursor.getString(0) +"\n";
                message += "Title:" + cursor.getString(1)+"\n";
                message += "Incident Date:"+cursor.getString(2)+"\n";
                message += "Employee Number:"+cursor.getString(3)+"\n";
                message += "Employee Name:"+cursor.getString(4)+"\n";
                message += "Gender:"+cursor.getString(5)+"\n";
                message += "Shift:"+cursor.getString(6)+"\n";
                message += "Department:"+cursor.getString(7)+"\n";
                message += "Position:"+cursor.getString(8)+"\n";
                message += "Incident Type:"+cursor.getString(9)+"\n";
                message += "Injured Body Part:"+cursor.getString(10)+"\n";

            cursor.close();

            String[] recipient = {"bhavikpatel7023@gmail.com, Bpatel4667@conestogac.on.ca"};

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient );
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HR Incident Reporting" );
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            emailIntent.setType("application/image");
            emailIntent.putExtra(Intent.EXTRA_STREAM, image_uri);
            startActivity(Intent.createChooser(emailIntent, "Choose app"));
        }else{

            Toast.makeText(getActivity(), "Unfortunately Image can not be captured",
                    Toast.LENGTH_LONG).show();

        }
    }



    public boolean getEmployeeDetails(){

        if(validateId()){
            Cursor response =  myDb.viewEmployeeRecord(empId.getText().toString().trim());

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

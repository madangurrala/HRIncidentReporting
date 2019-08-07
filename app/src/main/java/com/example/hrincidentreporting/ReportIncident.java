package com.example.hrincidentreporting;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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


public class ReportIncident extends Fragment {

    TextView labelTitle, labelIncident, labelEmpId, labelEmpName, labelDate,
            labelGender, labelShift, labelDept, labelPosition,
            labelIncType, labelInjPart;
    EditText title, incidentId, empId, empName, date, department, position;
    RadioGroup genderGroup;
    RadioButton male, female;
    private Spinner shift, incidentType, InjBodyPart;
    private Button submitButton;

     private String[] shiftArray, incidentArray, bodyParts;
     private int[] spinnerBParts;
    DataBaseHandler myDb;
    View view;
    String genderStr, shiftStr, incidentStr, injuredBdyStr;


    public boolean validateId(){
        /*This block of code checks if something is entered in ID field and then try to parses to int,
        if not parsable sets an error to ID filed.*/
            if(empId.getText().length() == 0){
                empId.setError("Enter Employee ID");
                return false;
            }else {
                empId.setError(null);
                return true;
            }
    }

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
        InjBodyPart = (Spinner) view.findViewById(R.id.injuredPartId);

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

        String bodyParts[] = new String[] {"Body_Part"};
        int spinnerBParts[] = new int[] {android.R.id.text1};

        Cursor cursor = myDb.selectBodyParts();

        SimpleCursorAdapter adapterBodyParts = new SimpleCursorAdapter(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, cursor, bodyParts, spinnerBParts);

        adapterBodyParts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        InjBodyPart.setAdapter(adapterBodyParts);
        reportIncident();
        return view;
    }

    public boolean genderSelection(){
        boolean ischecked = false;
        if(male.isChecked()){
            genderStr = male.getText().toString();
            ischecked = true;
        }
        if(female.isChecked()){
            genderStr = female.getText().toString();
            ischecked = true;
        }
        if(!male.isChecked() && !female.isChecked()){

            ischecked = false;
        }

        return ischecked;
    }

    public void reportIncident(){

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleStr, incidentDateStr, empNumberStr, empNameStr, shiftStr, departmentStr,
                        positionStr, incidentTypeStr, bodyPartStr;
                titleStr = title.getText().toString();
                incidentDateStr = date.getText().toString();
                empNumberStr = empId.getText().toString();


                if(getEmployeeDetails()) {

                   /* boolean createdRecord =myDb.insertRecord()*/



                }

                Toast.makeText(getActivity(), "Hello ", Toast.LENGTH_LONG ).show();
            }
        });
    }

    public boolean getEmployeeDetails(){

        if(validateId()){
            Cursor cursor =  myDb.viewEmployeeRecord(empId.getText().toString().trim());

            if(cursor.getCount() == 0){

                showData("Error", "Entered Employee ID doesn't exist");
                return false;
            }else{
                StringBuffer buffer = new StringBuffer();
                //Passing all the Cursor records to Buffer using index
                while (cursor.moveToNext()){
                    empName.setText(cursor.getString(1));
                    department.setText(cursor.getString(2));
                    position.setText(cursor.getString(3));
                }
                cursor.close();
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

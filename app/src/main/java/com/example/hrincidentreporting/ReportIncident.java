package com.example.hrincidentreporting;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class ReportIncident extends Fragment {

    TextView labelTitle, labelIncident, labelEmpId, labelDate,
            labelGender, labelShift, labelDept, labelPosition,
            labelIncType, labelInjPart;
    EditText title, incidentId, empId, date, department, position;
    RadioGroup genderGroup;
    RadioButton male, female;
    private Spinner shift, incidentType, InjBodyPart;
    private Button submitButton;

     private String[] shiftArray, incidentArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_report_incident, container, false);

        DataBaseHandler myDb = new DataBaseHandler(getContext());

        labelTitle = (TextView) view.findViewById(R.id.titleLabel);
        labelIncident = (TextView) view.findViewById(R.id.labelIncidentId);
        labelEmpId = (TextView) view.findViewById(R.id.labelEmpId);
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

        shiftArray = new String[]{"Shift A", "Shift B", "Shift C"};
        incidentArray = new String[]{"Near Miss", "First Aid", "Medical Aid"};

        ArrayAdapter<String> adptShift = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, shiftArray);
        shift.setAdapter(adptShift);

        ArrayAdapter<String> adptIncident = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_dropdown_item, incidentArray);
        incidentType.setAdapter(adptIncident);


        reportIncident();

        return view;
    }

    public void reportIncident(){

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Hello ", Toast.LENGTH_LONG ).show();
            }
        });
    }

}

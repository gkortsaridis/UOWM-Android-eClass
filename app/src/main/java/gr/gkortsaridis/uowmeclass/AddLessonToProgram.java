package gr.gkortsaridis.uowmeclass;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddLessonToProgram extends AppCompatActivity {

    Spinner daysSpinner;
    TimePicker timePickerStart,timePickerEnd;
    EditText titleET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson_to_program);

        daysSpinner = (Spinner) findViewById(R.id.daysSpinner);
        timePickerStart = (TimePicker) findViewById(R.id.timePickerStart);
        timePickerEnd = (TimePicker) findViewById(R.id.timePickerEnd);
        titleET = (EditText) findViewById(R.id.lessonNameET);

        List<String> list = new ArrayList<String>();
        list.add("Δευτέρα");
        list.add("Τρίτη");
        list.add("Τετάρτη");
        list.add("Πέμπτη");
        list.add("Παρασκευή");
        list.add("Σάββατο");
        list.add("Κυριακή");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daysSpinner.setAdapter(dataAdapter);

    }

    public void saveLesson(View view){

            MyProgram myProgram = new MyProgram(getBaseContext());
            myProgram.addProgram(getBaseContext(),titleET.getText().toString(),timePickerStart.getCurrentHour()+":"+timePickerStart.getCurrentMinute(),timePickerEnd.getCurrentHour()+":"+timePickerEnd.getCurrentMinute(),daysSpinner.getSelectedItemPosition()+"");

            Toast.makeText(getBaseContext(),"Το πρόγραμμα προστέθηκε",Toast.LENGTH_SHORT).show();
            finish();
    }
}

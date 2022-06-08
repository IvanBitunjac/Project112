package com.example.project112;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;

public class UserData_Activity extends AppCompatActivity {

    //User data info
    private EditText firstName;
    private EditText lastName;
    private EditText citizenNumber;
    private EditText age;

    private Spinner countrySpinner;

    //User data
    SharedPreferences userData;
    SharedPreferences.Editor userDataEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        firstName = findViewById(R.id.editTextName);
        lastName = findViewById(R.id.editTextSurname);
        citizenNumber = findViewById(R.id.editTextCitizenNum);

        FillCountrySpinner();
        DatePicker();

        //userData = getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE);

        //ReadUserDataFromPreferences();

    }

    public void BtnSaveDataClicked(View view){
        userDataEditor = userData.edit();

        userDataEditor.putString("firstName", firstName.getText().toString());
        userDataEditor.putString("lastName", lastName.getText().toString());
        userDataEditor.putString("citizenNumber", citizenNumber.getText().toString());
        userDataEditor.putString("age", age.getText().toString());

        if(userDataEditor.commit()){
            Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Error saving data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void ReadUserDataFromPreferences(){
        firstName.setText(userData.getString("firstName", ""));
        lastName.setText(userData.getString("lastName", ""));
        citizenNumber.setText(userData.getString("citizenNumber", ""));
    }

    private void FillCountrySpinner()
    {
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }

        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        Spinner country_obj = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        country_obj.setAdapter(adapter);
    }

    private void DatePicker()
    {
        EditText editTextDate = findViewById(R.id.editTextAge);

        final Calendar c = Calendar.getInstance();
        int tmpYear = c.get(Calendar.YEAR);
        int tmpMonth = c.get(Calendar.MONTH);
        int tmpDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                editTextDate.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year);
            }
        }, tmpYear, tmpMonth, tmpDay);

        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());

        editTextDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    datePickerDialog.show();
                }
                return true;
            }
        });
    }
}
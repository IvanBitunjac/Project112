package com.example.project112;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserData_Activity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText citizenNumber;
    private EditText age;
    private Spinner countrySpinner;
    private EditText medications;
    private EditText allergies;
    private Spinner usersSpinner;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> countryAdapter;


    private List<User> userList = new ArrayList<>();
    private List<String> userNames = new ArrayList<>();
    private User selectedUser;

    private UserAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        FindAllViews();
        FillCountrySpinner();
        DatePicker();
        UserSpinner();

        db = new UserAdapter(this);

        GetAllUserFromDB();
        //printAllFromDB();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, userNames);

        if (!userList.isEmpty())
        {
            usersSpinner.setSelection(0);
            selectedUser = userList.get(0);
            UpdateValues(selectedUser);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            usersSpinner.setAdapter(adapter);
        }
    }

    private void FindAllViews()
    {
        firstName = findViewById(R.id.editTextName);
        lastName = findViewById(R.id.editTextSurname);
        citizenNumber = findViewById(R.id.editTextCitizenNum);
        age = findViewById(R.id.editTextAge);
        countrySpinner = findViewById(R.id.spinner2);
        medications = findViewById(R.id.editTextTextMedications);
        allergies = findViewById(R.id.editTextTextAllergies);
        usersSpinner = findViewById(R.id.spinner);
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
        countryAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, countries);
        country_obj.setAdapter(countryAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
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

    public void AddUser(View view)
    {
        if (firstName.getText().length() == 0 || lastName.getText().length() == 0)
        {
            Toast.makeText(this, "Name and lastname is required", Toast.LENGTH_LONG).show();
            return;
        }

        for (User var : userList)
        {
            if (var.getName() == firstName.getText().toString())
            {
                Toast.makeText(this, "Name exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        User newUser = new User(firstName.getText().toString(),lastName.getText().toString());

        newUser.SetCNumber(citizenNumber.getText().toString());
        newUser.SetCountry(countrySpinner.getSelectedItem().toString());
        newUser.SetAllergies(allergies.getText().toString());
        newUser.SetMedications(medications.getText().toString());
        newUser.SetAge(age.getText().toString());

        selectedUser = newUser;
        userList.add(newUser);
        userNames.add(newUser.GetName());

        SaveUserToDataBase(newUser);

        SharedPreferences settings = getApplicationContext().getSharedPreferences("CountryPreference", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Country", newUser.GetCountry());

        editor.apply();

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersSpinner.setAdapter(adapter);
        usersSpinner.setSelection(adapter.getPosition(selectedUser.GetName()));
    }

    private void SaveUserToDataBase(User newUser)
    {
        try {
            db.open();
            long id;

            //Log.i("DB","insert " + newUser.GetName() +" "+ newUser.GetLastName() + " "+newUser.GetCNumber()+" "+ newUser.GetCountry()+ " " +newUser.GetMedications()+" " +newUser.GetAllergies());
            id = db.insertUser(newUser.GetName(), newUser.GetLastName(), age.getText().toString(),newUser.GetCNumber(),
                    newUser.GetCountry(), newUser.GetMedications(), newUser.GetAllergies());

            newUser.SetID(id);

            db.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void AddNewUser(View view)
    {
        ClearAllInputs();
    }

    private void UserSpinner()
    {
        usersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long di) {

                for (User var: userList)
                {
                     if (var.GetName() == usersSpinner.getSelectedItem().toString())
                     {
                         selectedUser = var;
                         UpdateValues(selectedUser);
                     }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void GetAllUserFromDB()
    {
        try {
            db.open();

            Cursor c = db.printAllUsers();
            if (c.moveToFirst()){
                do {
                    User user = new User(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(7));
                    //Log.i("DB", "add" + c.getLong(0)+c.getString(1)+c.getString(2)+c.getString(3)+c.getString(4)+c.getString(5)+c.getString(6));
                    //Log.i("DB","insert " + user.GetID() + user.GetName() +" "+ user.GetLastName() + " "+user.GetCNumber()+" "+ user.GetCountry()+ " " +user.GetMedications()+" " +user.GetAllergies());

                    userList.add(user);
                    userNames.add(user.GetName());
                }while (c.moveToNext());
            }
            db.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void printAllFromDB() {

        try {
            db.open();

            Cursor c = db.printAllUsers();
            if (c.moveToFirst()){
                do {
                    Log.i("DB","Ispis : ID = " + c.getString(0) + " IME = " + c.getString(1) + " "+ c.getString(2)+ " "+ c.getString(3)+ " "+ c.getString(4)+ " "+ c.getString(5)+ " "+ c.getString(6));
                }while (c.moveToNext());
            }
            db.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void UpdateValues(User newValues)
    {
        Log.i("DB", newValues.GetName() + newValues.GetLastName() + newValues.GetAge() + newValues.GetCNumber() + newValues.GetAllergies() + newValues.GetMedications());
        firstName.setText(newValues.GetName());
        lastName.setText(newValues.GetLastName());
        age.setText(newValues.GetAge());
        citizenNumber.setText(newValues.GetCNumber());
        medications.setText(newValues.GetMedications());
        allergies.setText(newValues.GetAllergies());
        countrySpinner.setSelection(countryAdapter.getPosition(newValues.GetCountry()));

        SharedPreferences settings = getApplicationContext().getSharedPreferences("CountryPreference", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Country", selectedUser.GetCountry());

        editor.apply();
    }

    private void ClearAllInputs()
    {
        firstName.setText("");
        lastName.setText("");
        age.setText("");
        citizenNumber.setText("");
        medications.setText("");
        allergies.setText("");
    }
}
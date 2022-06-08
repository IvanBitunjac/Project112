package com.example.project112;

import androidx.appcompat.app.AppCompatActivity;

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

    ArrayAdapter<String> adapter;


    private SharedPreferences userData;
    private SharedPreferences.Editor userDataEditor;

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
        printAllFromDB();

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

        //userData = getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE);
        //ReadUserDataFromPreferences();
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

    public void BtnSaveDataClicked(View view){
        userDataEditor = userData.edit();

        userDataEditor.putString("firstName", firstName.getText().toString());
        userDataEditor.putString("lastName", lastName.getText().toString());
        userDataEditor.putString("citizenNumber", citizenNumber.getText().toString());

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

    public void AddUser(View view)
    {
        if (firstName.getText().length() == 0)
        {
            Toast.makeText(this, "Name is required", Toast.LENGTH_LONG).show();
            return;
        }

        for (User var : userList)
        {
            if (firstName.getText().toString() == var.getName())
            {
                 UpdateUserToDB(var);
            }
        }

        User newUser = new User(firstName.getText().toString(),lastName.getText().toString());

        newUser.SetCNumber(citizenNumber.getText().toString());
        newUser.SetCountry(countrySpinner.getSelectedItem().toString());
        newUser.SetAllergies(allergies.getText().toString());
        newUser.SetMedications(medications.getText().toString());

        selectedUser = newUser;
        userList.add(newUser);
        userNames.add(newUser.GetName());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersSpinner.setAdapter(adapter);
        usersSpinner.setSelection(adapter.getPosition(selectedUser.GetName()));

        SaveUserToDataBase(newUser);
    }

    private void UpdateUserToDB(User user)
    {

    }

    private void SaveUserToDataBase(User newUser)
    {
        try {
            db.open();
            long id;

            id = db.insertUser(newUser.GetName(), newUser.GetLastName(), newUser.GetCNumber(),
                    newUser.GetCountry(), newUser.GetMedications(), newUser.GetAllergies());

            newUser.SetID(id);

            db.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        printAllFromDB();
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
                    User user = new User(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getString(6),c.getString(6));
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
        firstName.setText(newValues.GetName());
        lastName.setText(newValues.GetLastName());
        age.setText(newValues.GetAge());
        citizenNumber.setText(newValues.GetCNumber());
        medications.setText(newValues.GetMedications());
        allergies.setText(newValues.GetAllergies());
    }

    private void ClearAllInputs()
    {
        firstName.setText("");
        lastName.setText("");
        age.setText("");
        citizenNumber.setText("");
        medications.setText("Medications");
        allergies.setText("Allergies");
    }
}
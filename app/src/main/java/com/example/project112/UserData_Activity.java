package com.example.project112;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.List;

public class UserData_Activity extends AppCompatActivity {

    //User data info
    private EditText firstName;
    private EditText lastName;
    private EditText citizenNumber;
    private EditText age;

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
        age = findViewById(R.id.editTextAge);

        userData = getSharedPreferences("UserDataPreferences", Context.MODE_PRIVATE);

        ReadUserDataFromPreferences();

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
        age.setText(userData.getString("age", ""));
    }

}
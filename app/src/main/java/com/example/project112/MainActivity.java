package com.example.project112;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void emergancyOnClick(View view)
    {
        Intent intent = new Intent(this, Emergancy_Activity.class);
        startActivity(intent);
    }

    public void locateOnClick(View view)
    {
        Intent intent = new Intent(this, Locate_Activity.class);
        startActivity(intent);
    }

    public void personalDataOnClick(View view)
    {
        Intent intent = new Intent(this, UserData_Activity.class);
        startActivity(intent);
    }
}
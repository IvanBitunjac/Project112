package com.example.project112;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int CALLS_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*  PHONE CALL PERMISSION */
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
            requestCallsPermission();
        }  //Comment out this if-statement if it doesnt work, and put CheckCallPermission method instead
        //CheckCallPermission(CALLS_PERMISSION_CODE);
    }

    public void CheckCallPermission(int requestCode)
    {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, requestCode);
        }
    }

    public void locateOnClick(View view)
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void personalDataOnClick(View view)
    {
        Intent intent = new Intent(this, UserData_Activity.class);
        startActivity(intent);
    }

    public void btnEmergencyOnClick(View view)
    {
        String emergencyGeneralNumber = "112";
        CallEmergency(emergencyGeneralNumber);
    }

    public void btnFirebrigadeCallClicked(View view){
        String firebrigadeNumber = "193";
        CallEmergency(firebrigadeNumber);
    }

    public void btnHospitalCallClicked(View view){
        String hospitalNumber = "194";
        CallEmergency(hospitalNumber);
    }

    public void btnPoliceCallClicked(View view){
        String policeNumber = "192";
        CallEmergency(policeNumber);
    }

    public void btnSeaRescueCallClicked(View view){
        String seaRescueNumber = "195";
        CallEmergency(seaRescueNumber);
    }

    public void CallEmergency(String callNumber){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + callNumber));
        startActivity(callIntent);
    }

    public void requestCallsPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE))
        {
            //Explain why permission is needed
           new AlertDialog.Builder(this).setTitle("Permission necessary!")
                   .setMessage("Calls permission is necessary to call for help in case of an emergency!")
                   .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                       //OnClick even of OK button
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           ActivityCompat.requestPermissions(MainActivity.this,
                                   new String[] {Manifest.permission.CALL_PHONE}, CALLS_PERMISSION_CODE);
                       }
                   })
                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                       //OnClick even of Cancel button
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Permission is necessary, it will be asked again next time",
                                    Toast.LENGTH_SHORT).show();
                       }
                   }).create().show();
        } else {
            //If explanation is not needed
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE}, CALLS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == CALLS_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission for calls GRANTED. Thank you!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission for calls DENIED, please allow the permission!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
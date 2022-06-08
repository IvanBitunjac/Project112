package com.example.project112;

import androidx.annotation.VisibleForTesting;

public class User {
    private long _id;
    private String _name;
    private String _lastname;
    private String _age = "";
    private String _cNumber = "";
    private String _country = "";
    private String _medications = "";
    private String _allergies = "";

    public User(String name, String lastName)
    {
        _name = name;
        _lastname = lastName;
    }

    public User(long id, String name, String lastName, String age, String cNumber,String country,String medications,String allergies)
    {
        _id = id;
        _name = name;
        _lastname = lastName;
        _age =age;
        _cNumber = cNumber;
        _country = country;
        _medications = medications;
        _allergies = allergies;
    }

    public String GetName()
    {
        return _name;
    }

    public String GetLastName()
    {
        return _lastname;
    }

    public String GetAge()
    {
        return _age;
    }
    public void SetAge(String age)
    {
         _age = age;
    }

    public String GetCNumber()
    {
        return _cNumber;
    }

    public void SetCNumber(String cNumber) { _cNumber = cNumber; }

    public String GetCountry()
    {
        return _country;
    }
    public void SetCountry(String country) { _country = country; }


    public String GetMedications()
    {
        return _medications;
    }
    public void SetMedications(String medications) {  _medications = medications; }


    public String GetAllergies()
    {
        return _allergies;
    }
    public void SetAllergies(String allergies) { _allergies = allergies; }


    public void SetID(long id)
    {
        _id = id;
    }
    public Long GetID()
    {
        return  _id ;
    }

    public String getName(){
        return this._name;
    }
}

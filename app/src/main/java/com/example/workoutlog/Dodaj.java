package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Dodaj extends AppCompatActivity {

    EditText editTextWaga;
    TextView editTextData, textViewWaga, textViewEditWeight;
    CalendarView kalendarzWyborDaty;
    String data, strMonth, strDay;
    LocalDate ldt;
    private ControllerDB controllerDB;
    NumberPicker numberPickerJednosci;
    NumberPicker numberPickerDziesietne;
    ArrayList<Rekord> wagaArrayList;
    String waga, value, value2, jednosciInteger="90", dziesietneInteger="0";
    String weightIdExtra;
    Bundle extras;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controllerDB = new ControllerDB(Dodaj.this);
        setContentView(R.layout.activity_dodaj);

       // editTextWaga = findViewById(R.id.editTextDodajWagę);
        editTextData = findViewById(R.id.editTextData);
        textViewWaga = findViewById(R.id.textViewWaga);


        ldt = java.time.LocalDate.now();
        editTextData.setText(String.valueOf(ldt));

        numberPickerJednosci = (NumberPicker)findViewById(R.id.npJednosci);
        numberPickerDziesietne = (NumberPicker)findViewById(R.id.npDziesietne);


        wagaArrayList = controllerDB.wczytajOstatniaWage();

        numberPickerJednosci.setMaxValue(500);
        numberPickerJednosci.setMinValue(0);

        numberPickerDziesietne.setMaxValue(9);
        numberPickerDziesietne.setMinValue(0);
        textViewWaga.setText(90.0+" kg");
        numberPickerJednosci.setValue(90);

        extras = getIntent().getExtras();

        if(extras != null)
        {
            textViewEditWeight = findViewById(R.id.textViewDOdajWage);
            textViewEditWeight.setText("EDYCJA WAGI");
            weightIdExtra = extras.getString("weightId");
            waga = String.valueOf(controllerDB.getWeightById(Integer.parseInt(weightIdExtra)).get(0).getWaga());
            editTextData.setText(controllerDB.getWeightById(Integer.parseInt(weightIdExtra)).get(0).getData());
            setUpActivity(waga);
        }
        else if(wagaArrayList.size()>0)
        {
            waga = String.valueOf(wagaArrayList.get(0).getWaga());
            setUpActivity(waga);
        }


        if (extras != null) {
            weightIdExtra = extras.getString("weightId");
            Log.d("test edycja Id: ",""+weightIdExtra);
            Log.d("test edycja waga: ",""+controllerDB.getWeightById(Integer.parseInt(weightIdExtra)).get(0).getWaga());
            Log.d("test edycja data: ",""+controllerDB.getWeightById(Integer.parseInt(weightIdExtra)).get(0).getData());
        }

        numberPickerJednosci.setOnValueChangedListener(new MyListenerJednosci());

        editTextData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });





        numberPickerDziesietne.setOnValueChangedListener(new MyListenerDziesietne());

        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);







    }
    private class MyListenerJednosci implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            //get new value and convert it to String
            //if you want to use variable value elsewhere, declare it as a field
            //of your main function
            jednosciInteger = "" + newVal;
            //Log.d("Test zapisywanie:", jednosciInteger);
            textViewWaga.setText(jednosciInteger+"."+dziesietneInteger+" kg");

        }
    }
    private class MyListenerDziesietne implements NumberPicker.OnValueChangeListener {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            //get new value and convert it to String
            //if you want to use variable value elsewhere, declare it as a field
            //of your main function
            dziesietneInteger = "" + newVal;
           // Log.d("Test zapisywanie:", dziesietneInteger);
            textViewWaga.setText(jednosciInteger+"."+dziesietneInteger+" kg");

        }
    }

    private void setUpActivity(String waga){
        textViewWaga.setText(waga+" kg");
        int indexOfDecimal = waga.indexOf(".");
        int jednosci = Integer.parseInt(waga.substring(0, indexOfDecimal));
        jednosciInteger = String.valueOf(jednosci);
        numberPickerJednosci.setValue(jednosci);
        jednosci = Integer.parseInt(waga.substring(indexOfDecimal + 1));
        dziesietneInteger = String.valueOf(jednosci);
        numberPickerDziesietne.setValue(jednosci);
    }



    public void Zapisz(View view) {

        LocalTime time = LocalTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formatDateTime = time.format(myFormatObj);

        if(extras == null)
        {
            if (wagaArrayList.size()==0)
            {
                controllerDB.updateUserData(jednosciInteger+"."+dziesietneInteger,"BWEIGHT");
                controllerDB.updateUserData(String.valueOf(editTextData.getText()),"BDATE");
            }
            controllerDB.addWeight(Double.valueOf(jednosciInteger+"."+dziesietneInteger) ,String.valueOf(editTextData.getText()) ,formatDateTime);
            Home(null);
        }
        else
        {
            Rekord rekord = new Rekord(Integer.parseInt(weightIdExtra),Double.valueOf(jednosciInteger+"."+dziesietneInteger) ,String.valueOf(editTextData.getText()) ,formatDateTime);
            controllerDB.updateWeight(rekord);
            Historia(null);
        }
        finish();
    }

    public void datePicker(){
        int mYear, mMonth, mDay;
        // Get Current Date
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        if(extras != null)
        {
            ldt = LocalDate.parse(controllerDB.getWeightById(Integer.parseInt(weightIdExtra)).get(0).getData());
            mYear = ldt.getYear();
            mMonth = ldt.getMonthValue()-1;
            mDay = ldt.getDayOfMonth();
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(Dodaj.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    monthOfYear = monthOfYear +1;
                    strDay = dayOfMonth+"";
                    if(strDay.length() == 1) strDay = "0"+strDay;

                    strMonth = monthOfYear+"";
                    if(strMonth.length() == 1) strMonth = "0"+strMonth;

                    String data = year + "-"+strMonth+"-"+strDay;

                    editTextData.setText(data);


                }, mYear, mMonth, mDay);


        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.show();
    }
    public void Statystyki(View view) {
        Intent statystyki = new Intent(this, Wykresy.class);
        startActivity(statystyki);
        finish();
    }

    public void Historia(View view) {
        Intent historia = new Intent(this, Historia.class);
        startActivity(historia);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Dodaj(View view) {
        Intent dodaj = new Intent(this, Dodaj.class);
        startActivity(dodaj);
        finish();
    }

    public void Home(View view) {
        Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
        finish();
    }
    public void Ustawienia(View view) {
        Intent ustawienia = new Intent(this, Ustawienia.class);
        startActivity(ustawienia);
        finish();
    }




}
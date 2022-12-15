package com.example.workoutlog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class DaneUzytkownika extends AppCompatActivity {

    private ControllerDB controllerDB;
    TextView etHeight, etTargetWeight, etTargetDate, etTodayDate, etTodayWeight;
    int mYear, mMonth, mDay;
    String strDay, strMonth, todayWeight, todayDate;
    LocalDate ldt;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dane_uzytkownika);
        controllerDB = new ControllerDB(DaneUzytkownika.this);
        //controllerDB.createDataTable();
        etHeight = findViewById(R.id.etSetHeight);
        etTargetWeight = findViewById(R.id.etSetTargetWeight);
        etTargetDate = findViewById(R.id.etSetTargetDate);


        todayWeight = controllerDB.getData(4);
        etHeight.setText(controllerDB.getData(1)+" cm");
        etTargetWeight.setText(controllerDB.getData(2)+" kg");
        etTargetDate.setText(controllerDB.getData(3));
        etTodayWeight.setText(controllerDB.getData(4)+" kg");
        ldt = java.time.LocalDate.now();
        etTodayDate.setText(String.valueOf(ldt));

        etTodayWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                openDialog(0);
            }
        });
        etTargetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                openDialog(1);
            }
        });
        etTargetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                datePicker(0);
            }
        });
        etTodayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                datePicker(1);
            }
        });
        etHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                addHeight();
            }
        });
    }






    private void openDialog(int i) {
        //Inflating a LinearLayout dynamically to add TextInputLayout
        //This will be added in AlertDialog
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.view_number_dialog, null);
        NumberPicker numberpicker = (NumberPicker) linearLayout.findViewById(R.id.numberPicker1);
        NumberPicker numberPicker1 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker2);


        numberpicker.setMinValue(0);
        numberpicker.setMaxValue(400);
        numberpicker.setValue(75);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(9);
        numberPicker1.setValue(0);

        //Finally building an AlertDialog
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("Zapisz", null)
                .setNegativeButton("Anuluj", null)
                .setView(linearLayout)
                .setCancelable(false)
                .create();
        builder.show();
        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = numberpicker.getValue()+"."+numberPicker1.getValue();
                if (i==1){
                    etTargetWeight.setText(weight+" kg");
                    controllerDB.updateUserData(weight,"TWEIGHT");
                }
                else{
                    etTodayWeight.setText(weight+" kg");
                    todayWeight = weight;
                    Log.d("test 10",todayWeight);
                }
                builder.cancel();
            }
        });
    }
    public void datePicker(int i){

        // Get Current Date
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(DaneUzytkownika.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    monthOfYear = monthOfYear +1;
                    strDay = dayOfMonth+"";
                    if(strDay.length() == 1) strDay = "0"+strDay;

                    strMonth = monthOfYear+"";
                    if(strMonth.length() == 1) strMonth = "0"+strMonth;

                    String data = year + "-"+strMonth+"-"+strDay;

                    if (i == 0){
                        etTargetDate.setText(data);
                        controllerDB.updateUserData(data, "TDATE");
                    }else{
                        etTodayDate.setText(data);
                        todayDate = data;
                        Log.d("test 11",todayDate);
                    }


                }, mYear, mMonth, mDay);



        datePickerDialog.show();
    }
    public void addHeight(){
        final NumberPicker numberPicker = new NumberPicker(DaneUzytkownika.this);
        numberPicker.setMaxValue(250);
        numberPicker.setMinValue(50);
        numberPicker.setValue(150);


        AlertDialog.Builder builder = new AlertDialog.Builder(DaneUzytkownika.this);
        builder.setView(numberPicker);
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                etHeight.setText(numberPicker.getValue()+" cm");
                controllerDB.updateUserData(String.valueOf(numberPicker.getValue()),"HEIGHT");
            }

        });
        builder.setTitle("Podaj swój wzrost:");

        builder.setNegativeButton("Anuluj", null);

        builder.create();
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveUserData(View view) {


        finish();
    }
}
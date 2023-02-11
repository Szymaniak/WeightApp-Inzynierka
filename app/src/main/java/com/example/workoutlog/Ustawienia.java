package com.example.workoutlog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Ustawienia extends AppCompatActivity {

    private ControllerDB controllerDB;
    TextView etHeight, etTargetWeight, etTargetDate;
    Button buttonDeleteWeightDatabase, buttonDownloadData;
    LocalDate ldt, ldt2;
    int mYear, mMonth, mDay;
    String strDay, strMonth, todayWeight, todayDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ustawienia);
        controllerDB = new ControllerDB(Ustawienia.this);
        etHeight = findViewById(R.id.etSetHeight);
        etTargetWeight = findViewById(R.id.etSetTargetWeight);
        etTargetDate = findViewById(R.id.etSetTargetDate);

        etHeight.setText(controllerDB.getData(1) + " cm");
        etTargetWeight.setText(controllerDB.getData(2) + " kg");
        etTargetDate.setText(controllerDB.getData(3));
        ldt = java.time.LocalDate.now();

        buttonDownloadData = findViewById(R.id.buttonDownloadDatabase);
        buttonDeleteWeightDatabase = findViewById(R.id.buttonResetWeightDatabase);

        etHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                addHeight();
            }
        });
        etTargetWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                openDialog();
            }
        });
        etTargetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                datePicker(0);
            }
        });
    }


    private void openDialog() {
        //Inflating a LinearLayout dynamically to add TextInputLayout
        //This will be added in AlertDialog
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.view_number_dialog, null);
        NumberPicker numberpicker = (NumberPicker) linearLayout.findViewById(R.id.numberPicker1);
        NumberPicker numberPicker1 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker2);
        String weight = controllerDB.getData(2);
        Double weightD = Double.valueOf(weight);
        int weightI;
        weightI = (int) (weightD * 10);


        numberpicker.setMinValue(0);
        numberpicker.setMaxValue(400);
        numberpicker.setValue(weightI/10);
        numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(9);
        numberPicker1.setValue(weightI%10);

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
                String weight = numberpicker.getValue() + "." + numberPicker1.getValue();

                etTargetWeight.setText(weight + " kg");

                controllerDB.updateUserData(weight, "TWEIGHT");


                builder.cancel();
            }
        });
    }

    public void datePicker(int i) {

        // Get Current Date
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Ustawienia.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    monthOfYear = monthOfYear + 1;
                    strDay = dayOfMonth + "";
                    if (strDay.length() == 1) strDay = "0" + strDay;

                    strMonth = monthOfYear + "";
                    if (strMonth.length() == 1) strMonth = "0" + strMonth;

                    String data = year + "-" + strMonth + "-" + strDay;


                    etTargetDate.setText(data);
                    controllerDB.updateUserData(data, "TDATE");


                }, mYear, mMonth, mDay);

        datePickerDialog.getDatePicker().setFirstDayOfWeek(Calendar.MONDAY);
        datePickerDialog.show();
    }

    public void addHeight() {
        final NumberPicker numberPicker = new NumberPicker(Ustawienia.this);
        final int[] height = {Integer.parseInt(controllerDB.getData(1))};

        numberPicker.setMaxValue(250);
        numberPicker.setMinValue(50);
        numberPicker.setValue(height[0]);


        AlertDialog.Builder builder = new AlertDialog.Builder(Ustawienia.this);
        builder.setView(numberPicker);
        builder.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                height[0] = numberPicker.getValue();

                etHeight.setText(height[0] + " cm");

                controllerDB.updateUserData(String.valueOf(height[0]), "HEIGHT");
            }

        });
        builder.setTitle("Podaj swój wzrost:");

        builder.setNegativeButton("Anuluj", null);

        builder.create();
        builder.show();
    }

    public void deleteDatabase(View view) {
        controllerDB.deleteDataFromWeightTable();
        //controllerDB.createWeightTable();
    }

    public void downloadData(View view) throws IOException {
        StringBuilder dataToDownload = new StringBuilder();
        String date, time, weight;
        Date now = new Date();

        dataToDownload.append("Notatnik Wagi kopia danych "+now);
        dataToDownload.append(System.getProperty("line.separator"));

        dataToDownload.append("data,czas,waga");
        dataToDownload.append(System.getProperty("line.separator"));

        for(int i = 0; i<controllerDB.loadWeights().size(); i++){
            date = controllerDB.loadWeights().get(i).getData();
            time = controllerDB.loadWeights().get(i).getTime();
            weight = String.valueOf(controllerDB.loadWeights().get(i).getWaga());

            dataToDownload.append(date+","+time+","+weight);
            dataToDownload.append(System.getProperty("line.separator"));
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, (Serializable) dataToDownload);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.google.android.apps.docs");
        startActivity(sendIntent);
    }

    public void Statystyki(View view) {
        Intent statystyki = new Intent(this, Wykresy.class);
        statystyki.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(statystyki);
    }

    public void Historia(View view) {
        Intent historia = new Intent(this, Historia.class);
        historia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(historia);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Dodaj(View view) {
        Intent dodaj = new Intent(this, Dodaj.class);
        dodaj.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(dodaj);
    }

    public void Home(View view) {
        Intent home = new Intent(this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);

    }


    public void Ustawienia(View view) {
        Intent ustawienia = new Intent(this, Ustawienia.class);
        ustawienia.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ustawienia);
    }


}
package com.example.workoutlog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ControllerDB controllerDB;
    Button btnwprowadzDane;
    int mYear, mMonth, mDay;
    String strDay, strMonth;
    TextView textViewCurrentWeight, textViewStartWeight, textViewTargetWeight, textViewWeightDifference, textViewWeightLeft;
    TextView textViewStartDate, textViewTargetDate, textViewDaysLeft;
    TextView textViewMonthlyWeightLost, textViewWeeklyWeightLost, textViewDailyWeightLost;
    TextView textViewCurrentBMI, textViewStartBMI,textViewDifferenceBMI, textViewCurrentBMIText, textViewBMILeft;
    SharedPreferences prefs = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controllerDB = new ControllerDB(this);
        Obliczenia obliczenia = new Obliczenia();
        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        onResume();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        if(controllerDB.wczytajWage().size()==0){
            controllerDB.addWeight(90.0,"2022-11-04","17:12:12");
            controllerDB.addWeight(93.6,"2022-11-07","17:12:12");
            controllerDB.addWeight(92.2,"2022-11-11","17:12:12");
            controllerDB.addWeight(95.8,"2022-11-14","17:12:12");
            controllerDB.addWeight(99.5,"2022-11-19","17:12:12");
            controllerDB.addWeight(89.1,"2022-11-21","17:12:12");
            controllerDB.addWeight(91.0,"2022-11-27","17:12:12");
            controllerDB.addWeight(95.9,"2022-12-04","17:12:12");
            controllerDB.addWeight(92.9,"2022-12-09","17:12:12");
            controllerDB.addWeight(94.0,"2022-12-12","17:12:12");
            controllerDB.addWeight(91.0,"2022-12-16","17:12:12");
        }


        textViewStartDate = findViewById(R.id.textViewStartDate);
        textViewDaysLeft = findViewById(R.id.textViewDaysLeft);
        textViewTargetDate = findViewById(R.id.textViewTargetDate);

        textViewStartWeight = findViewById(R.id.textViewStartWeight);
        textViewWeightDifference = findViewById(R.id.textViewDifferenceWeight);
        textViewTargetWeight = findViewById(R.id.textViewTargetWeight);
        textViewCurrentWeight = findViewById(R.id.textViewCurrentWeight);
        textViewWeightLeft = findViewById(R.id.textViewWeightLeft);

        textViewCurrentBMI = findViewById(R.id.textViewCurrentBMI);
        textViewCurrentBMIText = findViewById(R.id.textViewCurrentBMIText);
        textViewDifferenceBMI = findViewById(R.id.textViewDiferrenceBMI);
        textViewStartBMI = findViewById(R.id.textViewStartBMI);
        textViewBMILeft = findViewById(R.id.textViewBMILeft);

        textViewDailyWeightLost = findViewById(R.id.textViewDayilyWeightLost);
        textViewWeeklyWeightLost = findViewById(R.id.textViewWeeklyWeightLost);
        textViewMonthlyWeightLost = findViewById(R.id.textViewMonthlyWeightLost);

        if(controllerDB.wczytajWage().size()>0) {
            String bmiStartStr, bmiCurrentStr, bmiDifferencestr, difference;

            bmiStartStr = obliczenia.BMI(controllerDB.getData(1),controllerDB.wczytajWage().get(0).getWaga());
            bmiCurrentStr = obliczenia.BMI(controllerDB.getData(1),
                    controllerDB.wczytajWage().get(controllerDB.wczytajWage().size()-1).getWaga());
            bmiDifferencestr = obliczenia.difference(Double.valueOf(bmiStartStr), Double.parseDouble(bmiCurrentStr));

            textViewStartBMI.setText(bmiStartStr);
            textViewCurrentBMI.setText(bmiCurrentStr);
            textViewDifferenceBMI.setText(bmiDifferencestr);

            if(Double.parseDouble(bmiCurrentStr)>25)
                textViewBMILeft.setText(obliczenia.difference(Double.parseDouble(bmiCurrentStr),25.0).replaceAll("[-+]",""));
            else if(Double.parseDouble(bmiCurrentStr)<18.5)
                textViewBMILeft.setText(obliczenia.difference(25.0,Double.parseDouble(bmiCurrentStr)).replaceAll("[-+]",""));
            else
                textViewBMILeft.setText("0");

            List<String> BMIText = Arrays.asList("Wygłodzenie", "Wychudzenie" ,"Niedowaga", "Waga prawidłowa" ,"Nadwaga",
                    "Otyłość I stopnia" ,"Otyłość II stopnia" ,"Otyłość skrajna");

            if(Double.parseDouble(bmiCurrentStr)<16) textViewCurrentBMIText.setText(BMIText.get(0));
            else if(Double.parseDouble(bmiCurrentStr)>=16 & Double.parseDouble(bmiCurrentStr)<17) textViewCurrentBMIText.setText(BMIText.get(1));
            else if(Double.parseDouble(bmiCurrentStr)>=17 & Double.parseDouble(bmiCurrentStr)<18.5) textViewCurrentBMIText.setText(BMIText.get(2));
            else if(Double.parseDouble(bmiCurrentStr)>=18.5 & Double.parseDouble(bmiCurrentStr)<25) textViewCurrentBMIText.setText(BMIText.get(3));
            else if(Double.parseDouble(bmiCurrentStr)>=25 & Double.parseDouble(bmiCurrentStr)<30) textViewCurrentBMIText.setText(BMIText.get(4));
            else if(Double.parseDouble(bmiCurrentStr)>=30 & Double.parseDouble(bmiCurrentStr)<35) textViewCurrentBMIText.setText(BMIText.get(5));
            else if(Double.parseDouble(bmiCurrentStr)>=35 & Double.parseDouble(bmiCurrentStr)<40) textViewCurrentBMIText.setText(BMIText.get(6));
            else if(Double.parseDouble(bmiCurrentStr)>=40) textViewCurrentBMIText.setText(BMIText.get(7));

            difference = obliczenia.difference(Double.parseDouble(controllerDB.getData(4)),
                    controllerDB.wczytajWage().get(controllerDB.wczytajWage().size()-1).getWaga());

            textViewCurrentWeight.setText(controllerDB.wczytajWage().get(controllerDB.wczytajWage().size()-1).getWaga()+" kg");
            textViewStartWeight.setText(controllerDB.getData(4)+" kg");
            textViewTargetWeight.setText(controllerDB.getData(2)+" kg");
            textViewWeightDifference.setText(difference+" kg");
            textViewWeightLeft.setText(obliczenia.difference(controllerDB.wczytajWage().get(controllerDB.wczytajWage().size()-1).getWaga(),
                    Double.valueOf(controllerDB.getData(2))).replaceAll("[-+]","")+" kg");

            textViewStartDate.setText(controllerDB.getData(5));
            textViewTargetDate.setText(controllerDB.getData(3));
            long daysBetween = ChronoUnit.DAYS.between(LocalDate.parse(controllerDB.getData(5)),
                    LocalDate.parse(controllerDB.getData(3)));
            textViewDaysLeft.setText(daysBetween+" dni");

            LocalDate firstDate, currentDate;
            long tempDifference;
            firstDate = LocalDate.parse(controllerDB.getData(5));
            currentDate = LocalDate.parse(controllerDB.wczytajWage().get(controllerDB.wczytajWage().size()-1).getData());

            tempDifference = ChronoUnit.DAYS.between(firstDate, currentDate);
            textViewDailyWeightLost.setText(averageWeightLoss(tempDifference, difference));

            tempDifference = ChronoUnit.WEEKS.between(firstDate, currentDate);
            textViewWeeklyWeightLost.setText(averageWeightLoss(tempDifference, difference));

            tempDifference = ChronoUnit.MONTHS.between(firstDate, currentDate);
            textViewMonthlyWeightLost.setText(averageWeightLoss(tempDifference, difference));
        }
    }
    public String averageWeightLoss(long time, String weight){
        BigDecimal bigDecimalWeight = BigDecimal.valueOf(Double.parseDouble(weight));
        BigDecimal bigDecimalTime = BigDecimal.valueOf(time).add(BigDecimal.valueOf(1));

        bigDecimalWeight = bigDecimalWeight.divide(bigDecimalTime, 1, RoundingMode.HALF_UP);

        return  String.valueOf(bigDecimalWeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            controllerDB.createDataTable();

            Intent firstRun = new Intent(this, DaneUzytkownika.class);
            startActivity(firstRun);

            prefs.edit().putBoolean("firstrun", false).commit();


        }
    }

    private static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getDatabasePath("weightdb");
        return dbFile.exists();
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
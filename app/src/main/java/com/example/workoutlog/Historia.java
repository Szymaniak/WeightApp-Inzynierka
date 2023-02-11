package com.example.workoutlog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Historia extends AppCompatActivity {
    LinearLayout linearLayoutDzienna, linearLayoutTygodniowa, linearLayoutMiesieczna;
    Button btnDziennia, btnTygodniowa, btnMiesieczna;
    private ControllerDB controllerDB;
    private ListView listViewHistoria, listViewTygodniowa, listViewMiesieczna;
    ArrayList<Rekord> wagaArrayList = new ArrayList<>();
    ArrayList<AdaperRekord> adapterArrayList = new ArrayList<>();
    ArrayList<String> wagaArrayListLaczana = new ArrayList<>();;
    MainActivity mainActivity = new MainActivity();
    Obliczenia obliczenia;
    ListViewAdapter customAdapter;
    Boolean ifButtonIsClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);
        obliczenia = new Obliczenia();
        controllerDB = new ControllerDB(Historia.this);

        linearLayoutDzienna = findViewById(R.id.linearLayoutDzienny);



        listViewHistoria = (ListView) findViewById(R.id.listViewHistoria);

        btnDziennia = findViewById(R.id.buttonDziennie);
        btnTygodniowa = findViewById(R.id.buttonTygodniowo);
        btnMiesieczna = findViewById(R.id.buttonMiesiecznie);

        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red2));


        //Wczytaj();


       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_2, android.R.id.text2,
                wagaArrayListLaczana);

        listViewHistoria.setAdapter(arrayAdapter);*/


        LoadDailyWeight(null);

        customAdapter = new ListViewAdapter(this, R.layout.adapter_historia, adapterArrayList);
        listViewHistoria.setAdapter(customAdapter);

        listViewHistoria.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(ifButtonIsClicked){
                    editWindow(null, adapterArrayList.get(i).getId());
                }



                int n = adapterArrayList.get(i).getId();
                Log.d("które id: ","   "+n);

                return false;

            }
        });



    }

    public void editWindow(View view, int id) {
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.view_edit_options, null);
        Button btnEdit = (Button) linearLayout.findViewById(R.id.btnEdit);
        Button btnDelete = (Button) linearLayout.findViewById(R.id.btnDelete);
        Button btnSetAsFirst = (Button) linearLayout.findViewById(R.id.btnSetAsFirst);

        final AlertDialog builder = new AlertDialog.Builder(this)
                .setView(linearLayout)
                .setCancelable(true)
                .create();
        builder.show();
        //builder.getWindow().setLayout(600, 530);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(builder.getWindow().getAttributes());
        lp.width = 650;
        lp.height = 700;
        lp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        builder.getWindow().setAttributes(lp);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d("które id2: ","   "+id);
                Intent dodaj = new Intent(view.getContext(), Dodaj.class);
                dodaj.putExtra("weightId",id+"");
                startActivity(dodaj);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerDB.removeWeight(id);
                builder.cancel();
                LoadDailyWeight(null);
            }
        });
        btnSetAsFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllerDB.updateUserData(String.valueOf(controllerDB.getWeightById(id).get(0).getWaga()),"BWEIGHT");
                controllerDB.updateUserData(String.valueOf(controllerDB.getWeightById(id).get(0).getData()),"BDATE");
                builder.cancel();
            }
        });

    }
    public void LoadDailyWeight(View view){
        ifButtonIsClicked = true;
        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red2));
        btnTygodniowa.setBackgroundColor(btnTygodniowa.getContext().getResources().getColor(R.color.red));
        btnMiesieczna.setBackgroundColor(btnMiesieczna.getContext().getResources().getColor(R.color.red));

        adapterArrayList.clear();
        String weightStr,weightDiffStr,dateStr,timeStr;
        for(int i = 0; i<controllerDB.loadWeights().size(); i++){
            weightStr = String.valueOf(controllerDB.loadWeights().get(i).getWaga());
            dateStr = controllerDB.loadWeights().get(i).getData();
            timeStr = controllerDB.loadWeights().get(i).getTime();
            int id = controllerDB.loadWeights().get(i).getId();
            if(i!=0){
                weightDiffStr = obliczenia.difference(controllerDB.loadWeights().get(i-1).getWaga(),
                        controllerDB.loadWeights().get(i).getWaga());
            }
            else{
                weightDiffStr = "";
            }
            AdaperRekord rekord = new AdaperRekord(id,weightStr,weightDiffStr,dateStr,timeStr);
            adapterArrayList.add(rekord);

        }
        Collections.reverse(adapterArrayList);
        listViewHistoria.setAdapter(customAdapter);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void LoadWeeklyWeight(View view){
        ifButtonIsClicked = false;
        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red));
        btnTygodniowa.setBackgroundColor(btnTygodniowa.getContext().getResources().getColor(R.color.red2));
        btnMiesieczna.setBackgroundColor(btnMiesieczna.getContext().getResources().getColor(R.color.red));

        adapterArrayList.clear();

        String weightDiffStr = "",dateStr,timeStr, dateStrPlusOne ="";
        BigDecimal sumaBd = new BigDecimal(0), symaBdTym = new BigDecimal(0);
        int counterBd =0, weekOfYearPlusOne=0;

        Locale userLocale = new Locale("pl");
        WeekFields weekNumbering = WeekFields.of(userLocale);

        for(int i = 0; i<controllerDB.loadWeights().size(); i++){
            timeStr = controllerDB.loadWeights().get(i).getTime();
            int id = controllerDB.loadWeights().get(i).getId();

            dateStr = controllerDB.loadWeights().get(i).getData();
            LocalDate date= LocalDate.parse(dateStr);
            int weekOfYear = date.get(weekNumbering.weekOfWeekBasedYear());

            if(i!=controllerDB.loadWeights().size()-1){
                dateStrPlusOne = controllerDB.loadWeights().get(i+1).getData();
                LocalDate datePlusOne= LocalDate.parse(dateStrPlusOne);
                weekOfYearPlusOne = datePlusOne.get(weekNumbering.weekOfWeekBasedYear());
            }

            sumaBd = sumaBd.add(BigDecimal.valueOf(controllerDB.loadWeights().get(i).getWaga()));
            counterBd++;

            if(i==controllerDB.loadWeights().size()-1||weekOfYear!=weekOfYearPlusOne){
                sumaBd = sumaBd.divide(BigDecimal.valueOf(counterBd),1,RoundingMode.HALF_UP);

                if(adapterArrayList.size()>0){
                    weightDiffStr=obliczenia.difference(Double.valueOf(adapterArrayList.get(adapterArrayList.size()-1).getWaga()), Double.parseDouble(String.valueOf(sumaBd)));
                }

                AdaperRekord rekord = new AdaperRekord(id,sumaBd+"",weightDiffStr,GetFirstAndLastDayOfWeek(date, userLocale),timeStr);
                adapterArrayList.add(rekord);

                sumaBd = new BigDecimal(0);
                counterBd = 0;
            }


        }
        Collections.reverse(adapterArrayList);
        listViewHistoria .setAdapter(customAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String GetFirstAndLastDayOfWeek(LocalDate date, Locale userLocale){
        LocalDate firstDayOfWeek, lastDayOfWeek;
        int firstDayOfWeekStr, lastDayOfWeekStr;
        String month, month2;
        TemporalField fieldISO = WeekFields.of(userLocale).dayOfWeek();
        firstDayOfWeek = date.with(fieldISO, 1);
        lastDayOfWeek = date.with(fieldISO, 7);
        firstDayOfWeekStr = firstDayOfWeek.getDayOfMonth();
        lastDayOfWeekStr = lastDayOfWeek.getDayOfMonth();

        if(firstDayOfWeek.getMonthValue()==lastDayOfWeek.getMonthValue()){
            month = Month.of(firstDayOfWeek.getMonthValue()).getDisplayName( TextStyle.SHORT , userLocale );
            return firstDayOfWeekStr +" - "+ lastDayOfWeekStr+" "+ month;
        }
        else{
            month = Month.of(firstDayOfWeek.getMonthValue()).getDisplayName( TextStyle.SHORT , userLocale );
            month2 = Month.of(lastDayOfWeek.getMonthValue()).getDisplayName( TextStyle.SHORT , userLocale );
            return firstDayOfWeekStr+" " +month+" - "+ lastDayOfWeekStr+" "+ month2;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void LoadMonthlyWeight(View view){
        ifButtonIsClicked = false;
        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red));
        btnTygodniowa.setBackgroundColor(btnTygodniowa.getContext().getResources().getColor(R.color.red));
        btnMiesieczna.setBackgroundColor(btnMiesieczna.getContext().getResources().getColor(R.color.red2));
        adapterArrayList.clear();

        String weightDiffStr = "",dateStr,timeStr, dateStrPlusOne ="", month;
        BigDecimal sumaBd = new BigDecimal(0);
        int counterBd =0, monthOfYearPlusOne=0;

        Locale userLocale = new Locale("pl");
        WeekFields weekNumbering = WeekFields.of(userLocale);

        for(int i = 0; i<controllerDB.loadWeights().size(); i++){
            timeStr = controllerDB.loadWeights().get(i).getTime();
            int id = controllerDB.loadWeights().get(i).getId();

            dateStr = controllerDB.loadWeights().get(i).getData();
            LocalDate date = LocalDate.parse(dateStr);
            int monthOfYear = date.getMonthValue();

            if(i!=controllerDB.loadWeights().size()-1){
                dateStrPlusOne = controllerDB.loadWeights().get(i+1).getData();
                LocalDate datePlusOne= LocalDate.parse(dateStrPlusOne);
                monthOfYearPlusOne = datePlusOne.getMonthValue();
            }

            sumaBd = sumaBd.add(BigDecimal.valueOf(controllerDB.loadWeights().get(i).getWaga()));
            counterBd++;

            if(i==controllerDB.loadWeights().size()-1||monthOfYear!=monthOfYearPlusOne){
                sumaBd = sumaBd.divide(BigDecimal.valueOf(counterBd),1,RoundingMode.HALF_UP);

                if(adapterArrayList.size()>0){
                    weightDiffStr=obliczenia.difference(Double.valueOf(adapterArrayList.get(adapterArrayList.size()-1).getWaga()),
                            Double.parseDouble(String.valueOf(sumaBd)));
                }
                month = Month.of(date.getMonthValue()).getDisplayName( TextStyle.FULL_STANDALONE , userLocale )+
                        " "+date.getYear();

                AdaperRekord rekord = new AdaperRekord(id,sumaBd+"",weightDiffStr,month,timeStr);
                adapterArrayList.add(rekord);

                sumaBd = new BigDecimal(0);
                counterBd = 0;
            }


        }
        Collections.reverse(adapterArrayList);
        listViewHistoria .setAdapter(customAdapter);
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
    //-----------------------------------------------------------------------------



}
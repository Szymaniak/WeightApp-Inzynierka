package com.example.workoutlog;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;


public class Wykresy extends AppCompatActivity {

    Button btnDziennia, btnTygodniowa, btnMiesieczna;
    private LineChart mChart;
    private ControllerDB controllerDB;
    ArrayList<AdaperRekord> adapterArrayList = new ArrayList<>();
    Obliczenia obliczenia;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wykresy);

        controllerDB = new ControllerDB(this);
        obliczenia = new Obliczenia();

        btnDziennia = findViewById(R.id.buttonDziennie);
        btnTygodniowa = findViewById(R.id.buttonTygodniowo);
        btnMiesieczna = findViewById(R.id.buttonMiesiecznie);

        mChart = findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setExtraTopOffset(0);

        LoadDailyWeight(null);
    }
    /* LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);*/
        /*leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void renderData(ArrayList label, int setXAxisMaximum) {
        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setAxisMaximum(setXAxisMaximum);
        xAxis.setAxisMinimum(0f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(label));

        Float llTargetWeight = Float.parseFloat(controllerDB.getData(2));
        LimitLine ll1 = new LimitLine(llTargetWeight, "Cel");
        ll1.setLineWidth(2f);
        ll1.enableDashedLine(30f, 30f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.addLimitLine(ll1);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setAxisMaximum(Math.round(controllerDB.returnMaxWeight()/10)*10+20);
        leftAxis.setAxisMinimum(Math.round(controllerDB.returnMinWeight()/10)*10-20);

        mChart.getAxisRight().setEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDataDailyWeight()
    {
        ArrayList<Entry> values = new ArrayList<>();
        for(int i = 0; i<controllerDB.loadWeights().size(); i++){
            Float weight = Float.valueOf(String.valueOf(controllerDB.loadWeights().get(i).getWaga()));
            values.add(new Entry(i, weight));
        }

        ArrayList<String> label = new ArrayList<>();
        for (int i = 0; i < controllerDB.loadWeights().size(); i++){
            LocalDate date = LocalDate.parse(controllerDB.loadWeights().get(i).getData());
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yy");
            String formatDateTime = date.format(myFormatObj);
            label.add(formatDateTime);
        }

        renderData(label, controllerDB.loadWeights().size()-1);
        setData(values);
    }
    private void setData(ArrayList<Entry> values) {
        mChart.clear();
        LineDataSet set1;
        set1 = new LineDataSet(values, "Waga");
        set1.setColor(Color.CYAN);
        set1.setLineWidth(2f);
        set1.setCircleColor(Color.CYAN);
        set1.setCircleRadius(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setDrawFilled(true);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        mChart.setData(data);
    }

//        set1.setFormLineWidth(1f);
//        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
//        set1.setFormSize(15.f);


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void LoadDailyWeight(View view) {
        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red2));
        btnTygodniowa.setBackgroundColor(btnTygodniowa.getContext().getResources().getColor(R.color.red));
        btnMiesieczna.setBackgroundColor(btnMiesieczna.getContext().getResources().getColor(R.color.red));

        if(controllerDB.loadWeights().size()>0){
            setDataDailyWeight();
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void LoadWeeklyWeight(View view) {
        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red));
        btnTygodniowa.setBackgroundColor(btnTygodniowa.getContext().getResources().getColor(R.color.red2));
        btnMiesieczna.setBackgroundColor(btnMiesieczna.getContext().getResources().getColor(R.color.red));

        if(controllerDB.loadWeights().size()>0){
            adapterArrayList.clear();

            String weightDiffStr = "",dateStr,timeStr, dateStrPlusOne ="";
            BigDecimal sumaBd = new BigDecimal(0);
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
                    sumaBd = sumaBd.divide(BigDecimal.valueOf(counterBd),1, RoundingMode.HALF_UP);

                    if(adapterArrayList.size()>0){
                        weightDiffStr=obliczenia.difference(Double.valueOf(adapterArrayList.get(adapterArrayList.size()-1).getWaga()), Double.parseDouble(String.valueOf(sumaBd)));
                    }

                    AdaperRekord rekord = new AdaperRekord(id,sumaBd+"",weightDiffStr,GetFirstAndLastDayOfWeek(date, userLocale),timeStr);
                    adapterArrayList.add(rekord);

                    sumaBd = new BigDecimal(0);
                    counterBd = 0;
                }


            }

            ArrayList<Entry> values = new ArrayList<>();
            for(int i =0;i<adapterArrayList.size();i++){
                Float weight = Float.valueOf(String.valueOf(adapterArrayList.get(i).getWaga()));
                values.add(new Entry(i, weight));
            }

            ArrayList<String> label = new ArrayList<>();
            for (int i = 0; i < adapterArrayList.size(); i++){
                label.add(adapterArrayList.get(i).getData());
            }

            renderData(label, adapterArrayList.size()-1);
            setData(values);
        }


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
            month = String.valueOf(firstDayOfWeek.getMonthValue());
            return firstDayOfWeekStr +"-"+ lastDayOfWeekStr+"."+ month;
        }
        else{
            month = String.valueOf(firstDayOfWeek.getMonthValue());
            month2 = String.valueOf(lastDayOfWeek.getMonthValue());
            return firstDayOfWeekStr+"." +month+"-"+ lastDayOfWeekStr+"."+ month2;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void LoadMonthlyWeight(View view) {
        btnDziennia.setBackgroundColor(btnDziennia.getContext().getResources().getColor(R.color.red));
        btnTygodniowa.setBackgroundColor(btnTygodniowa.getContext().getResources().getColor(R.color.red));
        btnMiesieczna.setBackgroundColor(btnMiesieczna.getContext().getResources().getColor(R.color.red2));

        if(controllerDB.loadWeights().size()>0){
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
                        weightDiffStr=obliczenia.difference(Double.valueOf(adapterArrayList.get(adapterArrayList.size()-1).getWaga()), Double.parseDouble(String.valueOf(sumaBd)));
                    }
                    month = Month.of(date.getMonthValue()).getDisplayName( TextStyle.SHORT_STANDALONE , userLocale )+
                            " "+date.getYear();

                    AdaperRekord rekord = new AdaperRekord(id,sumaBd+"",weightDiffStr,month,timeStr);
                    adapterArrayList.add(rekord);

                    sumaBd = new BigDecimal(0);
                    counterBd = 0;
                }
            }
            ArrayList<Entry> values = new ArrayList<>();
            for(int i = 0;i<adapterArrayList.size();i++){
                Float weight = Float.valueOf(String.valueOf(adapterArrayList.get(i).getWaga()));
                values.add(new Entry(i, weight));
            }

            ArrayList<String> label = new ArrayList<>();
            for (int i = 0; i < adapterArrayList.size(); i++){
                label.add(adapterArrayList.get(i).getData());
            }

            renderData(label, adapterArrayList.size()-1);
            setData(values);
        }


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
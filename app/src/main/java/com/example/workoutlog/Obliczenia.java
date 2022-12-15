package com.example.workoutlog;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

public class Obliczenia{

    public String BMI(String height, Double weight){

        BigDecimal weightBD = BigDecimal.valueOf(weight);
        BigDecimal heightBD = BigDecimal.valueOf(Double.parseDouble(height));
        heightBD = heightBD.divide(BigDecimal.valueOf(100));
        heightBD = heightBD.multiply(heightBD);
        return String.valueOf(weightBD.divide(heightBD, 1, RoundingMode.HALF_UP));
    }

    public String difference(Double weight1, Double weight2){
        BigDecimal weight1BD = BigDecimal.valueOf(weight1);
        BigDecimal weight2BD = BigDecimal.valueOf(weight2);
        weight1BD = weight2BD.subtract(weight1BD);
        String differenceStr = String.valueOf(weight1BD);
        if(weight1BD.signum()>0)
            differenceStr = "+"+differenceStr;

        return differenceStr;
    }
    public Boolean TablesTest(int i, int j, int k){
        if((i>0) & (j>0))
            return true;
        else
            return false;


    }

}



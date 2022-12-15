package com.example.workoutlog;

public class AdaperRekord {
    private int id;
    private String Waga;
    private String WagaRoznica;
    private String Data;
    private String Time;

    public AdaperRekord(int id, String waga, String wagaRoznica, String data, String time) {
        this.id = id;
        Waga = waga;
        WagaRoznica = wagaRoznica;
        Data = data;
        Time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWaga() {
        return Waga;
    }

    public void setWaga(String waga) {
        Waga = waga;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getWagaRoznica() {
        return WagaRoznica;
    }

    public void setWagaRoznica(String wagaRoznica) {
        WagaRoznica = wagaRoznica;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

}

